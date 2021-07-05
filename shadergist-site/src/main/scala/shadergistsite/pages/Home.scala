package shadergistsite.pages

import tyrian.Html
import tyrian.Html._
import shadergistsite.Msg
import shadergistsite.Styles

object Home:
  def view(gistPath: String, gistCode: Option[String]): Html[Msg] =
    gistCode match
      case None =>
        div(_class("container"))()

      case Some(gist) =>
        div(_class("container"))(
          div(_class("row"))(
            div(id("indigo-container"), _class("col-md-auto"))(),
            div(_class("col"), styles("overflow" -> "hidden", "font-size" -> "0.75em"))(
              pre(styles("margin" -> "0px"))(
                code(_class("language-glsl"))(
                  text(gist)
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
