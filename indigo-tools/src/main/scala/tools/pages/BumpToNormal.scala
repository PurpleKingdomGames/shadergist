package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg
import tools.cmds.FileReader

import org.scalajs.dom.html

object BumpToNormal:

  type Model = Option[FileReader.File[html.Image]]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FileSelected(inputFieldId) =>
        (model, FileReader.readImage(inputFieldId, resultToMessage))

      case BumpToNormalMsg.LoadFailed(msg) =>
        println(msg)
        (model, Cmd.Empty)

      case BumpToNormalMsg.LoadSucceeded(file) =>
        (Some(file), Cmd.Empty)

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
          case Some(FileReader.File(path, _)) => Seq(img(src(path)))
      }

    div()(children: _*)

  def resultToMessage: Either[FileReader.Error, FileReader.File[html.Image]] => BumpToNormalMsg = {
    case Left(e) =>
      BumpToNormalMsg.LoadFailed(e.message)

    case Right(file) =>
      BumpToNormalMsg.LoadSucceeded(file)
  }
