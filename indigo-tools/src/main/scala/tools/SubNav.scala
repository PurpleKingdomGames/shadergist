package tools

import tyrian.Html
import tyrian.Html._

object SubNav:
  def view: Html[Msg] =
    ol()(
      li()(a(href("#"), onClick(Msg.NavigateTo(SitePage.Home)))(text("home"))),
      li()(a(href("#"), onClick(Msg.NavigateTo(SitePage.BumpToNormal)))(text("bump to normal"))),
      li()(a(href("#"), onClick(Msg.NavigateTo(SitePage.FourLightsToNormal)))(text("four lights to normal"))),
      li()(a(href("#"), onClick(Msg.NavigateTo(SitePage.BitmapFont)))(text("font"))),
    )
