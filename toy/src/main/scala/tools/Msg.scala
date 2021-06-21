package tools

import org.scalajs.dom.html
import tools.cmds.FileReader

enum Msg:
  case NavigateTo(page: SitePage) extends Msg
  case UpdatePath(path: String)   extends Msg
  case LoadGist(gistPath: String) extends Msg
  case LoadedGist(body: String)   extends Msg
  case LoadGistError(msg: String) extends Msg
