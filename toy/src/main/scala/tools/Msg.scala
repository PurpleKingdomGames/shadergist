package tools

import org.scalajs.dom.html
import tools.cmds.FileReader

sealed trait Msg
object Msg:
  final case class NavigateTo(page: SitePage) extends Msg
