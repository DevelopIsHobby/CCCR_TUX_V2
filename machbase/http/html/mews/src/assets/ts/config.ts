import { random } from './common';
import mixin from '../../mixins';
import '../@mdi/font/css/materialdesignicons.css'


const gUrl  = mixin.data().gUrl;
const gHost = mixin.data().gHost;
const gId   = mixin.data().gId;
const gPw   = mixin.data().gPw;

type tMenu = {
    name: string;
    type?: string;
    url?: string;
    icon?: string;
    blank?: boolean;
};

const gMenus: tMenu[] = [
    {
        name: 'api',
        url: '/api',
        icon: 'mdi-api',
        blank: false
    },
    {
        name: 'example',
        url: '/example',
        icon: 'mdi-chart-line',
        blank: false
    },
    // {
    //     name: 'guide',
    //     url: '/url',
    //     icon: 'mdi-book-open'
    // },
    {
        name: '',
        type: 'divider'
    },
    {
        name: 'document',
        url: 'http://endoc.machbase.com',
        icon: 'mdi-text-box-search-outline',
        blank: true
    },
    {
        name: 'homepage',
        url: 'https://www.machbase.com',
        icon: 'mdi-export',
        blank: true
    }
];

const gUsage = {
    title: 'How to Use',
    description: `
    <h3>Default URL</h3>
    The default URL for the REST API is <b>http://hostname:port/machbase</b>. <br>
    ex) ${gUrl} <br><br>
    
    <h3>Data Extraction</h3>
    Retrieves the data using the HTTP GET method. The return value is a json type. You can pass the query statement you want to execute using the <b>'q'</b> parameter. <br><br>
    
    <h3>Data Input</h3>
    You can use the HTTP POST method to send the input value of the json type as a parameter. The data input json can use three keys<b>(name, values, date_format)</b>, and the 'name' and 'values' keys must be entered. <br>
    If you enter a date type, you must specify the date format. The date format must use the pattern used in Machbase. If not specified otherwise, is set to <b>YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn</b>. Machbase default format and the result value will not be returned properly if date format is not correct. When specifying date and time pattern, you should be careful of case sensitive.`
};

const gGenerate = {
    title: 'Generating Data for Example Chart',
    description: `
        Generate data randomly for drawing a chart. <br>
        Create Tag Table SQL: create tagdata table TAG (name varchar(50) primary key, time datetime basetime, value double summarized) <br>
        Tag Name: s_0~5, Base Time: now-6h, Value: random integer within 1 ~ 1024
    `
};

const gAppend = {
    title: 'Appending Data to Tag Table',
    description: `
        Use POST Method to add data to the Tag table for drawing a chart. Below is an example of adding data using curl.
    `
};

const gDraw = {
    title: 'Drawing Data from Tag Table',
    description: `
        Draws a chart by reading the data added to the Tag table. Please refer to the source code attached.
    `
};

const gTabB = [
    {
        name: 'query',
        table: 'V$STMT'
    },
    {
        name: 'property',
        table: 'V$PROPERTY'
    },
    {
        name: 'memory',
        table: 'V$SYSMEM'
    },
    {
        name: 'cluster',
        table: 'V$NODE_STATUS'
    }
];

const gQuery = [
    {
        name: 'CREATE TABLE',
        method: 'get',
        query: 'create log table sample (id integer, name varchar(20), value integer)'
    },
    {
        name: 'INSERT DATA',
        method: 'get',
        query: `insert into sample values (1, 's_test', ${random()})`
    },
    {
        name: 'APPEND DATA',
        method: 'post',
        query: `{ "name": "sample", "date_format": "YYYY-MM-DD", "values": [[${random()}, "s_t01", 128], [${random()}, "s_t02", 256], [${random()}, "s_t03", 512]] }`,
        data: { "name": "sample", "date_format": "YYYY-MM-DD", "values": [[11, "s_t01", random()], [12, "s_t02", random()], [13, "s_t03", random()]] }
    },
    {
        name: 'SELECT DATA',
        method: 'get',
        query: 'select * from sample'
    },
    {
        name: 'DELETE DATA',
        method: 'get',
        query: 'delete from sample'
    },
    {
        name: 'DROP TABLE',
        method: 'get',
        query: 'drop table sample'
    }
];

const gEdition = [
    {
        name: 'Edition',
        query: 'V$VERSION - BINARY_DB_MAJOR_VERSION, BINARY_DB_MINOR_VERSION'
    },
    {
        name: 'Meta Edition',
        query: 'V$VERSION - BINARY_META_MAJOR_VERSION, BINARY_META_MINOR_VERSION'
    },
    {
        name: 'Comm Edition',
        query: 'V$VERSION - BINARY_CM_MAJOR_VERSION, BINARY_CM_MINOR_VERSION'
    },
    {
        name: 'Sessions',
        query: 'V$SESSION - count(*)'
    },
    {
        name: 'Web Connection Count',
        query: 'V$HTTP_STATUS'
    }
];

const gChartSource = `<!DOCTYPE html>
<html lang="en">
<body>
    <div id="chart"></div>

<script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
<script src="https://momentjs.com/downloads/moment.js"></script>
<script>
    const g = { // global variables
        chartOptions: {
            chart: {
                type: 'line',
                height: 400
            },
            series: [],
            dataLabels: {
                enabled: false,
            },
            legend: {
                itemMargin: {
                    vertical: 10
                },
            },
            tooltip: {
                x: {
                    show: true,
                    format: 'yyyy-MM-dd HH:mm:ss.fff',
                },
            },
            xaxis: {
                type: 'datetime',
                categories: [],
                labels: {
                    show: true,
                    formatter: timestamp => moment(timestamp).format('YYYY-MM-DD HH:mm:ss.SSS')
                },
                tooltip: {
                    enabled: false,
                }
            },
        },
        tagCount: 5,
        tableName: 'tag',
        url: '${gUrl}',
        host: '${gHost}',
        id: 'user',
        pw: 'password',
        sampleData: {
            name: 'tag',
            date_format: 'YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn',
            values: [
                ["s0", "2020-07-31 11:31:19 719:000:000", 940],
                ["s1", "2020-07-31 11:31:19 719:000:000", 840],
                ["s2", "2020-07-31 11:31:19 719:000:000", 357],
                ["s3", "2020-07-31 11:31:19 719:000:000", 588],
                ["s4", "2020-07-31 11:31:19 719:000:000", 218],
            ]
        },
        appendData: {},
        selectData: {},
        transformDatetime: function(datetime) { // datetime nano to milli
            if (datetime.length === 31) {
                const newDatetime = datetime.slice(0, -12) + '.' + datetime.slice(-11, -8);

                return moment(newDatetime).valueOf();
            }
        }
    };

    const handleData = function() {
        const sIp = g.host.split(':')[0];
        const sTmp = g.id + '@' + sIp + ':' + g.pw;
        const sKey = "Basic " + btoa(sTmp);
        const sHeader = {
            headers: {"Authorization": sKey},
        };
        axios.post(g.url, JSON.parse(JSON.stringify(g.sampleData)), sHeader) // append data
            .then(appendReuslt => {
                g.appendData = appendReuslt.data;

                axios.get(g.url + '?q=select * from ' + g.tableName + ' order by time desc limit ' + g.sampleData.values.length, sHeader) // select data
                    .then(selectResult => {
                        g.selectData = selectResult.data;

                        drawChart();
                    })
                    .catch(err => {
                        console.error('Select Data Axios Error!');
                        console.error(err);
                    });
            })
            .catch(err => {
                console.error('Append Data Axios Error!');
                console.error(err);
            });
    };

    const drawChart = function() {
        if (g.selectData.data.length === 0) {
            alert('No Data for Chart');
        } else {
            g.chartOptions.xaxis.categories = [...new Set(g.selectData.map(data => g.transformDatetime(data.TIME)))];
            g.chartOptions.series = new Array(g.tagCount).fill(0).map((tag, idx) => ({ // mapping data to Apexchart's
                name: 's' + idx,
                data: g.selectData.filter(data => data.NAME === 's' + idx).map(data => data.VALUE)
            }));

            new ApexCharts(document.querySelector('#chart'), g.chartOptions).render(); // draw chart
        }
    };

    const ajaxTest = function() {
        const sIp = g.host.split(':')[0];
        const sKey = "Basic " + btoa(g.id + '@' + sIp + ':' + g.pw);
        const sHeader = {
            headers: {"Authorization": sKey},
        };
        axios.get(g.url + '?q=select * from ' + g.tableName + ' limit ' + g.sampleData.values.length, sHeader)
            .then(result => {
                console.log('select result');
                console.log(result);
            })
            .catch(err => {
                console.error('select result axios error');
                console.error(err);
            });

        // axios.post(g.url, JSON.parse(JSON.stringify(g.sampleData)))
        //     .then(result => {
        //         console.log('append result');
        //         console.log(result);
        //     })
        //     .catch(err => {
        //         console.error('append result axios error');
        //         console.error(err);
        //     });
    };

    const init = function() {
        handleData();
        // ajaxTest();
    };

    init(); // main function
</script>
</body>
</html>`;

export {
    gMenus,
    gTabB,
    gQuery,
    gEdition,
    gUsage,
    gGenerate,
    gAppend,
    gDraw,
    gChartSource
};