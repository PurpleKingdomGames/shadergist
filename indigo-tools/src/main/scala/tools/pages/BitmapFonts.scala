package tools.pages

import tyrian.Html
import tyrian.Html._

import tools.Msg

object BitmapFonts:
  def view: Html[Msg] =
    div()(
      text("bitmap fonts")
    )
