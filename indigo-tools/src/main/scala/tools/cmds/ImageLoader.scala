package tools.cmds

import tyrian.Cmd
import tyrian.Task

import org.scalajs.dom.html
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event

object ImageLoader:

  def load[Msg](path: String)(resultToMessage: Either[Error, html.Image] => Msg): Cmd[Msg] =
    Task
      .RunObservable[Error, html.Image] { observer =>
        val image: html.Image = document.createElement("img").asInstanceOf[html.Image]
        image.src = path
        image.onload = { (_: Event) =>
          observer.onNext(image)
        }
        image.addEventListener(
          "error",
          (_: Event) => observer.onError(Error(s"Image load error from path '$path'")),
          false
        )

        () => ()
      }
      .attempt(resultToMessage)

  final case class Error(message: String)
