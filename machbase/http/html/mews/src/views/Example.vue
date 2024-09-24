<template>
    <div class="Example">
        <Info :gInfo="info" />

        <div class="cardWrap start col">
            <div class="exampleStepper">
                <v-stepper :value="step">
                    <v-stepper-header>
                        <template v-for="(step, idx) in steps">
                            <v-stepper-step
                                :key="step"
                                :complete="step > idx + 1"
                                :step="idx + 1"
                                @click="handleChangeStep(idx + 1)">
                                {{ step }}
                            </v-stepper-step>
                            <v-divider v-if="idx + 1 !== steps.length" :key="idx"></v-divider>
                        </template>
                    </v-stepper-header>
                </v-stepper>
            </div>
    
            <v-card class="exampleCard" flat>
                <div>
                    <div class="exampleCardFirst" v-if="step === 1">
                        <div class="between">
                            <v-text-field
                                v-model="dataValue"
                                label="Amount of Data Input (/sec)"
                                outlined
                                readonly>
                            </v-text-field>
                            <v-text-field
                                v-model="timeValue"
                                label="Data Generation Time (sec)"
                                outlined
                                readonly>
                            </v-text-field>
                            <v-btn
                                color="#3852f3"
                                height="56px"
                                dark
                                depressed
                                @click="handleGenerateData">
                                <span v-if="!loading">Generate</span>

                                <v-progress-circular v-else indeterminate></v-progress-circular>
                            </v-btn>
                        </div>

                        <v-alert
                            v-show="generateErrorMessage !== ''"
                            outlined
                            type="error">{{ generateErrorMessage }}</v-alert>

                        <v-data-table
                            v-show="generated"
                            style="margin-top: 50px;"
                            :items-per-page="-1"
                            :headers="tableHeader"
                            :items="tableItems"></v-data-table>
                    </div>
    
                    <div v-else-if="step === 2">
                        <v-card-text v-show="data !== ''">
                            <span class="exampleCardCopy copy" @click="copy(cardText)">{{ copied ? 'Copied' : 'Copy' }}</span>
                            <div class="exampleCardText">
                                {{ cardText }}
                            </div>
                        </v-card-text>
                    </div>
    
                    <div class="center col" v-else-if="step === 3">
                        <template v-show="!deleted">
                            <v-radio-group
                                v-model="mode"
                                row>
                                <v-radio
                                    label="Source"
                                    value="source"></v-radio>
                                <v-radio
                                    label="Chart"
                                    value="chart"></v-radio>
                            </v-radio-group>

                            <div v-if="mode === 'chart'">
                                <VueApexCharts
                                    width="1000"
                                    type="line"
                                    :options="chartOptions"
                                    :series="series" />
                                
                                <v-alert
                                    v-show="deleteErrorMessage !== ''"
                                    outlined
                                    type="error">
                                    {{ deleteErrorMessage }}
                                </v-alert>
                            </div>
    
                            <div v-else-if="mode === 'source'" class="exampleCodeWrap">
                                <span class="copy" @click="copy(gChartSource)">{{ copied ? 'Copied' : 'Copy' }}</span>
                                <Prism language="javascript" :code="gChartSource" />
                            </div>
                            
                        </template>
                    </div>
    
                    <div class="btnWrap between" v-show="generated">
                        
                        <span v-show="step === 1"></span>
                        <v-btn
                            v-for="(item, idx) in steps"
                            v-show="step === idx || step === idx + 2"
                            :key="idx"
                            color="#3852f3"
                            outlined
                            @click="handleChangeStep(idx + 1)">{{ item }}</v-btn>
                        <v-btn
                            v-show="step === 3"
                            color="#3852f3"
                            dark
                            depressed
                            @click="handleEmptyTable">Empty Table</v-btn>
                    </div>
                </div>
            </v-card>
        </div>

 
    </div>
</template>

<script>
import moment from 'moment';
import VueApexCharts from 'vue-apexcharts';
import Prism from '@/plugins/prism';
import viewMixin from '@/mixins/view';
import { Info } from '@/components';
import { gGenerate, gAppend, gDraw, gChartSource } from '@/assets/ts/config';

export default {
    name: 'Example',
    mixins: [viewMixin],
    components: {
        Info,
        Prism,
        VueApexCharts,
    },
    computed: {
        info() {
            let sResult = {};

            if (this.step === 1) {
                sResult = gGenerate;
            } else if (this.step === 2) {
                sResult = gAppend;
            } else if (this.step === 3) {
                sResult = gDraw;
            }

            return sResult;
        },
        series() {
            return this.chartData.length ? this.chartData : [{ name: 'default', data: new Array(this.dataValue * this.timeValue).fill(0) }]
        },
        cardText() {
            return `curl -X POST -H "Content-Type: application/json" "${this.gUrl}" -d '${JSON.stringify(this.data)}';`;
        }
    },
    methods: {
        handleChangeStep(aStep) {
            if (this.step >= aStep) {
                this.step = aStep;

                document.querySelector('.topBtn').click();
            } else {
                this.handleClickBtn('next');
            }
        },
        handleClickBtn(aStr) {
            if (aStr === 'prev') {
                if (this.step > 1) {
                    this.step--;

                    document.querySelector('.topBtn').click();
                }
            } else if (aStr === 'next') {
                if (!this.generated || this.generateErrorMessage !== '') {
                    alert('Data is not generated yet');
                } else {
                    if (this.step === 1) {
                        this.step++;
                    } else if (this.step === 2) {
                        this.handleDrawChart();
                        this.step++;
                    }

                    document.querySelector('.topBtn').click();
                }
            }
        },
        handleResetData() {
            this.step = 1;
            this.data = '';
            this.mode = 'chart';
            this.loading = false;
            this.copied = false;
            this.generated = false;
            this.generateErrorMessage = '';
            this.deleted = false;
            this.deleteErrorMessage = '';
            this.tableItems = [];
            this.chartData = [];
            this.chartOptions = {};
        },
        async handleCreateTable() {
            const sResult = (await this.request(`create tagdata table TAG (name varchar(50) primary key, time datetime basetime, value double summarized)`)).data;
        },
        async handleEmptyTable() {
            const sText = 'Are you sure that delete all data in table?';

            if (confirm(sText)) {
                const sResult = (await this.request(`delete from tag`)).data;

                if (sResult.Error) {
                    this.deleteErrorMessage = sResult.Error;
                } else {
                    this.deleted = true;
                    this.handleResetData();
                }
            }
        },
        handleDrawChart() {
            this.chartData = new Array(this.gTagCount).fill(0).map((tag, idx) => ({
                name: `s${idx}`,
                data: this.tableItems.filter(item => item.NAME === `s${idx}`).map(item => Number(item.VALUE)).reverse()
            }));
        },
        async handleAppendData() {
            this.loading = true;

            const sAppendResult = (await this.request('', JSON.parse(JSON.stringify(this.data)))).data; // only use double quote "" in JSON
            const sSelectResult = (await this.request(`select * from tag order by time desc limit ${this.dataValue * this.timeValue * this.gTagCount}`)).data;

            this.loading = false;

            if (sSelectResult.Error) {
                this.tableItems = [];
                this.generateErrorMessage = sSelectResult.Error;
            } else {
                this.tableItems = sSelectResult.sort((a, b) => Number(a.NAME[1]) - Number(b.NAME[1]));
                this.generated = this.tableItems.length > 0 ? true : false;
                this.generateErrorMessage = '';
            }
        },
        async handleGenerateData() {
            if (this.dataValue !== '' && this.timeValue !== '') {
                await (() => {
                    return new Promise((resolve, reject) => {
                        const sBasetime = moment().subtract(6, 'hours').startOf().valueOf(); // now - 6
                        const sAvg = parseInt(1 / this.dataValue * 10**3); // ms
                        const sArr = [];
                        const sObj = {
                            name: 'tag',
                            date_format: 'YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn',
                            values: []
                        };

                        let sTimeData = [];
        
                        for (let i = 0; i < this.timeValue; i++) {
                            for (let j = 0; j < this.dataValue; j++) {
                                const sCalcDate = moment(Number(sBasetime) + 10**3 * i + sAvg * j).format('YYYY-MM-DD HH:mm:ss SSS');
                                
                                for (let k = 0; k < this.gTagCount; k++) {
                                    const sRandomValue = this.random(); // 1 ~ 1024
            
                                    sArr.push([`s${k}`, `${sCalcDate}:000:000`, sRandomValue]);
                                }
                            }
                        }
            
                        sObj.values = sArr;
                        sTimeData = [...new Set(sArr.map(item => this.slice(item[1])))];
            
                        this.data = sObj;
                        this.chartOptions = {
                            chart: {
                                id: 'exampleChart'
                            },
                            dataLabels: {
                                enabled: false,
                            },
                            legend: {
                                itemMargin: {
                                    vertical: 20
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
                                categories: sTimeData,
                                labels: {
                                    show: true,
                                    formatter: timestamp => moment(timestamp).format('YYYY-MM-DD HH:mm:ss.SSS')
                                },
                                tooltip: {
                                    enabled: false,
                                }
                            }
                        }

                        resolve();
                    });
                })();

                await this.handleAppendData();
            }
        }
    },
    data() {
        return {
            step: 1,
            steps: ['Generate data', 'Append data', 'Draw chart'],
            dataValue: 5,
            timeValue: 10,
            tableHeader: ['NAME', 'TIME', 'VALUE'].map(header => ({
                text: header,
                value: header,
                align: 'start'
            })),
            tableItems: [],
            data: '',
            mode: 'chart',
            loading: false,
            copied: false,
            generated: false,
            generateErrorMessage: '',
            deleted: false,
            deleteErrorMessage: '',
            chartData: [],
            chartOptions: {},
            gChartSource,
            gGenerate,
            gAppend,
            gDraw,
        }
    },
    mounted() {
        this.handleCreateTable();
    }
}
</script>

<style lang="scss">
.Example {
    .btnWrap {
        height: 40px;
        margin: 10px;
    }

    .v {
        &-card {
            margin-bottom: 10px;
        }

        &-stepper {
            &__step {
                cursor: pointer;
            }
        }
    }

    .cardWrap {
        padding: 0;

        .exampleStepper {
            width: 100%;
            margin-bottom: 10px;
        }

        .exampleCard {
            width: 100%;
            min-height: 100%;

            .v {
                &-alert {
                    width: 100%;
                    margin-top: 20px;
                }
            }

            .exampleCardFirst {
                .v {
                    &-input__slot {
                        margin: 0;
                    }

                    &-text-field__details {
                        display: none;
                    }

                    &-select,
                    &-text-field {
                        margin-right: 10px;
                    }

                    &-btn {
                        &__content {
                            min-height: 56px;
                        }
                    }
                }
            }

            .exampleCardTitle {
                font-weight: bold;
                display: block;
                margin-bottom: 5px;
            }

            .exampleCardText {
                background-color: #f0f0f0;
                padding: 20px;
                margin-bottom: 20px;
                word-break: break-all;
                overflow-x: hidden;
            }
            
            .exampleCardCopy {
                top: 17px;
                right: 6px;
                background-color: #f0f0f0;
            }

            .exampleCodeWrap {
                width: 100%;
                position: relative;
                margin: 20px 0 10px 0;
            }
        }
    }
}
</style>