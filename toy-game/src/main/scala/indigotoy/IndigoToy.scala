package indigotoy

import indigo._
import indigo.ShaderPrimitive._

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object IndigoToy extends IndigoDemo[InitialData, InitialData, Model, Size]:

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

    val maybeGist = flags.get("gist").flatMap(s => if s.isEmpty then None else Some(s))
    val assetName = AssetName("shader gist")
    // val assets: Set[AssetType] = maybeGist match
    //   case Some(p) =>
    //     Set(AssetType.Text(assetName, AssetPath(s"https://gist.githubusercontent.com/$p/raw")))
    //   case None =>
    //     Set()

    Outcome(
      BootResult(
        GameConfig.default
          .withViewport(gameViewport),
        InitialData(gameViewport.size, maybeGist)
      )
      // .withAssets(assets)
      // .withShaders(ToyEntity.shader(assetName))
    )

  def setup(bootData: InitialData, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[InitialData]] =
    Outcome(Startup.Success(bootData))

  def initialModel(startupData: InitialData): Outcome[Model] =
    startupData.maybeGist match
      case None =>
        Outcome(Model.NoGist)
      case Some(gist) =>
        Outcome(Model.Loading)
          .addGlobalEvents(HttpRequest.GET(s"https://gist.githubusercontent.com/$gist/raw"))

  def initialViewModel(startupData: InitialData, model: Model): Outcome[Size] =
    Outcome(startupData.screenSize)

  def updateModel(context: FrameContext[InitialData], model: Model): GlobalEvent => Outcome[Model] =
    case HttpResponse(200, _, code) =>
      Outcome(Model.Ready(code.getOrElse("No code!")))

    case HttpResponse(status, _, _) =>
      Outcome(Model.LoadingError(status, context.startUpData.maybeGist.getOrElse("missing")))

    case _ =>
      Outcome(model)

  def updateViewModel(context: FrameContext[InitialData], model: Model, viewModel: Size): GlobalEvent => Outcome[Size] =
    case indigo.shared.events.ViewportResize(newSize) =>
      Outcome(newSize.size)
    case _ =>
      Outcome(viewModel)

  def present(context: FrameContext[InitialData], model: Model, viewModel: Size): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        model match
          case Model.NoGist =>
            TextBox("No gist to load.", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.White)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.Loading =>
            TextBox("Loading gist...", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.White)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.LoadingError(status, path) =>
            TextBox(s"Error loading gist from '$path', got ${status.toString}", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.White)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.Ready(code) =>
            // ToyEntity(viewModel)
            TextBox(s"Ready got: ${code.take(30)} (..)", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.White)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)
      )
    )

final case class InitialData(screenSize: Size, maybeGist: Option[String])

enum Model derives CanEqual:
  case NoGist                                  extends Model
  case Loading                                 extends Model
  case LoadingError(status: Int, path: String) extends Model
  case Ready(code: String)                     extends Model

final case class ToyEntity(size: Size) extends EntityNode:
  val position: Point   = Point.zero
  val depth: Depth      = Depth(1)
  val flip: Flip        = Flip.default
  val ref: Point        = Point.zero
  val rotation: Radians = Radians.zero
  val scale: Vector2    = Vector2.one

  def withDepth(newDepth: Depth): ToyEntity = this

  def toShaderData: ShaderData =
    ShaderData(ToyEntity.shaderId)

object ToyEntity:
  val shaderId: ShaderId =
    ShaderId("toy shader")

  def shader(fragProgram: AssetName): EntityShader =
    EntityShader
      .External(shaderId)
      .withFragmentProgram(fragProgram)
