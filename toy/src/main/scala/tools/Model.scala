package tools

import tools.cmds.FileReader

import org.scalajs.dom.html

final case class Model(page: SitePage):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

object Model:
  def initial(pathName: String): Model =
    Model(SitePage.fromPathName(pathName))

enum SitePage:
  case Home

  def slug: String = SitePage.toPathName(this)
  def label: String = SitePage.toLabel(this)

object SitePage:
  def toLabel(sitePage: SitePage): String =
    sitePage match
      case SitePage.Home               => "home"

  def toPathName(sitePage: SitePage): String =
    sitePage match
      case SitePage.Home               => "/"

  def fromPathName(pathName: String): SitePage =
    pathName match
      // case "#/gist/1234"   => SitePage.???
      case _                   => SitePage.Home
