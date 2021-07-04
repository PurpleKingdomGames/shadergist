import scala.sys.process._
import scala.language.postfixOps
import sbt.Credentials

lazy val commonSettings =
  Seq(
    scalaVersion := "3.0.0",
    libraryDependencies ++= Seq(
      "org.scalameta" %%% "munit" % "0.7.26" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )

lazy val indigoTools =
  (project in file("indigo-tools"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "indigo-tools",
      scalaJSUseMainModuleInitializer := true,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian" % "0.2.0-SNAPSHOT"
      )
    )

lazy val toy =
  (project in file("toy"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "indigo-toy",
      scalaJSUseMainModuleInitializer := true,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian" % "0.2.0-SNAPSHOT"
      )
    )

lazy val toyGame =
  (project in file("toy-game"))
    .enablePlugins(ScalaJSPlugin, SbtIndigo)
    .settings(commonSettings: _*)
    .settings(
      name := "Indigo Toy",
      version := "0.0.1",
      showCursor := true,
      title := "My Game - Made with Indigo",
      gameAssetsDirectory := "assets",
      windowStartWidth := 550,
      windowStartHeight := 400,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo-json-circe" % "0.8.3-SNAPSHOT",
        "io.indigoengine" %%% "indigo"            % "0.8.3-SNAPSHOT",
        "io.indigoengine" %%% "indigo-extras"     % "0.8.3-SNAPSHOT"
      )
    )

lazy val tools =
  (project in file("."))
    .settings(
      code := { "code ." ! }
    )
    .enablePlugins(ScalaJSPlugin)
    .aggregate(indigoTools, toy, toyGame)

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")

addCommandAlias(
  "buildTools",
  List(
    "compile",
    "indigoTools/fastOptJS"
  ).mkString(";", ";", "")
)

addCommandAlias(
  "buildToy",
  List(
    "compile",
    "toy/fullOptJS"
  ).mkString(";", ";", "")
)

addCommandAlias(
  "buildGame",
  List(
    "compile",
    "toyGame/fullOptJS",
    "toyGame/indigoBuildFull"
  ).mkString(";", ";", "")
)
