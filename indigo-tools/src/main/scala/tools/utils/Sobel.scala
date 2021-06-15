package tools.utils

/** Sobel is a type of convolution matrix used for edge detection
 */
object Sobel:

  val kernelX: List[Double] =
    List(-1, 0, 1, -2, 0, 2, -1, 0, 1)

  val kernelY: List[Double] =
    List(1, 2, 1, 0, 0, 0, -1, -2, -1)
  
  def samplesGrid(center: Int, totalCount: Int, width: Int, multiplier: Int): ConvolutionSamples =
    val maxRows = Math.floor(((totalCount / multiplier) - 1) / width)

    val x = (center / multiplier) % width
    val y = Math.floor(center / multiplier / width)

    val l = x - 1
    val r = x + 1
    val a = y - 1
    val b = y + 1

    val left       = if l < 0 then None else Some(center - multiplier)
    val right      = if r > width - 1 then None else Some(center + multiplier)
    val aboveLeft  = if a < 0 || l < 0 then None else Some(center - (width * multiplier))
    val above      = if a < 0 then None else Some(center - (width * multiplier))
    val aboveRight = if a < 0 || r > width - 1 then None else Some(center - (width * multiplier))
    val belowLeft  = if b > maxRows || l < 0 then None else Some(center + (width * multiplier))
    val below      = if b > maxRows then None else Some(center + (width * multiplier))
    val belowRight = if b > maxRows || r > width - 1 then None else Some(center + (width * multiplier))

    ConvolutionSamples(
      tl = aboveLeft,
      tc = above,
      tr = aboveRight,
      ml = left,
      mc = center,
      mr = right,
      bl = belowLeft,
      bc = below,
      br = belowRight
    )

final case class ConvolutionSamples(
    tl: Option[Int],
    tc: Option[Int],
    tr: Option[Int],
    ml: Option[Int],
    mc: Int,
    mr: Option[Int],
    bl: Option[Int],
    bc: Option[Int],
    br: Option[Int]
):
  def toList: List[Option[Int]] =
    List(tl, tc, tr, ml, Some(mc), mr, bl, bc, br)

