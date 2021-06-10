package tools

import tools.pages.ImgData

final case class Model(page: SitePage, bumpToNormal: Option[ImgData]):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

object Model:
  val initial: Model =
    Model(SitePage.Home, None)

enum SitePage:
  case Home, BumpToNormal, FourLightsToNormal, BitmapFont
