package tools.pages

import tyrian.Html
import tyrian.Html._
import tools.Msg
import tools.Styles

object Home:
  def view: Html[Msg] =
    div(
      // img(
      //   src("images/indigo_logo_full.svg"),
      //   height("300px"),
      //   `class`("fade-in-image"),
      //   styles(Styles.centerImage)
      // ),
      div(id("indigo-container"))(),
      script(_type("text/javascript"), src("gamestart.js"))()
    )
