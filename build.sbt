name := "marcel"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "ch.qos.logback" % "logback-core" % "1.1.7"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.4"

// Package dependencies
packAutoSettings
//packMain := Map("MyBot" -> "slack.endofthe.marcel.MyBot")
