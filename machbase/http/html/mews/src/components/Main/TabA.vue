<template>
    <div class="TabA">
        <v-tabs color="#3852f3" show-arrows>

            <v-tab
                v-for="(data, idx) in gData"
                :key="idx"
                @change="handleChangeTabA(data.host)">
                {{ data.host }}
                {{ handleGetGroupName(data.host) }}
            </v-tab>
        </v-tabs>


        <v-card class="between" flat>
            <div class="tabAInfoWrapper">
                <div v-for="key in Object.keys(currentInfo)"
                    v-show="key !== 'name' && key !== 'host'"
                    :key="key"
                    class="itemWrapper">
                    <v-chip color="#3852f3" dark>
                        <v-icon></v-icon>
                        {{ key | uppercase }}
                    </v-chip>
                    
                    <div v-if="typeof currentInfo[key] === 'object'" class="spanWrapper">
                        <div v-for="aKey in Object.keys(currentInfo[key])" :key="aKey">
                            <span>{{ aKey }}</span>
                            <span class="colon">:</span>
                            <span>{{ currentInfo[key][aKey] ? comma(currentInfo[key][aKey]) : 0 }}</span>
                            <span class="unit">{{ unit[key] ? `(${unit[key]})` : '' }}</span>
                        </div>
                       
                    </div>

                    <div v-else class="spanWrapper">
                        <div>
                            <span>{{ currentInfo[key] }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="tabAChartWrapper start col">
                <span class="tabAChartMessage">* Refresh every {{ gRefresh / 1000 }} sec</span>

                <VueApexCharts
                    width="700px"
                    height="200px"
                    type="line"
                    :options="fetchChartOptions"
                    :series="[{ data: (seriesObj[currentInfo.name] ? seriesObj[currentInfo.name].fetchSeries : []) }]" />
                
                <VueApexCharts
                    width="700px"
                    height="200px"
                    type="line"
                    :options="appendChartOptions"
                    :series="[{ data: (seriesObj[currentInfo.name] ? seriesObj[currentInfo.name].appendSeries : []) }]" />
            </div>
            
        </v-card>
    </div>
</template>

<script>
import moment from 'moment';
import VueApexCharts from 'vue-apexcharts';
import { eventBus } from '@/main.ts';


const makeCategories = function(aCount, aDiff) {
    const sNow = moment();

    return new Array(aCount).fill(0).map((item, idx) => sNow.subtract(aDiff * idx, 'seconds').valueOf()).reverse();
}

const makeChartOptions = function(aTypeNum, aCategories, aIdx=0) {
    const sChartType = aTypeNum === 0 ? 'fetch' : 'append';

    return {
        chart: {
            id: `${sChartType}chart_${aIdx}`,
            toolbar: {
                show: false
            },
            zoom: {
                enabled: false
            },
        },
        title: {
            text: `${sChartType[0].toUpperCase()}${sChartType.slice(1)}ed Row Count`,
            align: 'left'
        },
        animations: {
            enabled: true,
            easing: 'linear',
            dynamicAnimation: {
                speed: 1000
            }
        },
        legend: {
            show: false,
        },
        noData: {
            text: ''
        },
        tooltip: {
            x: {
                formatter: aTimestamp => (moment(aTimestamp).format('YYYY-MM-DD HH:mm:ss'))
            },
            y: {
                title: {
                    formatter: () => ('count')
                },
            }
        },
        stroke: {
            colors: ['#3852f3'],
            curve: 'smooth'
        },
        xaxis: {
            type: 'datetime',
            categories: aCategories ? aCategories : [],
            labels: {
                formatter: aTimestamp => (moment(aTimestamp).format('HH:mm:ss'))
            },
            tooltip: {
                enabled: false,
            }
        },
        yaxis: {
            // min: 0,
            tickAmount: 5,
            labels: {
                formatter: (value) => (parseInt(value))
            }
        }
    }
}

export default {
    name: 'TabA',
    props: [
        'gData',
        'gRefresh',
        'gSeriesData',
        'gStackCount',
    ],
    components: {
        VueApexCharts
    },
    computed: {
        currentInfo() {
            const sHost = this.currentTab;
            
            return sHost === '' ? {} : this.gData.filter(data => data.host === sHost)[0];
        },
        defaultArr() {
            return new Array(this.gStackCount).fill(0);
        },
        unit() {
            return {
                memory: 'GB',
                'excuted queries': `last ${this.gRefresh / 1000} sec`,
                'data flow': `last ${this.gRefresh / 1000} sec`,
            }
        },
    },
    methods: {
        handleRefreshChart() {
            const sCategories = makeCategories(this.gStackCount, this.gRefresh / 1000);

            this.seriesObj = this.gSeriesData;

            this.gData.length && this.gData.forEach(() => {
                this.fetchChartOptions = makeChartOptions(0, sCategories);
                this.appendChartOptions = makeChartOptions(1, sCategories);
            });
        },
        handleChangeTabA(aHost) {
            if (aHost) {
                this.currentTab = aHost;
                eventBus.$emit('currentTab', aHost);
            }
        },
        handleGetGroupName(aHost) {
            const sGroup = this.handleGetNodeList('Node-Name', aHost)[0]['Group-Name'];

            let sStr = '';

            if (sGroup) {
                if (sGroup === 'Broker') {
                    sStr = '(B)';
                } else {
                    sStr = '(G)';
                }
            }

            return sStr;
        }
    },
    data() {
        return {
            currentTab: '',
            seriesObj: {},
            fetchChartOptions: makeChartOptions(0),
            appendChartOptions: makeChartOptions(1),
        }
    },
    mounted() {
        this.seriesObj = this.gSeriesData;

        const sTimer = setInterval(() => {
            this.handleRefreshChart();
            
        }, this.gRefresh);
    }
}
</script>

<style lang="scss">
.TabA {
    min-height: 500px;
    background-color: white;

    .between {
        flex-wrap: wrap;
    }

    .itemWrapper {
        margin-bottom: 10px;
    
        &:last-child {
            margin: 0;
        }
    }
    
    .spanWrapper {
        margin: 10px 0 0 10px;
    }
    
    .tabAInfoWrapper {
        width: 40%;
        min-width: 400px;
    }
    
    .tabAChartWrapper {
        width: 60%;
        min-width: 700px;
        position: relative;

        .tabAChartMessage {
            font-size: 0.75rem;
            color: #00000060;
            position: absolute;
            top: -20px;
            right: 30px;
        }
    }
}
</style>