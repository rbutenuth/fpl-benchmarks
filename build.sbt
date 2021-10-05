import sbt.Keys._

val scala3Version = "3.0.2"

lazy val root = (project in file("."))
  .enablePlugins(JmhPlugin)
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    compileOrder := CompileOrder.JavaThenScala,
    javacOptions ++= Seq("-source", "11"),

    resolvers += Resolver.mavenLocal,

    libraryDependencies ++= Seq(
      "de.codecentric" % "lists" % "0.0.1-SNAPSHOT",
	  "org.openjdk.jol" % "jol-core" % "0.16"
    )
  )
