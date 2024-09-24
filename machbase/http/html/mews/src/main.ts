import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import mixin from './mixins';
import 'babel-polyfill';
import '@babel/core';


// import './plugins/promise-polyfill'; // IE
// import './plugins/classlist';

import vuetify from './plugins/vuetify';
import VueCookies from 'vue-cookies'

export const eventBus = new Vue();

Vue.use(VueCookies);

Vue.config.productionTip = false;
Vue.mixin(mixin); // global mixin

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app');

