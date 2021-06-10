package tools

import tools.pages.BumpToNormal
import tools.pages.ImgData
import tools.Msg.BumpToNormalMsg

import org.scalajs.dom.html

sealed trait Msg
object Msg:
  final case class NavigateTo(page: SitePage) extends Msg

  enum BumpToNormalMsg extends Msg:
    case FileSelected(fileName: String) extends BumpToNormalMsg
    case LoadFailed(message: String) extends BumpToNormalMsg
    case LoadSucceeded(image: ImgData) extends BumpToNormalMsg
