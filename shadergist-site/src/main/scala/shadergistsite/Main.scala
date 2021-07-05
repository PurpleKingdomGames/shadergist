package shadergistsite

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._
import tyrian.http._

import views._

import org.scalajs.dom.document
import org.scalajs.dom.window
import scala.scalajs.js

import tyrian.cmds.Logger

object Main:

  // This is the UV gist, simple but not as harsh to look at as the solid green one...
  val defaultGist = "davesmith00000/5bed05e26ed97a3fe37d9e0762a77d14"

  def init: (Model, Cmd[Msg]) =
    val m = Model.initial(window.location.search, defaultGist)

    val gistCmd =
      if m.gistPath.isEmpty then Cmd.Emit(Msg.LoadGist(defaultGist))
      else Cmd.Emit(Msg.LoadGist(m.gistPath))

    (m, gistCmd)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case Msg.LoadGist(gistPath) =>
        (
          model,
          Http.send(
            Request.get(s"https://gist.githubusercontent.com/$gistPath/raw"),
            {
              case Right(b) => Msg.LoadedGist(b)
              case Left(e)  => Msg.LoadGistError(e.toString)
            }
          )
        )

      case Msg.UpdatePath(path) =>
        (model.updateGistPath(path), Cmd.Empty)

      case Msg.LoadGistError(e) =>
        (model, Logger.error(e))

      case Msg.LoadedGist(code) =>
        (model.withCode(code), Cmd.Batch(Logger.info("loaded code!"), CustomCmds.highlightAll))

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view(model.gistPath),
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        Home.view(model.gistPath, model.code)
      )
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty

  def main(args: Array[String]): Unit =
    Tyrian.start(document.getElementById("shadergistapp"), init, update, view, subscriptions)

object CustomCmds:

  val highlightAll: Cmd.SideEffect =
    Cmd.SideEffect { () =>
      js.Dynamic.global.window.Prism.highlightAll()
      ()
    }
