package tools

import tyrian.Html
import tyrian.Html._

object TitleBar:
  def view(gistPath: String): Html[Msg] =
    div(`class`("full-width-container p-0"), style("filter", "drop-shadow(0px 4px 4px #00000055)"))(
      div(`class`("full-width-container"), style("background-color", "#29016A"))(
        div(_class("row"))(
          div(_class("col"))(
            img(src("images/indigo_logo_solid_text.svg"), height("40px"))
          ),
          div(_class("col"), style("text-align", "right"))(
            div(`class`("container g-0"))(
              // form(_class("row g-0"))(
              //   div(_class("col"))(
              //     input(
              //       _class("form-control"),
              //       _type("text"),
              //       placeholder("Enter a gist path, e.g. <github-user>/<gist-id>"),
              //       value(gistPath),
              //       onInput(newPath => Msg.UpdatePath(newPath))
              //     )
              //   ),
              //   div(_class("col-md-auto"))(
              //     button(_type("button"), _class("btn btn-secondary"), onClick(Msg.LoadGist(gistPath)))(text("load gist"))
              //   )
              // )
            )
          )
        )
      )
    )
