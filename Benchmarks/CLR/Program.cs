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
        private const string SqlConnectionString = "Server=(local);Database=StockIndex;Trusted_Connection=True;";
        //static void Main(string[] args)
        //{
        //    RedisTest("ib");
        //}

        public static int iterations = 1000 * 10;
        public static string[] Keys = new[] {
        "AA" ,"AXP" ,"BA" ,"BAC" ,"CAT"
    ,"CSCO" ,"CVX"
    ,"DD"
    ,"DIS"
    ,"GE"
    ,"HD"
    ,"HPQ"
    ,"IBM"
    ,"INTC"
    ,"JNJ"
    ,"JPM"    ,"KO"    ,"MCD"    ,"MMM"    ,"MRK"    ,"MSFT"    ,"PFE"    ,"PG"    ,"T"    ,"TRV"    ,"UNH"    ,"UTX"    ,"VZ"    ,"WMT"
    ,"XOM"    };


        public static void Init(string[] args)
        {
            System.Diagnostics.Process.Start(@"C:\Users\Scott\Documents\d\GitHub\redis\msvs\bin\release\redis-server.exe");
        }

        [Benchmark]
        public static void SQL()
        {
            using (var cn = new SqlConnection(SqlConnectionString))
            {
                cn.Open();
                for (int i = 0; i < iterations; i++)
                {
                    foreach (var k in Keys)
                    {
                        //                        var o = Dapper.SqlMapper.Query(cn, "select count(*) from dbo.Symbols where Name like @key or [Desc] like @key", new { key = k + "%" });
                        var o = Dapper.SqlMapper.Query<string>(cn, "select Name from dbo.Symbols where Name = @key ", new { key = k + "%" });
                    }
                }
            }
        }

        [Benchmark]
        public static void SqlIncrement()
        {
            using (var cn = new SqlConnection(SqlConnectionString))
            {
                cn.Open();
                Dapper.SqlMapper.Execute(cn, "delete Increment");
                Dapper.SqlMapper.Execute(cn, "insert into Increment values (0)");
                for (int i = 0; i < iterations; i++)
                {
                    Dapper.SqlMapper.Execute(cn, "update Increment set count = count + 1");
                }
            }
        }

        [Benchmark]
        public static void RedisIncrement()
        {
            using (IRedisNativeClient nrc = new RedisNativeClient())
            {
                nrc.Del("Increment");
                for (int i = 0; i < iterations; i++)
                {
                    nrc.Incr("Increment");
                }
            }
        }

        
        [Benchmark]
        public async static void BooksleeveIncrement()
        {
            var sw = new System.Diagnostics.Stopwatch();
            sw.Start();
            using (var conn = new BookSleeve.RedisConnection("localhost"))
            {
                await conn.Open();
                await conn.Keys.Remove(0, "Increment");
                
                for (int i = 0; i < iterations; i++)
                {
                    conn.Strings.Increment(0, "Increment");
                }
                var x = await conn.Strings.Get(0, "Increment");
                sw.Stop();
                Console.WriteLine(x);
                Console.WriteLine(sw.ElapsedMilliseconds);
            }
        }


        [Benchmark]
        public static void ClrIncrement()
        {
            int ii =0;
            for (int i = 0; i < iterations; i++)
            {
                ii++;
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
                        var res = redisClient.GetAllWithScoresFromSortedSet("eq:docs:" + k + "*");
                    }
                }
            }
        }
    }
}
