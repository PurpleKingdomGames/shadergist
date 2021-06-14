package tools.cmds

import tyrian.Cmd
import tyrian.Task

import scala.collection.mutable.ArrayBuffer

/** A very, very simple logger that logs to the Browsers console with a few standard headers and the log message.
  */
object Logger:

  private val INFO: String  = "INFO"
  private val ERROR: String = "ERROR"
  private val DEBUG: String = "DEBUG"

  private val errorLogs: ArrayBuffer[String] = new ArrayBuffer[String]()
  private val debugLogs: ArrayBuffer[String] = new ArrayBuffer[String]()

  private def formatMessage(level: String, message: String): String =
    s"""[$level] [Tyrian] $message"""

  private val consoleLogString: String => Unit = message => println(message)

  private val infoString: String => Unit = message => println(formatMessage(INFO, message))

  private val errorString: String => Unit = message => println(formatMessage(ERROR, message))

  private val errorOnceString: String => Unit = message =>
    if (!errorLogs.contains(message)) {
      errorLogs += message
      println(formatMessage(ERROR, message))
    }

  private val debugString: String => Unit = message => println(formatMessage(DEBUG, message))

  private val debugOnceString: String => Unit = message =>
    if (!debugLogs.contains(message)) {
      debugLogs += message
      println(formatMessage(DEBUG, message))
    }

  def consoleLog(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      consoleLogString(messages.toList.mkString(", "))
    }.toCmd

  def info(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      infoString(messages.toList.mkString(", "))
    }.toCmd

  def error(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      errorString(messages.toList.mkString(", "))
    }.toCmd

  def errorOnce(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      errorOnceString(messages.toList.mkString(", "))
    }.toCmd

  def debug(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      debugString(messages.toList.mkString(", "))
    }.toCmd

  def debugOnce(messages: String*): Cmd.SideEffect =
    Task.SideEffect { () =>
      debugOnceString(messages.toList.mkString(", "))
    }.toCmd
