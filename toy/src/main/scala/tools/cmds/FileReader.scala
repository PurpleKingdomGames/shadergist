package tools.cmds

import tyrian.Cmd

import org.scalajs.dom
import org.scalajs.dom.raw
import org.scalajs.dom.html
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event

import scala.scalajs.js

object FileReader:

  def read[Msg](inputFieldId: String)(resultToMessage: Either[Error, File[js.Any]] => Msg): Cmd[Msg] =
    val files = document.getElementById(inputFieldId).asInstanceOf[html.Input].files
    if files.length == 0 then Cmd.Empty
    else
      val file = files.item(0)
      Cmd.Run[Error, File[js.Any]] { observer =>
          val fileReader = new dom.FileReader()
          fileReader.addEventListener("load", (e: Event) => {
            observer.onNext(
              File(
                name = file.name,
                path = e.target.asInstanceOf[js.Dynamic].result.asInstanceOf[String],
                data = fileReader.result
              )
            )
          }, false)
          fileReader.onerror = _ => observer.onError(Error(s"Error reading file from input field '$inputFieldId'"))
          fileReader.readAsDataURL(file)

          () => (),
        }
        .attempt(resultToMessage)

  def readImage[Msg](inputFieldId: String)(resultToMessage: Either[Error, File[html.Image]] => Msg): Cmd[Msg] =
    val cast: Either[Error, File[js.Any]] => Either[Error, File[html.Image]] =
      _.map(fi => File(fi.name, fi.path, fi.data.asInstanceOf[html.Image]))
    read(inputFieldId)(cast andThen resultToMessage)

  def readText[Msg](inputFieldId: String)(resultToMessage: Either[Error, File[String]] => Msg): Cmd[Msg] =
    val cast: Either[Error, File[js.Any]] => Either[Error, File[String]] =
      _.map(fi => File(fi.name, fi.path, fi.data.asInstanceOf[String]))
    read(inputFieldId)(cast andThen resultToMessage)

  final case class Error(message: String)
  final case class File[A](name: String, path: String, data: A)
