package shadergistgame

import indigo._
import indigo.ShaderPrimitive._

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object ShaderGistGame extends IndigoDemo[InitialData, InitialData, Model, Size]:

  val eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[InitialData]] =
    val gameViewport =
      (flags.get("width"), flags.get("height")) match {
        case (Some(w), Some(h)) =>
          GameViewport(w.toInt, h.toInt)

        case _ =>
          GameViewport(320, 200)
      }

    val maybeGistPath = flags.get("gist").flatMap(s => if s.isEmpty then None else Some(s))

    Outcome(
      BootResult(
        GameConfig.default
          .withViewport(gameViewport),
        InitialData(gameViewport.size, maybeGistPath)
      )
    )

  val shaderAssetName: AssetName = AssetName("shader gist")

  def setup(bootData: InitialData, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[InitialData]] =
    assetCollection.findTextDataByName(shaderAssetName) match
      case None =>
        Outcome(Startup.Success(bootData))

      case Some(code) if code.contains("//<indigo-fragment>") && code.contains("//</indigo-fragment>") =>
        Outcome(
          Startup
            .Success(bootData)
            .addShaders(GistEntity.shader(shaderAssetName))
        ).addGlobalEvents(ShaderAdded)

      case Some(_) =>
        Outcome(
          Startup
            .Success(bootData)
        ).addGlobalEvents(ShaderInvalid)

  def initialModel(startupData: InitialData): Outcome[Model] =
    startupData.maybeGistPath match
      case None =>
        Outcome(Model.NoGist)

      case Some(gistPath) =>
        Outcome(Model.Loading)
          .addGlobalEvents(
            LoadAsset(
              AssetType
                .Text(shaderAssetName, AssetPath(s"https://gist.githubusercontent.com/$gistPath/raw")),
              BindingKey("Remote shader load"),
              true
            )
          )

  def initialViewModel(startupData: InitialData, model: Model): Outcome[Size] =
    Outcome(startupData.screenSize)

  def updateModel(context: FrameContext[InitialData], model: Model): GlobalEvent => Outcome[Model] =
    case ShaderAdded =>
      Outcome(Model.Ready)

    case ShaderInvalid =>
      Outcome(Model.Failed)

    case _ =>
      Outcome(model)

  def updateViewModel(context: FrameContext[InitialData], model: Model, viewModel: Size): GlobalEvent => Outcome[Size] =
    case indigo.shared.events.ViewportResize(newSize) =>
      Outcome(newSize.size)
    case _ =>
      Outcome(viewModel)

  def present(context: FrameContext[InitialData], model: Model, viewModel: Size): Outcome[SceneUpdateFragment] =
    View.present(context, model, viewModel)

final case class InitialData(screenSize: Size, maybeGistPath: Option[String])

enum Model derives CanEqual:
  case NoGist  extends Model
  case Loading extends Model
  case Failed  extends Model
  case Ready   extends Model

case object ShaderAdded   extends GlobalEvent
case object ShaderInvalid extends GlobalEvent
