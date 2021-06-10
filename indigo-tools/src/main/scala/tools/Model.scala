package tools

final case class Model(page: SitePage):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

object Model:
  val initial: Model =
    Model(SitePage.Home)

enum SitePage:
  case Home, BumpToNormal, FourLightsToNormal, BitmapFont
