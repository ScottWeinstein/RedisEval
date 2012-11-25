package RedisIndexer

import redis.clients.jedis.{JedisPool, Jedis, JedisPoolConfig}
import org.squeryl.SessionFactory
import org.squeryl.Session
import org.squeryl.adapters.MSSQLServer
import org.squeryl.SessionFactory
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema


trait BBSymbolPersist {
  def close(): Unit
  def save(sym: BBSymbol): Unit
}


class ResisBBPersist(pool: JedisPool, jedis: Jedis) extends BBSymbolPersist {
  def save(sym: BBSymbol) = {
    val key = "symbols:" + sym.ID_BB_GLOBAL
    if (!jedis.exists(key)) {
      jedis.hmset(key, sym.toHashMap)
    }
  }

  def close = pool.returnResourceObject(jedis)
}

object RedisbbPersist {
  val config = new JedisPoolConfig()
  config.setMaxIdle(5 * 1000)
  val pool = new JedisPool(config, "localhost")

  def sink() = () => { 
      val jedis = pool.getResource() 
      new ResisBBPersist(pool, jedis)
  }

  def close = pool.destroy
}

object BBSymbolSchema extends Schema {
  val symbols = table[BBSymbol]
}

class SqlBBPersist() extends BBSymbolPersist { 
    val newSession = SessionFactory.newSession

    def save(sym: BBSymbol) = {
      using(newSession) {
        BBSymbolSchema.symbols.insert(sym)
      }
    }

    def close = newSession.cleanup
}

object SqlBBPersist {
  Class.forName("net.sourceforge.jtds.jdbc.Driver")
  val databaseConnectionUrl = "jdbc:jtds:sqlserver://localhost;DatabaseName=StockIndex"
  val dbUsername = "RedisEval"
  val dbPassword = "re"

  SessionFactory.concreteFactory = Some(()=> Session.create(java.sql.DriverManager.getConnection(databaseConnectionUrl, dbUsername, dbPassword), new MSSQLServer))

  SessionFactory.externalTransactionManagementAdapter = Some(()=> Some(Session.create(java.sql.DriverManager.getConnection(databaseConnectionUrl, dbUsername, dbPassword), new MSSQLServer)))

  def sink = () => {
    new SqlBBPersist()
  }
}



object PersistanceFactories {
  println(SqlBBPersist.dbUsername)
   transaction {
    println(BBSymbolSchema.printDdl)
    BBSymbolSchema.create
  }
  
  // val sinks = List(RedisbbPersist.sink, SqlBBPersist.sink)
  val sinks = List(SqlBBPersist.sink)
}

