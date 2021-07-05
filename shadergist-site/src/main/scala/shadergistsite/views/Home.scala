package shadergistsite.views

import tyrian.Html
import tyrian.Html._

import shadergistsite.Msg

object Home:
  def view(gistPath: String, gistCode: Option[String]): Html[Msg] =
    gistCode match
      case None =>
        div(_class("container"))()

      case Some(gist) =>
        div(_class("container"))(
          div(_class("row"))(
            div(_class("col-md-auto"))(
              div(id("indigo-container"))(),
              about
            ),
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

  def about: Html[Msg] =
    div(styles("width" -> "400px"))(
      br,
      h1(text("ShaderGist")),
      p(
        text("ShaderGist allows you to render "),
        a(href("https://indigoengine.io/"), target("_blank"), styles("color" -> "#DDBB00"))(text("Indigo")),
        text(" flavoured GLSL shaders, saved as public GitHub gists.")
      ),
      p(
        text("Sharing them is as simple as copying a link, try this one:"),
        br,
        br,
        a(
          href("?gist=davesmith00000/5a6b585dbc2184a2c215b16a91234c22"),
          styles("color" -> "#DDBB00")
        )(text("Green Fire Shader!"))
      )
    )
