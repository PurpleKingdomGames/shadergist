import scala.sys.process._
import scala.language.postfixOps
import sbt.Credentials

lazy val indigoTools =
  (project in file("indigo-tools"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      scalaVersion := "3.0.0",
      name := "indigo-tools",
      scalaJSUseMainModuleInitializer := true,
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      libraryDependencies ++= Seq(
        "davesmith00000" %%% "scalm" % "0.0.1-SNAPSHOT",
        "org.scalameta" %%% "munit" % "0.7.26" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework")
    )

lazy val tools =
  (project in file("."))
    .settings(
      code := { "code ." ! }
    )
    .enablePlugins(ScalaJSPlugin)
    .aggregate(indigoTools)

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")

addCommandAlias(
  "buildTools",
  List(
    "compile",
    "indigoTools/fastOptJS"
  ).mkString(";", ";", "")
)
