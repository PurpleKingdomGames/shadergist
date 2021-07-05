package shadergistgame

import indigo._

final case class GistEntity(size: Size) extends EntityNode:
  val position: Point   = Point.zero
  val depth: Depth      = Depth(1)
  val flip: Flip        = Flip.default
  val ref: Point        = Point.zero
  val rotation: Radians = Radians.zero
  val scale: Vector2    = Vector2.one

  def withDepth(newDepth: Depth): GistEntity = this

  def toShaderData: ShaderData =
    ShaderData(GistEntity.shaderId)

object GistEntity:
  val shaderId: ShaderId =
    ShaderId("gist shader")

  def shader(fragProgram: AssetName): EntityShader =
    EntityShader
      .External(shaderId)
      .withFragmentProgram(fragProgram)
