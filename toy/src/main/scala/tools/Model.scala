package tools

import tools.cmds.FileReader

import org.scalajs.dom.html

final case class Model(
    gistPath: String,
    code: Option[String]
):
  def withCode(newCode: String): Model =
    this.copy(code = Option(newCode))

  def updateGistPath(newPath: String): Model =
    this.copy(gistPath = newPath)

object Model:
  def initial(search: String): Model =
    search match
      case p if p.startsWith("?") =>
        val params   = p.substring(1).split("&").toList
        val gistPath = params.find(_.startsWith("gist=")).map(_.substring(5)).getOrElse("")

        Model(gistPath, None)

      case _ =>
        Model("", None)
