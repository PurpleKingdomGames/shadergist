package tools

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._
import tyrian.http._

import pages._

import org.scalajs.dom.document
import org.scalajs.dom.window
import tools.cmds.Logger

object Main:

  def init: (Model, Cmd[Msg]) =
    val m = Model.initial(window.location.hash)

    (m, Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case Msg.NavigateTo(page) =>
        (model.navigateTo(page), Cmd.Empty)

      case Msg.UpdatePath(path) =>
        (model.updateGistPath(path), Cmd.Empty)

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

      case Msg.LoadGistError(e) =>
        (model, Logger.error(e))

      case Msg.LoadedGist(code) =>
        (model.withCode(code), Logger.info("loaded code!"))

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view,
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        model.page match
          case SitePage.Home =>
            Home.view(model.gistPath, model.code)
      )
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty

  def main(args: Array[String]): Unit =
    Tyrian.start(document.getElementById("indigotoyapp"), init, update, view, subscriptions)
