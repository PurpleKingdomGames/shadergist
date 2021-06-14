package tools

import tools.pages.BumpToNormal
import tools.Msg.BumpToNormalMsg

import org.scalajs.dom.html
import tools.cmds.FileReader

sealed trait Msg
object Msg:
  final case class NavigateTo(page: SitePage) extends Msg

  enum BumpToNormalMsg extends Msg:
    case FileSelected(inputFieldId: String) extends BumpToNormalMsg
    case LoadFailed(message: String) extends BumpToNormalMsg
    case LoadSucceeded(image: FileReader.File[html.Image]) extends BumpToNormalMsg
    case ImageLoadFailed(message: String) extends BumpToNormalMsg
    case ImageLoadSucceeded(image: html.Image) extends BumpToNormalMsg
