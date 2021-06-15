package tools

import tyrian.Html
import tyrian.Html._

object SubNav:
  private val nav: List[Html[Msg]] =
    List(
      SitePage.Home,
      SitePage.BumpToNormal,
      SitePage.FourLightsToNormal,
      SitePage.BitmapFont
    ).map(makeItem)

  def view: Html[Msg] =
    ol()(nav: _*)

  private def makeItem(page: SitePage): Html[Msg] =
    li()(a(href(page.slug), onClick(Msg.NavigateTo(page)))(text(page.label)))
