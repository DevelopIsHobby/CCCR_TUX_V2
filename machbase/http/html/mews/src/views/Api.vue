<template>
    <div class="Api">
        <Info :gInfo="gUsage" />

        <div class="apiWrap">
            <div class="between">
                
                <v-text-field
                    v-model="textInput"
                    :error="inputError"
                    :disabled="disabled"
                    label="Test Query"
                    outlined
                    clearable
                    flat>
                </v-text-field>
                
                <v-btn
                    color="#3852f3"
                    style="margin-right: 10px;"
                    height="56px"
                    outlined
                    @click="copy(textInput)">
                    Copy
                </v-btn>
                
                <v-btn
                    class="center"
                    color="#3852f3"
                    height="56px"
                    dark
                    depressed
                    @click="handleRequest">
                    <span v-if="!requested">Request</span>

                    <v-progress-circular v-else indeterminate></v-progress-circular>
                </v-btn>
            </div>

            <v-alert
                v-show="result !== ''"
                outlined
                :type="axiosError ? 'error' : 'success'">
                <h2>Test Result</h2>
                <p>{{ result }}</p>
            </v-alert>

            <div class="panelWrap"> 
                <v-card
                    v-for="item in gQuery"
                    :key="item.name"
                    title="Click to copy"
                    flat
                    outlined
                    @click="handleClickPanel(item.name)">
                    <v-card-title class="between">
                        <span>{{ item.name }}</span>
                        <span>
                            <v-icon small>mdi-content-copy</v-icon>
                        </span>
                    </v-card-title>
                    <v-card-subtitle>{{ item.query }}</v-card-subtitle>
                </v-card>
            </div>
        </div>
        
      
    </div>
</template>

<script>
import axios from 'axios';
import { Info } from '@/components';
import { gQuery, gUsage } from '@/assets/ts/config';
import viewMixin from '@/mixins/view';

export default {
    name: 'Api',
    components: {
        Info,
    },
    mixins: [viewMixin],
    methods: {
        handleRefresh() {
            this.urlInput = {
                get: '',
                post: '',
            };
            this.selected = {};
            this.textInput = '';
            this.result = '';
            this.disabled = false;
            this.requested = false;
            this.inputError = false;
            this.axiosError = false;
        },
        async handleRequest() {
            let sResult = {};

            this.result = '';
            
            if (this.textInput === '') {
                this.inputError = true;
                return;
            } else {
                this.inputError = false;
                this.requested = true;
            }
            try {
                if (this.selected.method === 'get') {
                    const sIp = this.gHost.split(':')[0];
                    const sKey = "Basic " + btoa(`${this.$store.state.gId}@${sIp}:${this.$store.state.gPw}`);
                    const sHeader = {
                        headers: {"Authorization": sKey},
                    };
                    sResult = (await axios.get(this.textInput, sHeader)).data;
                    console.log(sResult);
                } else { // post
                    sResult = (await this.request('', this.selected.data)).data;
                    console.log(sResult);
                }
            } catch (err) {
                console.log('Axios Api Request Error!');
                console.error(err);
                sResult.Error = err;
                
            }
            this.requested = false;

            if (sResult.error_code !== 0) {
                this.axiosError = true;
            } else {
                this.axiosError = false;
            }

            this.result = sResult;
        },
        async handleClickPanel(aQuery) {
            this.result = '';
            this.requested = false;
            this.selected = gQuery.filter(item => item.name === aQuery)[0];
            this.disabled = this.selected.method === 'post' ? true : false;
            this.textInput = `${this.urlInput[this.selected.method]}${this.selected.query}`;

            document.querySelector('.topBtn').click();
            
            this.copy(this.urlInput + this.textInput);
        }
    },
    data() {
        return {
            urlInput: {
                get: '',
                post: '',
            },
            textInput: '',
            result: '',
            selected: {},
            disabled: false,
            requested: false,
            inputError: false,
            axiosError: false,
            gQuery,
            gUsage
        }
    },
    mounted() {
        this.handleRefresh();
        this.urlInput = {
            get: `${this.gUrl}?q=`,
            post: `curl -X POST -H "Content-Type: application/json" "${this.gUrl}" -d `
        };
    }
}
</script>

<style lang="scss">
.apiWrap {
    .between {
        flex-wrap: wrap;
    }

    .v {
        &-alert {
            margin-top: 20px;
        }

        &-input {
            min-width: 200px;

            &__control {
                margin-right: 10px;
            }

            &__slot {
                margin: 0;
            }
        }

        &-text-field {
            &:first-child {
                width: 2%;
            }

            &__details {
                display: none;
            }
        }

        &-expansion {
            &-panels {
                /* margin-top: 20px; */
            }

            &-panel {
                /* border: 1px solid #f0f0f0; */
                background-color: #f0f0f050 !important;

                &:not(:first-child) {
                    margin-top: 10px;
                }

                &-header {
                    font-weight: bold;
                    position: relative;
                    cursor: pointer;

                    &:hover {
                        background-color: #c3c3c350;
                    }
                    &__icon {
                        display: none;
                    }
                }
            }
        }
    }
}

.panelWrap {
    margin-top: 20px;
    padding: 20px;
    border-radius: 4px;
    background-color: white;

    .v {
        &-card {
            padding: 10px;

            &:not(:first-child) {
                margin-top: 10px;
            }

            &:hover {
                background-color: #00000005;
            }
        }
    }
}
</style>