import Vue from 'vue';
import Vuex, { StoreOptions } from 'vuex';

Vue.use(Vuex);

interface State {
    gCurrentPage: string;
    gNodeList: object[];
    gAuth: boolean;
    gId: string;
    gPw: string;
}

const store: StoreOptions<State> = {
    state: {
        gCurrentPage: '',
        gNodeList: [],
        gAuth: false,
        gId: 'noAuth',
        gPw: 'noAuth'
    },
    mutations: {
        setCurrentPage(state, aStr: string) {
            state.gCurrentPage = aStr;
        },
        setNodeList(state, aArr: object[]) {
            state.gNodeList = aArr;
        },
        setAuth(state, aBoo: boolean){
            state.gAuth = aBoo;
        },
        setId(state, aId: string){
            state.gId = aId;
        },
        setPw(state, aPw: string){
            state.gPw = aPw;
        }
    },
    actions: {
    },
    modules: {
    }
};

export default new Vuex.Store(store);