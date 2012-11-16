package RedisIndexer
  case class SymbolFile(name: String) {
    val cacheLocation = "cache"
    val host = "bdn-ak.bloomberg.com"
    val uri = "/precanned/" + name + postfix
    val prefix = "http://bdn-ak.bloomberg.com/precanned/"
    val postfix = "_20121118.txt.zip"
    val url = prefix + name + postfix
    val zipFileName = cacheLocation + "/" + name + ".zip"
  }
