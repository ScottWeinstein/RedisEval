package EvalRedis.myproj
import com.redis._
import redis.clients.jedis.Jedis

object Main extends App {
  println("hello sbt.g8")
  time("RedisClient", { incr(1000 * 10) })
  time("JedisClient", { incrJedis(1000 * 10) })

  time("RedisClient", { incr(1000 * 10) })
  time("JedisClient", { incrJedis(1000 * 10) })
  
  def time[R](msg: String, block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println(msg + " " + (t1 - t0) / 1000000 + " ms")
    result
  }


  def incr(runs: Int) {
    val r = new RedisClient("localhost", 6379)
    var ii = runs
    while(ii > 0) {
      r.incr("Increment")
      ii = ii - 1
    }
    r.get("Increment")
  }

  def incrJedis(runs: Int) {
    val r = new Jedis("localhost")
    var ii = runs
    while(ii > 0) {
      r.incr("Increment")
      ii = ii - 1
    }
    r.get("Increment")
  }
}
