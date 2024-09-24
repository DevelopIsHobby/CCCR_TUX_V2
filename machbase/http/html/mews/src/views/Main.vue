<template>
    <div class="Main">
        <div class="mainWrap">
            <v-alert
                v-show="isErrored"
                outlined
                type="error">DB Connection Error!</v-alert>                

            <MainTabA
                :gData="container"
                :gError="isErrored"
                :gRefresh="refresh"
                :gSeriesData="seriesData"
                :gStackCount="stackCount"/>
                
            <MainTabB :gIsCluster="isCluster" />

        </div>

    </div>
</template>

<script>
import axios from 'axios';
import { MainTabA, MainTabB } from '@/components';
import viewMixin from '@/mixins/view';

export default {
    name: 'Main',
    mixins: [viewMixin],
    components: {
        MainTabA,
        MainTabB,
    },
    computed: {
        container() {
            let sResult = [];

            if (this.isFetched) {
                if (this.isCluster) { // Cluster Edition
                    sResult = this.handleSortList(this.editionData.map(data => {
                        const sHost = data.HOSTNAME;

                        return {
                            name: sHost,
                            host: sHost,
                            version: {
                                DB: `${data.BINARY_DB_MAJOR_VERSION}.${data.BINARY_DB_MINOR_VERSION}.${data.BINARY_SIGNATURE.split('-')[0]}`,
                                Meta: `${data.BINARY_META_MAJOR_VERSION}.${data.BINARY_META_MINOR_VERSION}`,
                                'Comm. Level': `${data.BINARY_CM_MAJOR_VERSION}.${data.BINARY_CM_MINOR_VERSION}`,
                                'DB Created At': data.FILE_CREATE_TIME,
                            },
                            edition: data.EDITION,
                            memory: {
                                'Current Usage': this.gigabyte(this.memoryData.cur),
                                'Process Max Size': this.gigabyte(this.memoryData.max[sHost])
                            },
                            'excuted queries': {
                                'Success Count': this.data[sHost].successValue,
                                'Failure Count': this.data[sHost].failureValue,
                            },
                            'data flow': {
                                'Fetched Row Count': this.data[sHost].fetchedValue,
                                'Appended Row Count': this.data[sHost].appendedValue
                            }
                        }
                    }))
                } else { // Fog Edition
                    sResult = [{
                        name: 0,
                        host: this.gHost,
                        version: {
                            DB: `${this.editionData[0].BINARY_DB_MAJOR_VERSION}.${this.editionData[0].BINARY_DB_MINOR_VERSION}.${this.editionData[0].BINARY_SIGNATURE.split('-')[0]}`,
                            Meta: `${this.editionData[0].BINARY_META_MAJOR_VERSION}.${this.editionData[0].BINARY_META_MINOR_VERSION}`,
                            'Comm. Level': `${this.editionData[0].BINARY_CM_MAJOR_VERSION}.${this.editionData[0].BINARY_CM_MINOR_VERSION}`,
                            'DB Created Time': this.editionData[0].FILE_CREATE_TIME,
                        },
                        edition: this.editionData[0].EDITION,
                        memory: {
                            'Current Usage': this.gigabyte(this.memoryData.cur),
                            'Process Max Size': this.gigabyte(this.memoryData.max)
                        },
                        'excuted queries': {
                            'Success Count': this.data[0].successValue,
                            'Failure Count': this.data[0].failureValue,
                        },
                        'data flow': {
                            'Fetched Row Count': this.data[0].fetchedValue,
                            'Appended Row Count': this.data[0].appendedValue,
                        }
                    }];
                }
            }

            return sResult;
        }
    },
    methods: {
        handleSetSubtract() {
            this.editionData.forEach(data => {
                const sHost = data.HOSTNAME ? data.HOSTNAME : 0;

                const sSuccessCount = Number(this.sysData[sHost].EXECUTE_SUCCESS);
                const sFailureCount = Number(this.sysData[sHost].EXECUTE_FAILURE);
                const sFetchedCount = Number(this.sysData[sHost].CURSOR_FETCH_CNT);
                const sAppendedCount = Number(this.sysData[sHost].APPEND_DATA_SUCCESS);

                if (!this.data[sHost]) {
                    this.data[sHost] = JSON.parse(JSON.stringify(this.dataObj));
                    this.seriesData[sHost] = JSON.parse(JSON.stringify(this.seriesDataObj));

                    this.data[sHost].successCount = sSuccessCount;
                    this.data[sHost].failureCount = sFailureCount;
                    this.data[sHost].fetchedCount = sFetchedCount;
                    this.data[sHost].appendedCount = sAppendedCount;
                } else {
                    this.data[sHost].successValue = sSuccessCount - this.data[sHost].successCount;
                    this.data[sHost].failureValue = sFailureCount - this.data[sHost].failureCount;
                    this.data[sHost].fetchedValue = sFetchedCount - this.data[sHost].fetchedCount;
                    this.data[sHost].appendedValue = sAppendedCount - this.data[sHost].appendedCount;

                    this.data[sHost].successCount = sSuccessCount;
                    this.data[sHost].failureCount = sFailureCount;
                    this.data[sHost].fetchedCount = sFetchedCount;
                    this.data[sHost].appendedCount = sAppendedCount;

                    if (this.seriesData[sHost].fetchSeries.length >= this.stackCount) {
                        this.seriesData[sHost].fetchSeries.shift();
                        this.seriesData[sHost].appendSeries.shift();
                    } else {
                        this.seriesData[sHost].fetchSeries = new Array(this.stackCount).fill(0).concat(this.seriesData[sHost].fetchSeries).slice(this.stackCount * -1 + 1);
                        this.seriesData[sHost].appendSeries = new Array(this.stackCount).fill(0).concat(this.seriesData[sHost].appendSeries).slice(this.stackCount * -1 + 1);
                    }
                    
                    this.seriesData[sHost].fetchSeries.push(this.data[sHost].fetchedValue);
                    this.seriesData[sHost].appendSeries.push(this.data[sHost].appendedValue);
                }
            });
        },
        handleSortList(aArr) {
            const sBroker = aArr.filter(item => item.name === 'Broker').sort((a, b) => (a.name.toUpperCase().localeCompare(b.name.toUpperCase())));
            const sNonBroker = aArr.filter(item => item.name !== 'Broker').sort((a, b) => (a.name.toUpperCase().localeCompare(b.name.toUpperCase())));

            return sBroker.concat(sNonBroker);
        },
        handleGetGroup(aHostName) {
            let sResult = '';

            if (this.isCluster) {
                sResult = this.gNodeList.filter(node => node['Node-Name'] === aHostName)[0]['Group-Name'];
            }

            return sResult;
        },
        handleMapMemData(aArr) {
            let sResult = {};
            
            if (aArr.length) { // array
                const sMap = new Map();

                aArr.forEach(arr => {
                    sMap.set(arr.HOSTNAME, Number(arr.VALUE));
                });

                sResult = Object.fromEntries(sMap);
            }

            return sResult;
        },
        handleMapSysData(aArr) {
            let sResult = {};

            if (aArr.length) { // array
                const sMap = new Map();
    
                aArr.forEach(arr => {
                    const sHostName = arr.HOSTNAME;
                    const sValue = sMap.has(sHostName) ? sMap.get(sHostName) : {};
    
                    sValue[arr.NAME] = Number(arr.VALUE);
                    sMap.set(sHostName, sValue);
                });
    
                sResult = Object.fromEntries(sMap);
            }
            
            return sResult;
        },
        async handleGetCluterData() {
            try {
                const [sMemoryCur] = (await this.request(`select SUM(USAGE) FROM V$SYSMEM`)).data;
                const sMemoryMax = (await this.request(`select HOSTNAME, VALUE from V$PROPERTY where NAME = 'PROCESS_MAX_SIZE'`)).data;
                const sSystem = (await this.request(`select HOSTNAME, NAME, VALUE from V$SYSSTAT where NAME = 'EXECUTE_SUCCESS' or NAME = 'EXECUTE_FAILURE' or NAME = 'CURSOR_FETCH_CNT' or NAME = 'APPEND_DATA_SUCCESS'`)).data;

                const sUrl = `http://${this.clusterData[0].HOST.split(':')[0]}:${this.clusterData[0].COORD_HTTP_ADMIN_PORT}`;
                const sNodeList = (await axios.get(`${sUrl}/List-Node`)).data;
                
                if (sNodeList['Node-List']) {
                    this.$store.commit('setNodeList', sNodeList['Node-List']);
                }

                this.memoryData = {
                    cur: Number(sMemoryCur['SUM(USAGE)']),
                    max: this.handleMapMemData(sMemoryMax)
                };
                
                this.sysData = sSystem && this.handleMapSysData(sSystem);
                this.handleSetSubtract();
            } catch (err) {
                console.error('Get Cluster Info Axios Error!');
                console.error(err);
            }
        },
        async handleGetNonClusterData() {
            const [sMemoryCur] = (await this.request(`SELECT SUM(USAGE) FROM V$SYSMEM`)).data;
            const [sMemoryMax] = (await this.request(`SELECT VALUE FROM V$PROPERTY WHERE NAME = 'PROCESS_MAX_SIZE'`)).data;
            const sSystem = (await this.request(`select NAME, VALUE from V$SYSSTAT where NAME = 'EXECUTE_SUCCESS' or NAME = 'EXECUTE_FAILURE' or NAME = 'CURSOR_FETCH_CNT' or NAME = 'APPEND_DATA_SUCCESS'`)).data;
            const sObj = {};

            this.memoryData = {
                cur: Number(sMemoryCur['SUM(USAGE)']),
                max: Number(sMemoryMax['VALUE'])
            };

            sSystem.forEach(item => {
                sObj[item.NAME] = Number(item.VALUE);
            });

            this.sysData[0] = sObj;
            this.handleSetSubtract();
        },
        async handleGetInfo() {
            this.clusterData = (await this.request(`select * from V$NODE_STATUS`)).data;
            this.editionData = (await this.request(`select * from V$VERSION`)).data;
            if (!this.clusterData) {
                this.isErrored = true;
            } else {
                this.isErrored = false;
                
                if (!this.clusterData.Error) { // Cluster Edition
                    await this.handleGetCluterData();
                    this.isCluster = true;
                } else { // Fog Edition
                    await this.handleGetNonClusterData();
                }

                this.isFetched = true;
            }
        }
    },
    data() {
        return {
            isErrored: false,
            isFetched: false,
            isCluster: false,
            editionData: {},
            memoryData: {},
            sysData: {},
            clusterData: {},
            clusterInfo: {},
            dataObj: {
                successCount: 0,
                successValue: 0,
                failureCount: 0,
                failureValue: 0,
                fetchedCount: 0,
                fetchedValue: 0,
                appendedCount: 0,
                appendedValue: 0,
            },
            seriesDataObj: {
                fetchSeries: [],
                appendSeries: [],
            },
            data: {},
            seriesData: {},
            refresh: 5000,
            stackCount: 20,
        }
    },
    async mounted() {
        let sTimer;

        try {
            await this.handleGetInfo();
            
            sTimer = await setInterval(() => {
                if (this.isCluster) {
                    this.handleGetCluterData();
                } else {
                    this.handleGetNonClusterData();
                }
            }, this.refresh);

            if (this.isErrored) {
                clearInterval(sTimer);
            }
        } catch (err) {
            console.log('Getting info axios error!');
            console.log(err);
            clearInterval(sTimer);
        }

        document.querySelector('.TabA .v-tab').click(); // click trigger
    }
}
</script>

<style lang="scss">
.Main {
    .mainWrap {
        // height: calc(100vh - 310px);
        min-height: 300px;

        & > div {
            margin-bottom: 10px;

            &:last-child {
                margin: 0;
            }
        }
    }
}

.infoItemTitleWrap { width: 40% }
.infoItemTableWrap { width: 60% }
</style>