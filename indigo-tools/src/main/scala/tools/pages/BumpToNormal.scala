package tools.pages

import tyrian.Html
import tyrian.Html._

import tools.Msg

object BumpToNormal:
  def view: Html[Msg] =
    div()(
      text("bump to norml")
    )
