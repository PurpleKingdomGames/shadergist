package tools

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._

import pages._

import org.scalajs.dom.document
import org.scalajs.dom.window

object Main:

  def init: (Model, Cmd[Msg]) =
    val m = Model.initial(window.location.hash)

    (m, Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case Msg.NavigateTo(page) =>
        (model.navigateTo(page), Cmd.Empty)

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view,
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        model.page match
          case SitePage.Home =>
            Home.view
      )
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty

  def main(args: Array[String]): Unit =
    Tyrian.start(document.getElementById("indigotoyapp"), init, update, view, subscriptions)

