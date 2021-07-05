package shadergistsite

import org.scalajs.dom.html

enum Msg:
  case UpdatePath(path: String)   extends Msg
  case LoadGist(gistPath: String) extends Msg
  case LoadedGist(body: String)   extends Msg
  case LoadGistError(msg: String) extends Msg
