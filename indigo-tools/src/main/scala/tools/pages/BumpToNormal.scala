package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg

import org.scalajs.dom
import org.scalajs.dom.raw
import org.scalajs.dom.html
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event

import scala.scalajs.js

object BumpToNormal:

  type Model = Option[ImgData]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FileSelected(fieldName) =>
        (model, grabFile(fieldName))

      case BumpToNormalMsg.LoadFailed(msg) =>
        println(msg)
        (model, Cmd.Empty)

      case BumpToNormalMsg.LoadSucceeded(imgData) =>
        (Some(imgData), Cmd.Empty)

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
        )
      ) ++ {
        model match
          case None                   => Seq()
          case Some(ImgData(path, _)) => Seq(img(src(path)))
      }

    div()(children: _*)

  def grabFile(fieldName: String): Cmd[BumpToNormalMsg] =
    val files = document.getElementById(fieldName).asInstanceOf[html.Input].files
    if files.length > 0 then readFile(files.item(0)) else Cmd.Empty

  def readFile(file: raw.File): Cmd[BumpToNormalMsg] =
    Task
      .RunObservable[String, ImgData] { observer =>
        val fileReader = new dom.FileReader()
        fileReader.readAsDataURL(file)
        fileReader.onload = (e: Event) => {
          observer.onNext(
            ImgData(
              path = e.target.asInstanceOf[js.Dynamic].result.asInstanceOf[String],
              data = fileReader.result.asInstanceOf[html.Image]
            )
          )
        }
        fileReader.onerror = _ => observer.onError("Boom")

        () => ()
      }
      .attempt {
        case Left(msg) =>
          BumpToNormalMsg.LoadFailed(msg)

        case Right(imgData) =>
          BumpToNormalMsg.LoadSucceeded(imgData)
      }

final case class ImgData(path: String, data: html.Image)
