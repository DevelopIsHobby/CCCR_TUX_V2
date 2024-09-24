using System;
using System.Threading.Tasks;
using System.Net.Http;
using System.Net.Http.Headers;

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

namespace Restful_Sample
{

    class MachConnectorExampleMain
    {

        private static async Task _dataAppendAsync(HttpClient client)
        {

            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, "http://127.0.0.1:5657/machbase");
            request.Content = new StringContent("{\"name\":\"test_table\", \"date_format\":\"YYYY-MM-DD HH24:MI:SS\",\"values\":[[\"test\", \"1999-01-01 00:00:01\", 1], [\"test\", \"1999-01-01 00:00:02\", 2], [\"test\", \"1999-01-01 00:00:03\", 3]]}");
            request.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            HttpResponseMessage response = await client.SendAsync(request);
            string responseBody = await response.Content.ReadAsStringAsync();
        }

        static async Task Main(string[] args)
        {

            int loop = 0;
            HttpClient client = new HttpClient();

            _dataAppendAsync(client);

            Console.WriteLine("Press any key to exit.");
            Console.Read();
        }
    }
}
