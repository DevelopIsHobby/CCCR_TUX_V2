import axios from 'axios';
import moment from 'moment';
import { mapState, mapMutations } from 'vuex';
import '@/plugins/clipboard';
import * as gMethods from '@/assets/ts/common';

const gTagCount = 5;
const gHost = window.location.host;
const gUrl = `http://${gHost}/machbase`;

const mixin = {
    computed: {
        ...mapState([
            'gNodeList',
            'gCurrentPage',
        ])
    },
    filters: {
        uppercase(aStr) {
            return aStr.toUpperCase();
        },
        camelcase(aStr) {
            return aStr.split(' ').map(str => str[0].toUpperCase() + str.slice(1)).join(' ');
        },
    },
    methods: {
        async request(aQuery, aParams) {
            const sUrl = aQuery === '' ? gUrl : `${gUrl}?q=${aQuery}`;
            const sIp = gHost.split(':')[0];
            const sKey = "Basic " + btoa(`${this.$store.state.gId}@${sIp}:${this.$store.state.gPw}`);
            const sHeader = {
                headers: {"Authorization": sKey},
            };
            let sResult = {};

            try {
                if (aParams) { // POST method
                    sResult = await axios.post(sUrl, aParams, sHeader);
                } else { // GET method
                    sResult = await axios.get(sUrl, sHeader);
                }
                if (sResult.data.error_code !== 0) {
                    sResult.data = { Error: sResult.data.error_message };
                } else if (!aParams) { // GET method
                    sResult.data = sResult.data.data;
                }
            } catch (err) {
                console.log('DB request error!');
                console.log(err);
                sResult.data = { Error: err };
            }

            return sResult;
        },
        slice(aNanoTimestamp) {
            return moment(`${aNanoTimestamp.slice(0, -12)}.${aNanoTimestamp.slice(-11, -8)}`).valueOf();
        },
        copy(aStr) {
            this.$copyText(aStr).then(() => {
                this.copied = true;
                alert('Copied to Clipboard!');

                setTimeout(() => {
                    this.copied = false;
                }, 3000);
            });
        },
        isEmptyObj(aObj) {
            return Object.keys(aObj).length === 0 && aObj.constructor === Object;
        },
        handleGetNodeList(aKey, aValue) { // filter nodeList
            const sNodeList = [... new Set(this.gNodeList.map(node => node[aKey]))];

            let sResult;

            if (sNodeList.indexOf(aValue) >= 0) { // exist
                sResult = this.gNodeList.filter(node => node[aKey] === aValue);
            } else {
                sResult = [{}];
            }

            return sResult;
        },
        ...gMethods,
        ...mapMutations([
            'setCurrentPage'
        ])
    },
    data() {
        return {
            gUrl,
            gHost,
            gTagCount,
            gCopied: false,
        }
    },
}

export default mixin;