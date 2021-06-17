package tools.pages

import tyrian.{Html, Cmd, Task}
import tyrian.Html._

import tools.Msg.BumpToNormalMsg
import tools.cmds.FileReader
import tools.cmds.ImageLoader
import tools.cmds.Logger
import tools.utils.Sobel

import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js

object BumpToNormal:

  val defaultImagePath: String = "images/shapes.png"

  type Model = Option[FileReader.File[html.Image]]

  def update(msg: BumpToNormalMsg, model: Model): (Model, Cmd[BumpToNormalMsg]) =
    msg match
      case BumpToNormalMsg.FirstLoad =>
        val cmd = model match
          case Some(file) =>
            Cmd.Emit(BumpToNormalMsg.LoadSucceeded(file))

          case None =>
            ImageLoader.load(defaultImagePath) {
              case Left(e) =>
                BumpToNormalMsg.ImageLoadFailed(e.message)

              case Right(img) =>
                BumpToNormalMsg.ImageLoadSucceeded(img)
            }

        (model, cmd)

      case BumpToNormalMsg.FileSelected(inputFieldId) =>
        val readFile = FileReader.readImage(inputFieldId) {
          case Left(e) =>
            BumpToNormalMsg.LoadFailed(e.message)

          case Right(file) =>
            BumpToNormalMsg.LoadSucceeded(file)
        }

        (model, readFile)

      case BumpToNormalMsg.LoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.LoadSucceeded(file) =>
        val cmds = Cmd.batch(
          Logger.info("loaded image", file.name),
          ImageLoader.load(file.path) {
            case Left(e) =>
              BumpToNormalMsg.ImageLoadFailed(e.message)

            case Right(img) =>
              BumpToNormalMsg.ImageLoadSucceeded(img)
          }
        )

        (Some(file), cmds)

      case BumpToNormalMsg.ImageLoadFailed(msg) =>
        (model, Logger.error(msg))

      case BumpToNormalMsg.ImageLoadSucceeded(img) =>
        val cmds = Cmd.batch(
          Logger.info("loaded the image!"),
          WriteToCanvas.bumpToNormal("bump-to-normal-canvas", img)
        )

        (model, cmds)

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
            case None                              => Seq(img(src(defaultImagePath)))
            case Some(FileReader.File(_, path, _)) => Seq(img(src(path)))
        }
        ++ Seq(canvas(id("bump-to-normal-canvas"), width("150"), height("150"))())

    div()(children: _*)

object WriteToCanvas:

  def bumpToNormal[Msg](canvasName: String, img: html.Image): Cmd[Msg] =
    Task.SideEffect { () =>
      val canvas = document.getElementById(canvasName).asInstanceOf[html.Canvas]
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

      // Clone the data to avoid reading and writing at the same time.
      val readFromData = data ++ new js.Array[Double]()

      i = 0
      while i < readFromData.length do
        val samples = Sobel.samplesGrid(i, readFromData.length, img.width, 4).toList

        val red =
          samples
            .zip(Sobel.kernelX)
            .map {
              case (Some(i), k) =>
                readFromData(i).toDouble * k

              case (None, i) =>
                0.0d
            }
            .sum

        val green =
          samples
            .zip(Sobel.kernelY)
            .map {
              case (Some(i), k) =>
                readFromData(i).toDouble * k

              case (None, i) =>
                0.0d
            }
            .sum

        data(i) = 128 + red
        data(i + 1) = 128 + green
        data(i + 2) = 255
        i += 4

      ctx.putImageData(imageData, 0, 0)
    }.toCmd
