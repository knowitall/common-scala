organization := "edu.knowitall.common-scala"

name := "common-scala"

version := "1.0.10-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "org.scalaz" %% "scalaz-core" % "6.0.4",
    "junit" % "junit" % "4.11" % "test",
    "org.specs2" %% "specs2" % "1.12.3" % "test"
    )

pomExtra := (
  <description>Common functionality for the KnowItAll group.</description>
  <url>https://github.com/knowitall/common-scala</url>
  <licenses>
    <license>
      <name>BSD 3-clause License</name>
      <url>http://www.opensource.org/licenses/bsd-3-clause</url>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/knowitall/common-scala</url>
    <connection>scm:git://github.com/knowitall/common-scala.git</connection>
    <developerConnection>scm:git:git@github.com:knowitall/common-scala.git</developerConnection>
    <tag>HEAD</tag>
  </scm>
  <developers>
   <developer>
      <name>Michael Schmitz</name>
    </developer>
    <developer>
      <name>Robert Bart</name>
    </developer>
  </developers>)
