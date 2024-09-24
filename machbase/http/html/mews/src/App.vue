<template>
    <v-app>
        <Drawer/>
        <div id="content">
            <component :is="loader"></component><!-- dynamic import -->
        </div>
        
    </v-app>
</template>

<script lang="ts">
import Vue from 'vue';
import { Api, Example, Main, Login } from '@/views';
import { Drawer } from '@/components';
import viewMixin from '@/mixins/view';

export default Vue.extend({
    components: {
        Drawer,
        Api,
        Example,
        Main,
        Login,
        
    },
    mixins: [viewMixin],
    computed: {
        loader() {
            if(this.$store.state.gAuth){
                let sCurrentPage = this.$store.state.gCurrentPage;
                if (sCurrentPage) {
                    sCurrentPage = sCurrentPage[0].toUpperCase() + sCurrentPage.slice(1);
                    return () => import(`./views/${sCurrentPage}`);
                } else {
                    return null;
                }
            }else{
                return 'login';
            }
        }
    },
    mounted() {

        const scookie = this.$cookies.get("accesstoken");
        if(scookie != null){
            const sdata = scookie.split('||')
            this.$store.commit('setAuth', true);
            this.$store.commit('setId', sdata[0]);
            this.$store.commit('setPw', sdata[1]);
        }

        if(this.$store.state.gAuth){
            this.$store.commit('setCurrentPage', 'main');
        }else{
            this.$store.commit('setCurrentPage', 'login');
        }
    },
});
</script>

<style lang="scss">
@import '@/assets/scss/common';


#app {
    width: 100%;
    height: 100%;
    position: relative;
    background-color: #f0f0f0;

    #content {
        width: calc(100% - 56px);
        height: 100%;
        margin-left: 56px;
        padding: 20px;
    }
}

</style>
