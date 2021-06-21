package tools

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._

import pages._

import org.scalajs.dom.document
import org.scalajs.dom.window
import tools.Msg.BumpToNormalMsg

object Main:

  def init: (Model, Cmd[Msg]) =
    val m = Model.initial(window.location.hash)

    (m, changePageCommand(m.page))

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case Msg.NavigateTo(page) =>
        (model.navigateTo(page), changePageCommand(page))

      case e: Msg.BumpToNormalMsg =>
        val (m, cmd) = BumpToNormal.update(e, model.bumpToNormal)
        (model.copy(bumpToNormal = m), cmd)

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view,
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        SubNav.view,
        model.page match
          case SitePage.Home =>
            Home.view

          case SitePage.BumpToNormal =>
            BumpToNormal.view(model.bumpToNormal)

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

  def changePageCommand(page: SitePage): Cmd[Msg] =
    page match
      case SitePage.BumpToNormal => Cmd.Emit(BumpToNormalMsg.FirstLoad)
      case _                     => Cmd.Empty