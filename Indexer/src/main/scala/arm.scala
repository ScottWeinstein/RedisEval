package RedisIndexer

// http://stackoverflow.com/a/2259254/25201
// autoCompilerPlugins := true

// addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.1")

// scalacOptions += "-P:continuations:enable"


// import scala.util.continuations._

// object  ARM  {
//   // standard using block definition
//   def using[X <: {def close()}, A](resource : X)(f : X => A) = {
//      try {
//        f(resource)
//      } finally {
//        resource.close()
//      }
//   }

//   // A DC version of 'using' 
//   def resource[X <: {def close()}, B](res : X) = shift(using[X, B](res))

//   // some sugar for reset
//   def withResources[A, C](x : => A @cps[A, C]) = reset{x}
// }