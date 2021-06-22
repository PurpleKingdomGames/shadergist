package indigotoy

import indigo._
import indigo.ShaderPrimitive._

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object IndigoToy extends IndigoDemo[Size, Size, Unit, Size]:

  val eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Size]] =
    val gameViewport =
      (flags.get("width"), flags.get("height")) match {
        case (Some(w), Some(h)) =>
          GameViewport(w.toInt, h.toInt)

        case _ =>
          GameViewport(320, 200)
      }

    val maybeGist = flags.get("gist")
    val assetName = AssetName("shader gist")
    val assets: Set[AssetType] = maybeGist match
      case Some(p) =>
        Set(AssetType.Text(assetName, AssetPath(s"https://gist.githubusercontent.com/$p/raw")))
      case None =>
        Set()

    Outcome(
      BootResult(
        GameConfig.default
          .withViewport(gameViewport),
        gameViewport.size
      )
        .withAssets(assets)
        .withShaders(ToyEntity.shader(assetName))
    )

  def setup(bootData: Size, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Size]] =
    Outcome(Startup.Success(bootData))

  def initialModel(startupData: Size): Outcome[Unit] =
    Outcome(())

  def initialViewModel(startupData: Size, model: Unit): Outcome[Size] =
    Outcome(startupData)

  def updateModel(context: FrameContext[Size], model: Unit): GlobalEvent => Outcome[Unit] =
    _ => Outcome(model)

  def updateViewModel(context: FrameContext[Size], model: Unit, viewModel: Size): GlobalEvent => Outcome[Size] =
    case indigo.shared.events.ViewportResize(newSize) =>
      Outcome(newSize.size)
    case _ =>
      Outcome(viewModel)

  def present(context: FrameContext[Size], model: Unit, viewModel: Size): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        ToyEntity(viewModel)
      )
    )

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
