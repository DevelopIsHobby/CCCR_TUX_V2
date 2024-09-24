using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Threading.Tasks;
using Mach.Data.MachClient;
using System.Threading;
using System.Text;

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

namespace AppendPoolExample
{

    class MachConnectionPoolAppend
    {
        public struct ConnectionStruct
        {
            public MachConnection connection;
            public MachCommand command;
            public MachAppendWriter writer;

            public ConnectionStruct(String conString, String tableName)
            {
                this.connection = new MachConnection(conString);
                this.connection.Open();
                this.command = new(connection);
                this.writer = this.command.AppendOpen(tableName);
            }
        }
        public ConcurrentQueue<ConnectionStruct> connections;
        private string constring;
        private string tablename;
        private int size;

        public MachConnectionPoolAppend(String conString, int Size, String tableName)
        {
            this.constring = conString;
            this.size = Size;
            this.tablename = tableName;
            connections = new ConcurrentQueue<ConnectionStruct>();
            for (int i = 0; i < size; i++)
            {
                this.connections.Enqueue(new ConnectionStruct(constring, tablename));
            }
        }

        public ConnectionStruct GetConnection()
        {
            ConnectionStruct DequeueConnection;
            while (true)
            {
                if (this.connections.TryDequeue(out DequeueConnection))
                {
                    if (!DequeueConnection.connection.IsConnected())
                    {
                        bool opened = false;
                        try
                        {
                            DequeueConnection.connection.Open();
                            opened = true;
                            DequeueConnection.command = new(DequeueConnection.connection);
                            DequeueConnection.writer = DequeueConnection.command.AppendOpen(this.tablename);
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine($"Exception caught while opening connection: {e}");
                            if (opened == true)
                            {
                                DequeueConnection.connection.Close();
                                opened = false;
                            }
                            ReleaseConnection(DequeueConnection);
                            break;
                        }
                    }
                    return DequeueConnection;
                }
                else
                {
                    Thread.Sleep(100);
                }
            }
            throw new Exception("Fail connection Open");
        }

        public void ExecuteAppend(List<object> aList)
        {
            ConnectionStruct connection;
            try
            {
                connection = GetConnection();
                try
                {
                    connection.command.AppendData(connection.writer, aList);
                    connection.command.AppendFlush(connection.writer);
                }
                catch
                {
                    Console.WriteLine("Exception caught while during input data processing");
                    if (connection.connection.IsConnected())
                    {
                        connection.connection.Close();
                    }
                    this.connections.Enqueue(new ConnectionStruct(constring, tablename));
                    return;
                }
                ReleaseConnection(connection);
            }
            catch (Exception ex)
            {
                Console.Write("Error" + ex.Message);
            }
        }
        public void ReleaseConnection(ConnectionStruct connection)
        {
            if (this.connections.Count < this.size)
            {
                this.connections.Enqueue(connection);
            }
            else
            {
                connection.connection.Close();
            }
        }
        public void FinalizeAllConnections()
        {
            ConnectionStruct DequeueConnection;
            if (this.connections.TryDequeue(out DequeueConnection))
            {
                DequeueConnection.connection.Close();
            }

        }

    }


    class MachConnectorExampleMain
    {
        internal const string SERVER_HOST = "127.0.0.1";
        internal const int SERVER_PORT = 5656;

        static void Main(string[] args)
        {

            //-------------------
            // Connection Open
            //-------------------

            //Data Create--------------------------------------------------
            var sList = new List<object>();
            sList.Add((Int16)1);                       //SEQ SHORT
            sList.Add((Int64)1 * 4);                       //TOTAL LONG
            sList.Add((Single)(1 * 2.2));                       //PERCENTAGE FLOAT
            sList.Add((Double)(1 * 16.4));                       //RATIO DOUBLE
            sList.Add(1 * 2);                       //ID INTEGER
            sList.Add(String.Format("MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_MESSAGE_TEXT_{0}", 1 % 100));              //MESSAGE text
            sList.Add(Encoding.UTF8.GetBytes(String.Format("IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_IMAGE_BINARY_{0}", 1 % 100)));              //image binary
            //Data Create Finish-------------------------------------------

            //Machbase Connection Pool Class
            MachConnectionPoolAppend MachConPool = new MachConnectionPoolAppend(String.Format("SERVER={0};PORT_NO={1};UID=SYS;PWD=MANAGER", SERVER_HOST, SERVER_PORT), 50, "SAMPLE_TABLE");

            //Start Time
            Console.WriteLine(DateTime.Now);

            // Parallel TEST
            Parallel.For(0, 500000, (i) =>
            {
                MachConPool.ExecuteAppend(sList);
            });
            Parallel.For(0, 500000, (i) =>
            {
                MachConPool.ExecuteAppend(sList);
            });
            Parallel.For(0, 500000, (i) =>
            {
                MachConPool.ExecuteAppend(sList);
            });
            Parallel.For(0, 500000, (i) =>
            {
                MachConPool.ExecuteAppend(sList);
            });
            Parallel.For(0, 500000, (i) =>
            {
                MachConPool.ExecuteAppend(sList);
            });

            //End Time
            Console.WriteLine(DateTime.Now);
            Console.WriteLine("Append Finish");
            MachConPool.FinalizeAllConnections();

            // Sleep
            Console.Read();

        }

        internal const string TABLE_NAME = @"SAMPLE_TABLE";
        internal const string CREATE_QUERY = @"CREATE TABLE SAMPLE_TABLE (
                                                    SEQ      short,
                                                    TOTAL      long,
                                                    PERCENTAGE      float,
                                                    RATIO      double,
                                                    ID      INTEGER,
                                                    MESSAGE varchar(200),
                                                    IMAGE binary
                                                );";
    }
}

