package tools.pages

import tyrian.Html
import tyrian.Html._
import tools.Msg
import tools.Styles

object Home:
  def view(gistPath: String, gistCode: Option[String]): Html[Msg] =
    div(_class("container"))(
      div(_class("row"))(
        div(id("indigo-container"), _class("col-md-auto"))(),
        div(_class("col"), styles("overflow" -> "scroll", "font-size" -> "0.75em"))(
          pre()(
            code(_class("language-glsl"))(
              gistCode.map(c => List(text(c))).getOrElse(Nil)
            )
          ),
          script(_type("text/javascript"))(
            gistCode match
              case None =>
                text("")

              case Some(_) =>
                text(
                  s"""window.Prism.highlightAll();"""
                )
          )
        )
      ),
      script(_type("text/javascript"), defer)(
        text(
          s"""window.indigo.IndigoGame.launch({"width": "400", "height": "400", "gist": "$gistPath"});"""
        )
      )
    )
