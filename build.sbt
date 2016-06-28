name := "console-based-social-networking-application"

version := "1.0"

scalaVersion  := "2.9.2"
sbtVersion    := "0.13.8"

libraryDependencies += "joda-time" % "joda-time" % "2.9.4" withSources() withJavadoc()
libraryDependencies += "org.joda" % "joda-convert" % "1.2"  withSources() withJavadoc()
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19" % Test withSources() withJavadoc()
libraryDependencies += "org.scalatest" % "scalatest_2.9.2" % "1.9.2" % Test withSources() withJavadoc()
