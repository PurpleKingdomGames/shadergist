package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg
import tools.cmds.FileReader
import tools.cmds.ImageLoader
import tools.cmds.Logger

import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js

object BumpToNormal:

  type Model = Option[FileReader.File[html.Image]]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FileSelected(inputFieldId) =>
        (model, FileReader.readImage(inputFieldId, resultToMessage))

      case BumpToNormalMsg.LoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.LoadSucceeded(file) =>
        val cmds = Cmd.batch(
          Logger.info("loaded image", file.name),
          ImageLoader.load(file.path, resultToMessage2)
        )

        (Some(file), cmds)

      case BumpToNormalMsg.ImageLoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.ImageLoadSucceeded(img) =>
        val canvas = document.getElementById("bump-to-normal-canvas").asInstanceOf[html.Canvas]
        canvas.width = img.width
        canvas.height = img.height

        val ctx = canvas.getContext("2d")
        ctx.drawImage(img, 0, 0)

        ctx.drawImage(img, 0, 0)
        val imageData = ctx.getImageData(0, 0, canvas.width, canvas.height)
        val data      = imageData.data.asInstanceOf[js.Array[Double]]

        // Convert to greyscale
        var i = 0
        while i < data.length do
          var avg = (data(i) + data(i + 1) + data(i + 2)) / 3
          data(i) = avg     // red
          data(i + 1) = avg // green
          data(i + 2) = avg // blue
          i += 4

        // Convert to normal - Sobel method
        i = 0

        val readFromData = data ++ new js.Array[Double]()

        val redKernel: List[Double] =
          List(-1, 0, 1, -2, 0, 2, -1, 0, 1)
        val greenKernel: List[Double] =
          List(1, 2, 1, 0, 0, 0, -1, -2, -1)

        while i < readFromData.length do
          val samples = samplesGrid(i, readFromData.length, img.width, 4).toList

          val red =
            samples
              .zip(redKernel)
              .map {
                case (Some(i), k) =>
                  readFromData(i).toDouble * k

                case (None, i) =>
                  0.0d
              }
              .sum

          val green =
            samples
              .zip(greenKernel)
              .map {
                case (Some(i), k) =>
                  readFromData(i).toDouble * k

                case (None, i) =>
                  0.0d
              }
              .sum

          val blue = 255

          data(i) = 128 + red       // rGamma
          data(i + 1) = 128 + green // gGamma
          data(i + 2) = blue
          i += 4

        ctx.putImageData(imageData, 0, 0)

        (model, Logger.info("loaded the image!"))

  def view(model: Model): Html[BumpToNormalMsg] =
    val children: Seq[Html[BumpToNormalMsg]] =
      Seq(
        h1()(text("bump to normal")),
        br,
        input(
          `type`("file"),
          id("image-upload"),
          name("image upload"),
          accept("image/png, image/jpeg, image/gif"),
          onChange(BumpToNormalMsg.FileSelected("image-upload"))
        )
      )
        ++ {
          model match
            case None                              => Seq()
            case Some(FileReader.File(_, path, _)) => Seq(img(src(path)))
        }
        ++ Seq(canvas(id("bump-to-normal-canvas"), width("150"), height("150"))())

    div()(children: _*)

  def resultToMessage: Either[FileReader.Error, FileReader.File[html.Image]] => BumpToNormalMsg = {
    case Left(e) =>
      BumpToNormalMsg.LoadFailed(e.message)

    case Right(file) =>
      BumpToNormalMsg.LoadSucceeded(file)
  }

  def resultToMessage2: Either[ImageLoader.Error, html.Image] => BumpToNormalMsg = {
    case Left(e) =>
      BumpToNormalMsg.ImageLoadFailed(e.message)

    case Right(img) =>
      BumpToNormalMsg.ImageLoadSucceeded(img)
  }

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
