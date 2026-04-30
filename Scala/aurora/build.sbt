ThisBuild / organization := "com.sandbox"
ThisBuild / scalaVersion := "3.4.3"
ThisBuild / version := "1.0.0"

ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-encoding",
  "utf8",
)

val Http4sVer = "0.23.29"
val CirceVer = "0.14.11"

lazy val root = (project in file(".")).settings(
  name := "aurora-atelier",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.5.5",
    "org.http4s" %% "http4s-ember-server" % Http4sVer,
    "org.http4s" %% "http4s-dsl" % Http4sVer,
    "org.http4s" %% "http4s-circe" % Http4sVer,
    "io.circe" %% "circe-generic" % CirceVer,
    "ch.qos.logback" % "logback-classic" % "1.5.12",
  ),
)
