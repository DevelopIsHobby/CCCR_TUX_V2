using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Mach.Data.MachClient;
using System.Threading;
using System.Globalization;
using System.Net;

/**********************************************************************************
 * 
 * [ MACHBASE .NET Sample Project ]
 * 
 * You SHOULD check if machNetConnector DLL file is linked successfully.
 * 
 *  - Default location is $(MACHBASE_HOME)\lib\machNetConnector.dll
 *  - If you will run this under Linux/Unix system, 
 *    default location should be ${MACHBASE_HOME}/lib/machNetConnector.dll
 *  - If you already have DLLs which is not located in $(MACHBASE_HOME), 
 *    please link correctly.
 *
 **********************************************************************************/

namespace MachConnectorExample
{
    enum ErrorCheckType
    {
        ERROR_CHECK_YES = 0,
        ERROR_CHECK_WARNING,
        ERROR_CHECK_NO
    }

    class Program
    {
        internal const string SERVER_HOST = "127.0.0.1";
        internal const int SERVER_PORT = 5656;

        private static string GetRandomTimeString(Random randomSeed, string aDate)
        {
            return String.Format("{0} {1}:{2}:{3} {4}",
                aDate,
                randomSeed.Next(0, 24).ToString("D2"),
                randomSeed.Next(0, 60).ToString("D2"),
                randomSeed.Next(0, 60).ToString("D2"),
                randomSeed.Next(0, 1000000).ToString("D6"));
        }

        private static string GetRandomIPv4(Random randomSeed)
        {
            return String.Format("{0}.{1}.{2}.{3}",
                randomSeed.Next(0, 256),
                randomSeed.Next(0, 256),
                randomSeed.Next(0, 256),
                randomSeed.Next(0, 256));
        }

        private static string GetRandomIPv6(Random randomSeed)
        {
            switch (randomSeed.Next(0, 3))
            {
                case 0: // Compatible IPv4
                    return String.Format("::{0}.{1}.{2}.{3}",
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256));
                case 1: // Compatible IPv4 filled with ffff
                    return String.Format("::ffff:{0}.{1}.{2}.{3}",
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256),
                        randomSeed.Next(0, 256));
                case 2: // IPv6
                    {
                        byte[] bytes = new byte[16];
                        randomSeed.NextBytes(bytes);
                        IPAddress ipv6Address = new IPAddress(bytes);
                        return ipv6Address.ToString();
                    }
                default:
                    return null;
            }
        }

        private static void ExecuteQuery(MachConnection aConn, string aQueryString, ErrorCheckType aCheckType)
        {
            using (MachCommand sCommand = new MachCommand(aQueryString, aConn))
            {
                try
                {
                    sCommand.ExecuteNonQuery();
                }
                catch (MachException me)
                {
                    switch (aCheckType)
                    {
                        case ErrorCheckType.ERROR_CHECK_YES:
                            throw me;
                            break;
                        case ErrorCheckType.ERROR_CHECK_WARNING:
                            Console.WriteLine("[WARNING!]");
                            Console.WriteLine("{0}", me.ToString());
                            break;
                        case ErrorCheckType.ERROR_CHECK_NO:
                        default:
                            break;
                    }
                }
            }
        }

        private static void ExecuteSelect(MachConnection aConn, string aSelectQueryString)
        {
            using (MachCommand sSelectCommand = new MachCommand(aSelectQueryString, aConn))
            {
                MachDataReader sDataReader = sSelectCommand.ExecuteReader();

                int sCount = 0;
                while (sDataReader.Read())
                {
                    Console.WriteLine("------------------");
                    for (int i = 0; i < sDataReader.FieldCount; i++)
                    {
                        Console.WriteLine(String.Format("{0} : {1}",
                                                        sDataReader.GetName(i),
                                                        sDataReader.GetValue(i)));
                    }
                    sCount++;
                }

                Console.WriteLine("\n------------------");
                Console.WriteLine("{0} Rows selected", sCount);
            }
        }

        private static void ExecuteAppend(MachConnection aConn, int aRecordCount)
        {
            using (MachCommand sAppendCommand = new MachCommand(aConn))
            {
                MachAppendWriter sWriter = sAppendCommand.AppendOpen(TABLE_NAME);
                Random randomSeed = new Random();
                DateTime sStartTime = DateTime.Now;

                var sList = new List<object>();
                for (int i = 1; i <= aRecordCount; i++)
                {
                    sList.Add((Int16)i);                       //SEQ SHORT
                    sList.Add((Int64)i * 4);                       //TOTAL LONG
                    sList.Add((Single)(i * 2.2));                       //PERCENTAGE FLOAT
                    sList.Add((Double)(i * 16.4));                       //RATIO DOUBLE
                    sList.Add(i * 2 );                       //ID INTEGER
                    sList.Add(String.Format("NAME_{0}", i % 100));              //NAME VARCHAR(20)
                    sList.Add(GetRandomTimeString(randomSeed, "2020-01-01"));   //ETIME DATETIME
                    sList.Add(String.Format("MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_{0}", i % 100));              //MESSAGE text
                    sList.Add(GetRandomIPv4(randomSeed));                       //SRC_IP IPv4
                    sList.Add(GetRandomIPv6(randomSeed));                       //DEST_IP IPv6
                    sList.Add(Encoding.UTF8.GetBytes(String.Format("IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_{0}", i % 100)));              //image binary


                    sAppendCommand.AppendData(sWriter, sList, "yyyy-MM-dd HH:mm:ss ffffff");
                    sList.Clear();
                }

                sAppendCommand.AppendClose(sWriter);

                Console.WriteLine(String.Format("Success Count : {0}", sWriter.SuccessCount));
                Console.WriteLine(String.Format("Elapsed Time : {0}", DateTime.Now.Subtract(sStartTime)));
            }
        }

        static void Main(string[] args)
        {
            //-------------------
            // Connection Open
            //-------------------
            MachConnection sConn = new MachConnection(String.Format("SERVER={0};PORT_NO={1};UID=SYS;PWD=MANAGER", SERVER_HOST, SERVER_PORT));
            sConn.Open();

            // DDL : DROP TABLE && CREATE TABLE
            ExecuteQuery(sConn, DROP_QUERY, ErrorCheckType.ERROR_CHECK_NO);
            ExecuteQuery(sConn, CREATE_QUERY, ErrorCheckType.ERROR_CHECK_YES);

            // INSERT
            ExecuteQuery(
                sConn,
                String.Format(INSERT_QUERY,
                              2,
                              4,
                              8.5,
                              10.9,
                              1,
                              "TEST_NAME1",
                              "2020-01-01 00:00:00 000:000",
                              "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                              "192.168.0.1",
                              "21da:d3::2f3b:2aa:ff:fe28:9c5a",
                              "FAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFA"),
                ErrorCheckType.ERROR_CHECK_YES);

            // SELECT
            Console.WriteLine("\n==========SELECT==========");
            ExecuteSelect(sConn, SELECT_QUERY);

            // APPEND
            Console.WriteLine("\n==========APPEND==========");
            ExecuteAppend(sConn, 100000);

            // SELECT
            Console.WriteLine("\n==========SELECT==========");
            ExecuteSelect(sConn, SELECT_QUERY);

            //-------------------
            // Connection Close
            //-------------------
            sConn.Close();

            Console.WriteLine("Press any key to exit.");
            Console.Read();
        }

        internal const string TABLE_NAME = @"SAMPLE_TABLE";
        internal const string CREATE_QUERY = @"CREATE TABLE SAMPLE_TABLE (
                                                    SEQ      short,
                                                    TOTAL      long,
                                                    PERCENTAGE      float,
                                                    RATIO      double,
                                                    ID      INTEGER,
                                                    NAME    VARCHAR(20),
                                                    ETIME   DATETIME,
                                                    MESSAGE text,
                                                    SRC_IP  IPV4,
                                                    DEST_IP IPV6,
                                                    IMAGE binary

                                                );";
        internal const string DROP_QUERY = @"DROP TABLE SAMPLE_TABLE;";

        internal const string INSERT_QUERY = @"INSERT INTO SAMPLE_TABLE VALUES({0}, {1}, {2}, {3}, {4}, '{5}', '{6}', '{7}', '{8}', '{9}', '{10}');";
        internal const string SELECT_QUERY = @"SELECT * FROM SAMPLE_TABLE LIMIT 5;";
    }
}
