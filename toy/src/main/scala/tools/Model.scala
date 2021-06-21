package tools

import tools.cmds.FileReader

import org.scalajs.dom.html

final case class Model(page: SitePage, gistPath: String, code: Option[String]):
  def navigateTo(newPage: SitePage): Model =
    this.copy(page = newPage)

  def withCode(newCode: String): Model =
    this.copy(code = Option(newCode))

  def updateGistPath(newPath: String): Model =
    this.copy(gistPath = newPath)

object Model:
  def initial(pathName: String): Model =
    Model(SitePage.fromPathName(pathName), "davesmith00000/43ee912e19f60b0dc1fde9905cbff832", None)

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
