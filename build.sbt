name := "WikiPageProcessor"

version := "0.1"

scalaVersion := "2.12.10"


libraryDependencies ++= Seq( "org.apache.spark" % "spark-core_2.11" % "2.4.7",
  "org.apache.spark" % "spark-sql_2.11" % "2.4.7",
  "org.scala-lang" % "scala-reflect" % "2.12.10"


)
lazy val app = (project in file("app"))
  .settings(
    assembly / mainClass := Some("com.learning.WikiPageProcessor"),
    // more settings here ...
  )

lazy val utils = (project in file("utils"))
  .settings(
    assembly / assemblyJarName := "utils.jar",
    // more settings here ...
  )
ThisBuild / assemblyMergeStrategy  := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith(".class") => MergeStrategy.first
  case x if x.endsWith(".html") => MergeStrategy.first
  case x if x.endsWith(".properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}