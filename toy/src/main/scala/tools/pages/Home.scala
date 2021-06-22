package tools.pages

import tyrian.Html
import tyrian.Html._
import tools.Msg
import tools.Styles

object Home:
  def view(gistPath: String, gistCode: Option[String]): Html[Msg] =
    div(_class("container"))(
      div(_class("row"))(
        div(id("indigo-container"), _class("col"))(),
        div(_class("col"))(
          pre(
            code(id("foo"), _class("language-glsl"))(
              gistCode.map(c => List(text(c))).getOrElse(Nil)
            )
          )
        )
      ),
      // div(_class("row"))(
      //   div(_class("col"))(
      //     input(_type("text"), placeholder("davesmith00000/43ee912e19f60b0dc1fde9905cbff832"), value(gistPath)),
      //     button(_type("button"), onClick(Msg.LoadGist(gistPath)))(text("reload"))
      //   )
      // ),
      script(_type("text/javascript"), defer)(
        text(
          s"""window.indigo.IndigoGame.launch({"width": "550", "height": "400", "gist": "$gistPath"});"""
        )
      )
    )
