package tools

import tools.cmds.FileReader

import org.scalajs.dom.html

final case class Model(page: SitePage, bumpToNormal: Option[FileReader.File[html.Image]]):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

object Model:
  val initial: Model =
    Model(SitePage.Home, None)

enum SitePage:
  case Home, BumpToNormal, FourLightsToNormal, BitmapFont
