import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Main from '../views/Main.vue';
import NotFound from '../views/404.vue';

import Login from '../views/Login.vue';

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
    {
        path: '/',
        name: 'Main',
        component: Main
    },
    // {
    //     path: '/api',
    //     name: 'Api',
    //     component: Api
    // },
    // {
    //     path: '/example',
    //     name: 'example',
    //     component: Example
    // },
    {
        path: '*',
        component: NotFound
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;