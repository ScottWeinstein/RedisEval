using ServiceStack.Redis;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Benchmarks
{
    public static class Program
    {
        //static void Main(string[] args)
        //{
        //    RedisTest("ib");
        //}

        public static int iterations = 1000;
        public static string[] Keys = new[] {
        "AA"
    ,"AXP"
    ,"BA"
    ,"BAC"
    ,"CAT"
    ,"CSCO"
    ,"CVX"
    ,"DD"
    ,"DIS"
    ,"GE"
    ,"HD"
    ,"HPQ"
    ,"IBM"
    ,"INTC"
    ,"JNJ"
    ,"JPM"
    ,"KO"
    ,"MCD"
    ,"MMM"
    ,"MRK"
    ,"MSFT"
    ,"PFE"
    ,"PG"
    ,"T"
    ,"TRV"
    ,"UNH"
    ,"UTX"
    ,"VZ"
    ,"WMT"
    ,"XOM"
    };

        [Benchmark]
        public static void SQL()
        {
            using (var cn = new SqlConnection("Server=(local);Database=StockIndex;Trusted_Connection=True;"))
            {
                cn.Open();
                for (int i = 0; i < iterations; i++)
                {
                    foreach (var k in Keys)
                    {
                        var o = Dapper.SqlMapper.Query(cn, "select count(*) from dbo.Symbols where Name like @key or [Desc] like @key", new { key = k + "%" });
                    }
                }
            }
        }

        [Benchmark]
        public static void RedisTest()
        {
            using (IRedisClient redisClient = new RedisClient())
            {
                for (int i = 0; i < iterations; i++)
                {
                    foreach (var k in Keys)
                    {
                        var res = redisClient.GetAllWithScoresFromSortedSet("eq:docs:" + k);
                    }
                }
            }
        }
    }
}
