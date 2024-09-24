<template>
    <div class="TabB">
        <v-tabs color="#3852f3" v-model="tab">
            <v-tab
                v-for="tab in gTabB"
                v-show="tab.name !== 'cluster' || gIsCluster"
                :key="tab.name"
                :href="'#tab_' + tab.name"
                @change="handleChangeTabB(tab.name)">{{ tab.name }}</v-tab>

            <v-tab-item
                v-for="tab in gTabB"
                class="tabItem"
                :key="tab.name"
                :value="'tab_' + tab.name">
                <v-card flat>
                    <v-data-table
                        v-if="tab.name !== 'cluster'"
                        :items-per-page="-1"
                        :headers="tableHeader"
                        :items="tableItems"></v-data-table>
                    
                    <div class="center col" v-else><!-- cluster tab -->
                        <v-radio-group
                            v-model="mode"
                            row
                            @change="handleCluster">
                            <v-radio
                                label="Group Mode"
                                value="group"></v-radio>
                            <v-radio
                                label="Deployer Mode"
                                value="deployer"></v-radio>
                        </v-radio-group>

                        <div class="tabBClusterWrap">
                            <div class="tabBClusterCoord">
                                <span class="tabBClusterTitle">Coordinator</span>
                                <div class="tabBClusterItemWrap">
                                    <div class="tabBClusterItem">
                                        <Chip
                                            v-for="item in coordinator"
                                            :key="item['Node-Name']"
                                            :gChipName="item['Node-Name']"
                                            :gChipInfo="item" />
                                    </div>
                                </div>
                            </div>

                            <div class="tabBClusterChildren">
                                <span class="tabBClusterTitle">{{ mode === 'deployer' ? 'Deployer / Nodes' : 'Group' }}</span>

                                <div class="tabBClusterItemWrap">
                                    <div v-for="key in Object.keys(clusterItem)" :key="key">
                                        <div class="tabBClusterItem start">
                                            <Chip
                                                gChipColor="#3852f3"
                                                :gChipName="key"
                                                :gChipInfo="handleGetNodeList('Node-Name', key)[0]" />
                                            
                                            <div class="tabBClusterNodeWrap start">
                                                <template v-if="mode === 'deployer'">
                                                    <Chip
                                                        v-for="item in clusterItem[key]"
                                                        :key="item"
                                                        :gChipName="item"
                                                        :gChipInfo="handleGetNodeList('Node-Name', item)[0]" />
                                                </template>
    
                                                <template v-if="mode === 'group'">
                                                    <Chip
                                                        v-for="item in clusterItem[key]"
                                                        :key="item"
                                                        :gChipName="item"
                                                        :gChipInfo="handleGetNodeList('Node-Name', item)[0]" />
                                                </template>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- cluster tab end -->
                </v-card>
            </v-tab-item>
        </v-tabs>
    </div>
</template>

<script>
import { Chip } from '@/components';
import { gTabB } from '@/assets/ts/config';
import { eventBus } from '@/main.ts';

export default {
    name: 'TabB',
    props: [
        'gIsCluster',
    ],
    components: {
        Chip,
    },
    computed: {
        coordinator() {
            return this.handleGetNodeList('Node-Type', 'coordinator');
        },
    },
    methods: {
        handleCluster() { // make tooltip info obj
            const sMap = new Map();

            if (this.mode === 'deployer') {
                const sBroker = this.handleGetNodeList('Node-Type', 'broker');
                const sDeployer = this.handleGetNodeList('Node-Type', 'deployer');
                const sWarehouse = this.handleGetNodeList('Node-Type', 'warehouse');
    
                sDeployer.forEach(deployer => {
                    sMap.set(deployer['Node-Name'], []);
                });

                sBroker.forEach(broker => {
                    const sParent = broker['Deployer'];
                    const sArr = sMap.get(sParent);

                    sMap.set(sParent, sArr.concat(broker['Node-Name']));
                });
    
                sWarehouse.forEach(warehouse => {
                    const sParent = warehouse['Deployer'];
                    const sArr = sMap.get(sParent);
    
                    sMap.set(sParent, sArr.concat(warehouse['Node-Name']));
                });
            } else {
                const sGroup = [...new Set(this.gNodeList.map(node => node['Group-Name']))];
                
                sGroup.forEach(group => {
                    sMap.set(group, []);
                });

                this.gNodeList.forEach(node => {
                    const sCurrentGroup = node['Group-Name'];
                    const sArr = sMap.get(sCurrentGroup);

                    sMap.set(sCurrentGroup, sArr.concat(node['Node-Name']));
                });

                sMap.delete('Coordinator');
            }
            
            this.clusterItem = Object.fromEntries(sMap);
        },
        async handleChangeTabB(aStr) {
            const sTarget = gTabB.filter(tab => tab.name === aStr)[0];
            const sQuery = `select * from ${sTarget.table}`;

            if (sTarget.name !== 'cluster') { // query, property, memory
                const sResult = (await this.request(sQuery)).data;

                let sHeader = [];
                let sItems = [];

                if (sResult[0] && typeof sResult[0] === 'object') {
                    sItems = sResult;
                    sHeader = sItems[0] && Object.keys(sItems[0]).map(header => ({
                        text: header,
                        value: header,
                        align: 'start',
                        sortable: sTarget.name === 'memory' ? true : false,
                    }));
    
                    if (this.gIsCluster) {
                        sItems = sItems.filter(item => item.HOSTNAME === this.currentTab);
    
                        if (sTarget.name === 'property') {
                            sHeader.forEach(header => (header.text === 'DEFLT') && (header.text = 'DEFAULT'));
                        } else if (sTarget.name === 'memory') {
                            sHeader.forEach(header => (header.text === 'USAGE' || header.text === 'MAX_USAGE') && (header.text = `${header.text} (byte)`));
                            sItems = sItems.map(item => {
                                const sUsage = item['USAGE'];
                                const sMaxUsage = item['MAX_USAGE'];
    
                                return {
                                    ...item,
                                    USAGE: this.fit(this.comma(Number(sUsage))),
                                    MAX_USAGE: this.fit(this.comma(Number(sMaxUsage)))
                                }
                            });
                        }
                    } else {
                        sItems = sItems.filter(item => item.QUERY !== sQuery);
                    }
                }

                this.tableHeader = sHeader;
                this.tableItems = sItems;
            } else { // cluster tab
                this.handleCluster();
            }
        }
    },
    data() {
        return {
            clusterItem: [],
            currentTab: '',
            deployer: {},
            mode: 'group',
            tab: null,
            tableHeader: [],
            tableItems: [],
            gTabB
        }
    },
    created() {
        eventBus.$on('currentTab', result => {
            this.currentTab = result;

            document.querySelector('.TabB .v-tab').click(); // click trigger
        });
    },
}
</script>

<style lang="scss">
.tabBClusterWrap {
    width: 100%;
    border: 1px solid #00000040;
    border-radius: 4px;
    padding: 20px;
    margin-top: 10px;

    .tabBClusterTitle {
        font-size: 1rem;
        font-weight: bold;
        display: inline-block;
    }

    .tabBClusterCoord {
        margin-bottom: 20px;
    }

    .tabBClusterItemWrap {
        flex-wrap: wrap;
        margin-top: 10px;

        .start {
            flex-wrap: wrap;
        }
    }

    .v {
        &-chip {
            margin: 0 5px 5px 0;
        }
    }
}
</style>