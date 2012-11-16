name := "RedisSymbolIndex"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-client" % "1.0-M5",
  "com.typesafe.akka" % "akka-actor" % "2.0.3",
  "org.squeryl" %% "squeryl" % "0.9.5-2",
  "net.sourceforge.jtds" % "jtds" % "1.2.4",
  "redis.clients" % "jedis" % "2.1.0",
  "org.scalatest" %% "scalatest" % "2.0.M5" % "test"
)