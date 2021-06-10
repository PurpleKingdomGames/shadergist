package tools

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._

import pages._

import org.scalajs.dom.document

object Main:

  def init: (Model, Cmd[Msg]) =
    (Model.initial, Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case Msg.NavigateTo(page) => (model.navigateTo(page), Cmd.Empty)

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view,
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        SubNav.view,
        model.page match
          case SitePage.Home =>
            Home.view

          case SitePage.BumpToNormal =>
            BumpToNormal.view

          case SitePage.FourLightsToNormal =>
            FourLightsToNormal.view

          case SitePage.BitmapFont =>
            BitmapFonts.view
      )
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty

  def main(args: Array[String]): Unit =
    Tyrian.start(document.getElementById("myapp"), init, update, view, subscriptions)
