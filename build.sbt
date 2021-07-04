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

lazy val site =
  (project in file("shadergist-site"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "shadergist-site",
      scalaJSUseMainModuleInitializer := true,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian" % "0.2.0-SNAPSHOT"
      )
    )

lazy val game =
  (project in file("shadergist-game"))
    .enablePlugins(ScalaJSPlugin, SbtIndigo)
    .settings(commonSettings: _*)
    .settings(
      name := "shadergist-game",
      version := "0.0.1",
      showCursor := true,
      title := "shadergist",
      gameAssetsDirectory := "assets",
      windowStartWidth := 550,
      windowStartHeight := 400,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo-json-circe" % "0.8.3-SNAPSHOT",
        "io.indigoengine" %%% "indigo"            % "0.8.3-SNAPSHOT",
        "io.indigoengine" %%% "indigo-extras"     % "0.8.3-SNAPSHOT"
      )
    )

lazy val shadergist =
  (project in file("."))
    .settings(
      code := { "code ." ! }
    )
    .enablePlugins(ScalaJSPlugin)
    .aggregate(site, game)

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")

addCommandAlias(
  "buildSite",
  List(
    "compile",
    "site/fullOptJS"
  ).mkString(";", ";", "")
)

addCommandAlias(
  "buildGame",
  List(
    "compile",
    "game/fullOptJS",
    "game/indigoBuildFull"
  ).mkString(";", ";", "")
)

addCommandAlias(
  "buildAll",
  List(
    "buildGame",
    "buildSite"
  ).mkString(";", ";", "")
)
