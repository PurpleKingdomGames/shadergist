package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg
import tools.cmds.FileReader

import org.scalajs.dom.html
import org.scalajs.dom.document

import org.scalajs.dom.raw.Event
import scala.concurrent.{Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.raw.HTMLImageElement

object BumpToNormal:

  type Model = Option[FileReader.File[html.Image]]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FileSelected(inputFieldId) =>
        (model, FileReader.readImage(inputFieldId, resultToMessage))

      case BumpToNormalMsg.LoadFailed(msg) =>
        (model, Cmd.Empty)

      case BumpToNormalMsg.LoadSucceeded(file) =>
        doSomething(file.path)
        (model, Cmd.Empty)

  def doSomething(imgPath: String): Unit =
    val canvas = document.getElementById("bump-to-normal-canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d")
    ctx.fillStyle = "green"
    ctx.fillRect(0, 0, 150, 150)
    loadImageAsset(ctx, imgPath)
    // TODO Next: https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial/Pixel_manipulation_with_canvas

  def view(model: Model): Html[BumpToNormalMsg] =
    val children: Seq[Html[BumpToNormalMsg]] =
      Seq(
        h1()(text("bump to normal")),
        br,
        input(
          `type`("file"),
          id("image-upload"),
          name("image upload"),
          accept("image/png, image/jpeg, image/gif"),
          onChange(BumpToNormalMsg.FileSelected("image-upload"))
        ),
        canvas(id("bump-to-normal-canvas"), width("150"), height("150"))()
      )
      //  ++ {
      //   model match
      //     case None                   => Seq()
      //     case Some(FileReader.File(path, _)) => Seq(img(src(path)))
      // }

    div()(children: _*)

  def resultToMessage: Either[FileReader.Error, FileReader.File[html.Image]] => BumpToNormalMsg = {
    case Left(e) =>
      BumpToNormalMsg.LoadFailed(e.message)

    case Right(file) =>
      BumpToNormalMsg.LoadSucceeded(file)
  }

  def onLoadFuture(image: HTMLImageElement): Future[HTMLImageElement] =
    if (image.complete) Future.successful(image)
    else {
      val p = Promise[HTMLImageElement]()
      image.onload = { (_: Event) =>
        p.success(image)
      }
      image.addEventListener(
        "error",
        (_: Event) => p.failure(new Exception("Image load error")),
        false
      )
      p.future
    }

  @SuppressWarnings(Array("scalafix:DisableSyntax.asInstanceOf"))
  def loadImageAsset(ctx: scala.scalajs.js.Dynamic, path: String): Unit = {
    val image: html.Image = document.createElement("img").asInstanceOf[html.Image]
    image.src = path

    onLoadFuture(image).map { i =>
      val canvas = document.getElementById("bump-to-normal-canvas").asInstanceOf[html.Canvas]
      canvas.width = i.width
      canvas.height = i.height
      ctx.drawImage(i, 0, 0)
    }
  }
