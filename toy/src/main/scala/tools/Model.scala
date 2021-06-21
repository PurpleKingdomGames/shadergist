package tools

import tools.cmds.FileReader

import org.scalajs.dom.html

final case class Model(page: SitePage, bumpToNormal: Option[FileReader.File[html.Image]]):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

object Model:
  def initial(pathName: String): Model =
    Model(SitePage.fromPathName(pathName), None)

enum SitePage:
  case Home, BumpToNormal, FourLightsToNormal, BitmapFont

  def slug: String = SitePage.toPathName(this)
  def label: String = SitePage.toLabel(this)

object SitePage:
  def toLabel(sitePage: SitePage): String =
    sitePage match
      case SitePage.Home               => "home"
      case SitePage.BumpToNormal       => "bump to normal"
      case SitePage.FourLightsToNormal => "four lights to normal"
      case SitePage.BitmapFont         => "font"

  def toPathName(sitePage: SitePage): String =
    sitePage match
      case SitePage.Home               => "/"
      case SitePage.BumpToNormal       => "/#/bump-to-normal"
      case SitePage.FourLightsToNormal => "/#/four-lights"
      case SitePage.BitmapFont         => "/#/bitmap-fonts"

  def fromPathName(pathName: String): SitePage =
    pathName match
      case "#/bump-to-normal" => SitePage.BumpToNormal
      case "#/four-lights"    => SitePage.FourLightsToNormal
      case "#/bitmap-fonts"   => SitePage.BitmapFont
      case _                   => SitePage.Home
