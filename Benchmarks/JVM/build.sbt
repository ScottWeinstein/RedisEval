name := "EvalRedis"

version := "0.1.0-SNAPSHOT"

description := "EvalRedis"

scalaVersion := "2.9.2"

offline := true

scalacOptions ++= Seq("-deprecation", "-unchecked")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

/* entry point */
mainClass in (Compile, packageBin) := Some("EvalRedis.myproj.Main")

mainClass in (Compile, run) := Some("EvalRedis.myproj.Main")

/* dependencies */
libraryDependencies ++= Seq (
  "net.debasishg" % "redisclient_2.9.1" % "2.5",
  "redis.clients" % "jedis" % "2.1.0"
  // -- lang --
  // "org.apache.commons" % "commons-lang3" % "3.1",
  // "org.scalaz" %% "scalaz-core" % "7.0.0-M4",
  // "org.scalaz" %% "scalaz-effect" % "7.0.0-M4",
  // "joda-time" % "joda-time" % "2.1",
  // -- collections --
  // "org.scalaj" %% "scalaj-collection" % "1.2",
  // "com.google.guava" % "guava" % "13.0.1",
  // "com.chuusai" %% "shapeless" % "1.2.2",
  // -- io --
  // "commons-io" % "commons-io" % "2.4",
  // "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.1-seq",
  // "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1-seq",
  // -- logging & configuration --
  // "org.clapper" %% "grizzled-slf4j" % "0.6.10",
  // "ch.qos.logback" % "logback-classic" % "1.0.7",
  // "com.typesafe" % "config" % "1.0.0", //(also included in akka-actor)
  // -- persistence & serialization --
  // "org.json4s" %% "json4s-native" % "3.0.0",
  // "com.novus" %% "salat" % "1.9.2-SNAPSHOT",
  // "com.typesafe.akka" % "akka-actor" % "2.0.3",
  // "com.h2database" % "h2" % "1.2.127",
  // "mysql" % "mysql-connector-java" % "5.1.10",
  // -- concurrency --
  // "com.typesafe.akka" % "akka-actor" % "2.0.3",
  // "org.scala-stm" %% "scala-stm" % "0.6",
  // -- network --
  //  "net.databinder.dispatch" %% "dispatch-core" % "0.9.2",
  // -- testing --
  // "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
  // "org.specs2" %% "specs2" % "1.12.2" % "test",
  // "org.scalatest" %% "scalatest" % "2.0.M4" % "test"
)

/* you may need these repos */
resolvers ++= Seq(
  // Resolver.sonatypeRepo("snapshots")
  // Resolver.typesafeIvyRepo("snapshots")
  // Resolver.typesafeIvyRepo("releases")
  // Resolver.typesafeRepo("releases")
  // Resolver.typesafeRepo("snapshots")
  // JavaNet2Repository,
  // JavaNet1Repository
)

/* sbt behavior */
logLevel in compile := Level.Warn

traceLevel := 5

/* assembly plugin */
mainClass in AssemblyKeys.assembly := Some("EvalRedis.myproj.Main")

assemblySettings

test in AssemblyKeys.assembly := {}
