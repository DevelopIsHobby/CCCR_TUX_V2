<template>
    <div class="Drawer">
        <v-navigation-drawer
            v-model="drawer"
            color="#3852f3"
            expand-on-hover
            fixed
            stateless>
            
            <v-list>
                <v-list-item
                    class="between"
                    :class="gCurrentPage === 'main' && 'active'"
                    two-line
                    @click="handleClickMenu({ name: 'main' })">
                    <v-list-item-avatar
                        class="miniVariant && 'px-0'"
                        height="auto"
                        min-width="25px"
                        size="25px"
                        style="margin-left: -2px;"
                        tile>
                        <img src="@/assets/img/m_s_w.png" alt="logo">
                    </v-list-item-avatar>
                    <v-list-item-content>
                        <v-list-item-title class="drawerAvatarTitle">Machbase</v-list-item-title>
                        <v-list-item-subtitle>Embedded Web Server</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>

                <v-divider></v-divider>
                

                <v-list-item
                    v-for="menu in gMenus"
                    class="between"
                    :class="[menu.type && 'drawerDivider', (gCurrentPage === menu.name) && 'active']"
                    :key="menu.name"
                    :style="menu.style && menu.style"
                    @click="handleClickMenu(menu)">
                    
                    <template v-if="menu.type">
                        <v-divider></v-divider>
                    </template>

                    <template v-else>
                        <v-list-item-icon>
                            <v-icon color="white">
                               {{ menu.icon }}
                            </v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>
                                {{ menu.name | uppercase }}
                            </v-list-item-title>
                        </v-list-item-content>
                    </template>
                </v-list-item>

                <div v-if="this.$store.state.gAuth && this.$store.state.gId != 'noAuth'">
                    <div tabindex="0" role="listitem" class="between v-list-item v-list-item--link theme--light drawerDivider"><hr role="separator" aria-orientation="horizontal" class="v-divider theme--light"></div>

                    <v-list-item
                        class="between"
                        @click="handleLogout()">
                        <v-list-item-icon>
                            <v-icon color="white">
                                L
                            </v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>
                                LOGOUT
                            </v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </div>
            </v-list>
        </v-navigation-drawer>
    </div>
</template>

<script>
import { mapState } from 'vuex';
import { gMenus } from '@/assets/ts/config';
import viewMixin from '@/mixins/view';


export default {
    name: "Drawer",
    mixins: [viewMixin],
    methods: {
        handleClickMenu(aObj, $event) {
            if (aObj.blank) {
                window.open(aObj.url, '_blank');
            } else {
                aObj.name !== '' && this.setCurrentPage(aObj.name);
            }
        },
        handleLogout(){

            this.$store.commit('setId', '');
            this.$store.commit('setPw', '');

            this.$cookies.remove("accesstoken");
            this.$store.commit('setAuth', false);
            this.$store.commit('setCurrentPage', 'login');
        }
    },
    data() {
        return {
            drawer: true,
            miniVariant: false,
            gMenus
        }
    }
}
</script>

<style lang="scss">


.Drawer {
    
    .v {
        
        &-list {
            &-item {
                &__subtitle {
                    color: white !important;
                }
            }
        }
    }

    .active {
        background-color: #2b43df;
    }

    .drawerAvatarTitle {
        font-size: 1rem !important;
    }

    .drawerDivider {
        &:hover {
            cursor: default;
            all: none;

            &::before {
                opacity: 0 !important;
            }
        }
    }

    .drawerNotice {
        width: 100%;
        height: 56px;
        color: white;

        .drawerNoticeIconWrap {
            min-width: 56px;

            .v-icon {
                cursor: pointer;
            }
        }

        .drawerNoticeMessage {
            min-width: 150px;
        }
    }
}
.auth_img { width: 100%; height: 100%;}
.Drawer {
    
}
</style>