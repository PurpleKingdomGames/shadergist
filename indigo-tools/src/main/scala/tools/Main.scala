package tools

import tyrian.{Html, Tyrian, Sub, Cmd, Style}
import tyrian.Html._
import org.scalajs.dom.document

object Main:
  type Msg          = NavigateTo
  opaque type Model = SitePage

  def init: (Model, Cmd[Msg]) =
    (SitePage.Home, Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case NavigateTo(page) => (page, Cmd.Empty)

  def view(model: Model): Html[Msg] =
    div(`class`("full-width-container p-0"))(
      TitleBar.view,
      div(`class`("full-width-container"), style("padding-top", "40px"))(
        a(href("#"), onClick(NavigateTo(SitePage.Home)))(text("home")),
        text(" "),
        a(href("#"), onClick(NavigateTo(SitePage.Bump2Normal)))(text("bump")),
        text(" "),
        a(href("#"), onClick(NavigateTo(SitePage.BitmapFont)))(text("font")),
        text(" "),
        model match
          case SitePage.Home =>
            img(
              src("images/indigo_logo_full.svg"),
              height("300px"),
              `class`("fade-in-image"),
              styles(Styles.centerImage)
            )

          case SitePage.Bump2Normal =>
            text("bump to norml")

          case SitePage.BitmapFont =>
            text("bitmap text")
      )
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty

  def main(args: Array[String]): Unit =
    Tyrian.start(document.getElementById("myapp"), init, update, view, subscriptions)

enum SitePage:
  case Home, Bump2Normal, BitmapFont

final case class NavigateTo(page: SitePage)

object Styles:
  val centerImage: Style =
    Style(
      "display"      -> "block",
      "margin-left"  -> "auto",
      "margin-right" -> "auto",
      "width"        -> "50%"
    )
