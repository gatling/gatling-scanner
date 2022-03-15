import net.moznion.sbt.spotless.config.{ GoogleJavaFormatConfig, JavaConfig, SpotlessConfig }

name := "gatling-scanner"

version := "0.1"

scalaVersion := "2.13.8"

inScope(Global) {
  Seq(
    githubPath := "gatling/gatling-scanner",
    gatlingDevelopers := Seq(
      GatlingDeveloper("tpetillot@gatling.io", "Thomas Petillot", isGatlingCorp = true)
    )
  )
}

val junitVersion = "5.8.2"

lazy val root = (project in file("."))
  .enablePlugins(GatlingOssPlugin)
  .settings(
    name := "gatling-scanner",
    crossPaths := false, // drop off Scala suffix from artifact names.
    autoScalaLibrary := false, // exclude scala-library from dependencies
    libraryDependencies ++= Seq(
      "io.gatling"                 % "gatling-asm-shaded"   % "9.2",
      "commons-io"                 % "commons-io"           % "2.11.0",
      "org.junit.jupiter"          % "junit-jupiter-engine" % junitVersion                     % Test,
      "org.junit.jupiter"          % "junit-jupiter-api"    % junitVersion                     % Test,
      "net.aichler"                % "jupiter-interface"    % JupiterKeys.jupiterVersion.value % Test
    ),
    spotlessJava := JavaConfig(
      googleJavaFormat = GoogleJavaFormatConfig()
    ),
    spotless := SpotlessConfig(
      applyOnCompile = !sys.env.getOrElse("CI", "false").toBoolean
    )
  )
