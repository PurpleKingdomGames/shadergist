package tools.pages

import tyrian.Html
import tyrian.Html._

import tools.Msg

object FourLightsToNormal:
  def view: Html[Msg] =
    div()(
      text("fours lights to normal")
    )
