package shadergistsite

import org.scalajs.dom.html
import shadergistsite.cmds.FileReader

enum Msg:
  case UpdatePath(path: String)   extends Msg
  case LoadGist(gistPath: String) extends Msg
  case LoadedGist(body: String)   extends Msg
  case LoadGistError(msg: String) extends Msg
