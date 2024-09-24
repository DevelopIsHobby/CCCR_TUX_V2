<template>
    <div class="Top">
        <v-btn
            class="topBtn"
            color="#3852f3"
            title="Go to Top"
            bottom
            dark
            fab
            fixed
            right
            small
           
            @click="handleClickTop">
            <v-icon dark small>mdi-arrow-up</v-icon>
        </v-btn>
    </div>
</template>

<script>
export default {
    name: 'Top',
    methods: {
        handleClickTop() {
            window.scrollTo(0, 0);
        }
    }
}
</script>

<style lang="scss">
    .topBtn{
        position: fixed;
        top:50%;
        left:50%;
        z-index: 99999;
        padding:50px;
    }
</style>