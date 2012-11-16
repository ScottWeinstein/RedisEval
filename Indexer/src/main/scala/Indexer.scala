package RedisIndexer

import akka.actor._
import akka.dispatch.Await
import akka.dispatch.Future
import akka.pattern.ask
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.Timeout
import akka.util.duration._
import java.io.{InputStream, FileOutputStream}
import spray.can.client.HttpClient
import spray.client.HttpConduit
import spray.http._
import spray.io._
import spray.util._
import redis.clients.jedis.{JedisPool, Jedis, JedisPoolConfig}

object Indexer extends App {
  implicit val system = ActorSystem("idx")
  import system.log
  import HttpConduit._
  import HttpMethods._

  // every spray-can HttpClient (and HttpServer) needs an IOBridge for low-level network IO
  // (but several servers and/or clients can share one)
  val ioBridge = new IOBridge(system).start()

  // since the ioBridge is not an actor it needs to be stopped separately,
  // here we hook the shutdown of our IOBridge into the shutdown of the applications ActorSystem
  system.registerOnTermination(ioBridge.stop())

  // create and start a spray-can HttpClient
  val httpClient = system.actorOf(
    props = Props(new HttpClient(ioBridge)),
    name = "http-client"
  )

  startExample1()

  def downloadFileCached(pipeline: HttpRequest => Future[HttpResponse], sf: SymbolFile): Future[String] = {
    val f = new java.io.File(sf.zipFileName)
    if (f.exists()) {
      Future(sf.zipFileName)
    } 
    else {
      log.info(sf.url)
      pipeline(Get(sf.url)).map((resp:spray.http.HttpResponse) => {
        val fos = new FileOutputStream(sf.zipFileName)
        fos.write(resp.entity.buffer)
        fos.close()
        log.info("done " + sf.name)
        sf.zipFileName
      })
    }
  }

  def ioStreamtoBBSymbols(pool: JedisPool, st: java.io.InputStream) = {
    val jedis = pool.getResource()

    try {
      val in = scala.io.Source.fromInputStream(st)

      val syms = for {
        line <- in.getLines
        if (!(line.startsWith("#") || line.startsWith("NAME|ID_BB_SEC_NUM_DES|FEED_SOURCE|")))
        } yield BBSymbol(line)
      
      var count = 0
        for (sym <- syms) {
          val key = "symbols:" + sym.ID_BB_GLOBAL
          if (!jedis.exists(key)) {
            jedis.hmset(key, sym.toHashMap)
            count = count + 1
          }
        }
      count
    }
    finally {
      st.close
      pool.returnResourceObject(jedis)
    }
  }

  def startExample1() {
    import collection.JavaConverters._
    val sf = SymbolFile("")
    val config = new JedisPoolConfig()
    config.setMaxIdle(5 * 1000)
    val pool = new JedisPool(config, "localhost")
    (new java.io.File(sf.cacheLocation)).mkdir()
    
    val conduit = system.actorOf(props = Props(new HttpConduit(httpClient, sf.host)))
    val pipeline = HttpConduit.sendReceive(conduit)
    val listOfFutureZipFiles = symbolZipFiles.map { sf => downloadFileCached(pipeline, sf) }

    // List[Future[Stream[RedisIndexer.BBSymbol]]]
    val lsOfSomething = listOfFutureZipFiles map { fzipFileName => fzipFileName map { zipFileName => {
       val rootzip = new java.util.zip.ZipFile(zipFileName)
       val entries = rootzip.entries.asScala
       val counts = entries map { ze => ioStreamtoBBSymbols(pool, rootzip.getInputStream(ze))}
       counts.sum
      }
    }
    }
    
    val futureList = Future.sequence(lsOfSomething)
    Await.result(futureList, 2000 second)
    system.stop(conduit)
    system.shutdown
    pool.destroy
  }

  

  def symbolZipFiles: List[SymbolFile] = {
    val source = scala.io.Source.fromFile("SymbolFiles.txt")
    val lines = source.getLines.map( SymbolFile(_))
    
    try { 
      lines.toList
    } finally {
      source.close()
    }
  }
}
