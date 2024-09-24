<template>
    <div class="Chip" :class="gChipColor && 'main'">
        <v-tooltip right>
            <template v-slot:activator="{ on, attrs }">
                <v-chip
                    v-bind="attrs"
                    v-on="!isEmptyObj(gChipInfo) && on"
                    label
                    outlined
                    :color="gChipColor && gChipColor"
                    :style="state">
                    {{ gChipName }}
                </v-chip>
            </template>

            <div class="chipTooltipWrap">
                <div
                    v-for="key in Object.keys(gChipInfo)"
                    class="chipTooltipItem between"
                    :key="key">
                    <span>{{ key }}</span>
                    <span>{{ gChipInfo[key] }}</span>
                </div>
            </div>
        </v-tooltip>
    </div>
</template>

<script>
export default {
    name: 'Chip',
    props: [
        'gChipColor',
        'gChipInfo',
        'gChipName',
    ],
    computed: {
        state() {
            const sState = this.gChipInfo['Actual-State'];

            return (sState === 'normal' || sState === 'primary' || sState === 'leader') ? '' : 'errorClass';
        }
    },
    data() {
        return {
            errorClass: {
                color: '#ff5252'
            }
        }
    }
}
</script>

<style lang="scss">
.Chip {
    display: inline-flex;

    .v {
        &-chip {
            min-width: 80px;
            justify-content: center;
        }
    }

    .chipTooltipItem {
        span {
            &:first-child {
                margin-right: 10px;
            }
        }
    }
}
</style>