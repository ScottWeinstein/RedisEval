import org.scalatest.FunSpec
import RedisIndexer._

class BBSymbolSpec extends FunSpec {
  describe("BBSymbol") {
    it("be able to parse a string") {
        val bb = BBSymbol("VTG-LEHNKERING AG|LEHGR L 07/04/06 6|||COLN2785274|ASSET-BASED BRIDGE|Corp|BBG00013Y1F5||46707|||0||||")  
        assert(bb.NAME === "VTG-LEHNKERING AG")
        assert(bb.ID_BB_SEC_NUM_DES === "LEHGR L 07/04/06 6")
        assert(bb.ID_BB_UNIQUE == "COLN2785274")
        assert(bb.Subscription3 === "")
      }
  }
}