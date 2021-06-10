package tools

import tyrian.{Html, Tyrian, Sub, Cmd}
import tyrian.Html._

object TitleBar:
  def view[Msg]: Html[Msg] =
    div(`class`("full-width-container p-0"), style("filter", "drop-shadow(0px 4px 4px #00000055)"))(
      div(`class`("full-width-container"), style("background-color", "#29016A"))(
        img(src("images/indigo_logo_solid_text.svg"), height("40px"))
      )
    )
