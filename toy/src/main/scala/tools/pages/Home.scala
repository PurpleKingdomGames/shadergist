package tools.pages

import tyrian.Html
import tyrian.Html._
import tools.Msg
import tools.Styles

object Home:
  def view(gistPath: String, code: Option[String]): Html[Msg] =
    div(
      // img(
      //   src("images/indigo_logo_full.svg"),
      //   height("300px"),
      //   `class`("fade-in-image"),
      //   styles(Styles.centerImage)
      // ),
      div(id("indigo-container"))(),
      script(_type("text/javascript"), defer)(
        text(
          """window.indigo.IndigoGame.launch({"width": window.innerWidth.toString(), "height": window.innerHeight.toString()});"""
        )
      ),
      div(
        input(_type("text"), placeholder("davesmith00000/43ee912e19f60b0dc1fde9905cbff832"), value(gistPath)),
        button(_type("button"), onClick(Msg.LoadGist(gistPath)))(text("reload"))
      ),
      div(code.map(c => List(text(c))).getOrElse(Nil))
    )
