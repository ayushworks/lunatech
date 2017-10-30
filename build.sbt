name := """Lunatech"""
organization := "com.lunatech"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.nrinaudo" %% "kantan.csv" % "0.3.0"

libraryDependencies += "com.nrinaudo" %% "kantan.csv-generic" % "0.3.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.lunatech.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.lunatech.binders._"
