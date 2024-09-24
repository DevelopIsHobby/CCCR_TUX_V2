<template>
    <div class="Info">
        <v-card flat>
            <v-card-text>
                <h2>{{ gInfo && gInfo.title }}</h2>
            </v-card-text>
            <v-card-text v-show="gInfo.description" class="between">
                
                <div v-html="gInfo && gInfo.description"></div>
                
                <VueApexCharts
                    v-show="gSeries"
                    height="150"
                    type="bar"
                    style="width: 70%"
                    :options="chartOptions"
                    :series="gSeries ? [gSeries] : []" />
            </v-card-text>
            
            
        </v-card>
    </div>
</template>

<script>
import VueApexCharts from 'vue-apexcharts';

const chartOptions = {
    chart: {
        id: 'infoChart',
    },
    colors: ['#3852f3'],
    dataLabels: {
        enabled: false,
    },
    plotOptions: {
        bar: {
            horizontal: true,
        }
    },
    xaxis: {
        categories: ['Sessions', 'Connections']
    },
    yaxis: {
        min: 0,
        max: 100
    }
}

export default {
    name: 'Info',
    components: {
        VueApexCharts
    },
    props: [
        'gInfo',
        'gSeries'
    ],
    data() {
        return {
            chartOptions
        }
    },
}
</script>

<style lang="scss">
.Info {
    width: 100%;
    
    .v-card {
        width: 100%;
        /* min-height: 200px; */
        margin-bottom: 20px;
    }
}
</style>