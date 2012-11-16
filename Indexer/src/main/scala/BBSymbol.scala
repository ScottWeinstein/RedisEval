package RedisIndexer

import java.util
import scala.collection.JavaConversions.mapAsJavaMap

case class BBSymbol(NAME: String,
                     ID_BB_SEC_NUM_DES: String,
                     FEED_SOURCE: String,
                     ID_BB_SEC_NUM_SRC: String,
                     ID_BB_UNIQUE: String,
                     SECURITY_TYPE: String,
                     MARKET_SECTOR_DES: String,
                     ID_BB_GLOBAL: String,
                     COMPOSITE_ID_BB_GLOBAL: String,
                     FEED_EID1: String,
                     FEED_EID2: String,
                     FEED_EID3: String,
                     FEED_EID4: String,
                     FEED_DELAYED_EID1: String,
                     Subscription1: String,
                     Subscription2: String,
                     Subscription3: String) {
    def toHashMap: util.Map[String, String] = {
      Map("NAME" -> NAME, 
          "ID_BB_SEC_NUM_DES" -> ID_BB_SEC_NUM_DES,
          "FEED_SOURCE" -> FEED_SOURCE,    
          "ID_BB_SEC_NUM_SRC" -> ID_BB_SEC_NUM_SRC,       
          "ID_BB_UNIQUE" -> ID_BB_UNIQUE,            
          "SECURITY_TYPE" -> SECURITY_TYPE,    
          "MARKET_SECTOR_DES" -> MARKET_SECTOR_DES,     
          "ID_BB_GLOBAL" -> ID_BB_GLOBAL,     
          "COMPOSITE_ID_BB_GLOBAL" -> COMPOSITE_ID_BB_GLOBAL, 
          "FEED_EID1" -> FEED_EID1,   
          "FEED_EID2" -> FEED_EID2,       
          "FEED_EID3" -> FEED_EID3,  
          "FEED_EID4" -> FEED_EID4,  
          "FEED_DELAYED_EID1" -> FEED_DELAYED_EID1,  
          "Subscription1" -> Subscription1,
          "Subscription2" -> Subscription2,
          "Subscription3" -> Subscription3)
    }
  }


  object BBSymbol {
    def apply(inputStr: String): BBSymbol = {
      val a = inputStr.split('|')
      val s = a.toList ++ Range(0, 17 - a.length ).map {_ => "" }
      new BBSymbol(s(0),s(1),s(2),s(3),s(4),s(5),s(6),s(7),s(8),s(9),s(10),s(11),s(12),s(13),s(14),s(15),s(16))
    }
  }
