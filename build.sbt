organization := "edu.washington.cs.knowitall.common-scala"

name := "common-scala"

description := "Common functionality for the KnowItAll group."

version := "1.1.2"

crossScalaVersions := Seq("2.11.4", "2.10.2", "2.9.3")

scalaVersion <<= crossScalaVersions { (vs: Seq[String]) => vs.head }

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.11" % "test",
    "org.specs2" % "specs2" % "2.3.11" % "test" cross CrossVersion.binaryMapped {
      case "2.9.3" => "2.9.2"
      case "2.10.2" => "2.10"
      case "2.11.4" => "2.11"
      case x => x
    },
    "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
  )

licenses := Seq("BSD 3-clause License" -> url("http://www.opensource.org/licenses/bsd-3-clause"))

homepage := Some(url("https://github.com/knowitall/common-scala"))

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
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
