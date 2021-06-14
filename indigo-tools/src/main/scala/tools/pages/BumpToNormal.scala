package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg
import tools.cmds.FileReader
import tools.cmds.ImageLoader
import tools.cmds.Logger

import org.scalajs.dom.html
import org.scalajs.dom.document

object BumpToNormal:

  type Model = Option[FileReader.File[html.Image]]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FileSelected(inputFieldId) =>
        (model, FileReader.readImage(inputFieldId, resultToMessage))

      case BumpToNormalMsg.LoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.LoadSucceeded(file) =>
        val cmds = Cmd.batch(
          Logger.info("loaded image", file.name),
          ImageLoader.load(file.path, resultToMessage2)
        )

        (model, cmds)

      case BumpToNormalMsg.ImageLoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.ImageLoadSucceeded(img) =>
        val canvas = document.getElementById("bump-to-normal-canvas").asInstanceOf[html.Canvas]
        canvas.width = img.width
        canvas.height = img.height

        val ctx = canvas.getContext("2d")
        ctx.drawImage(img, 0, 0)
        // TODO Next: https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial/Pixel_manipulation_with_canvas

        (model, Logger.info("loaded the image!"))

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

  def resultToMessage2: Either[ImageLoader.Error, html.Image] => BumpToNormalMsg = {
    case Left(e) =>
      BumpToNormalMsg.ImageLoadFailed(e.message)

    case Right(img) =>
      BumpToNormalMsg.ImageLoadSucceeded(img)
  }
