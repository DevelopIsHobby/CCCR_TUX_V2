<template>
    <div class="login_box">
        <div class="logo_div">
            <img src="@/assets/img/machbase_logo.png" alt="logo" class="logo">
        </div>
        <p class="login_text">If you have forgetten your username or password,<br>please contact your Machbase Web Analytics Manager</p>
        <div>
            <input type="text" placeholder="ID" class="login_input" v-model="id">
            <input type="password" placeholder="Password" class="login_input" v-model="pw">
            <button class="send_btn" @click="onSubmitForm">-></button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import viewMixin from '@/mixins/view';

export default {
    name: 'Login',
    components: {
        // Info,
    },
    mixins: [viewMixin],
    methods: {
        async onSubmitForm() {
            let sResult = {};
            
            if (this.id === '' || this.pw === '') {
                this.inputError = true;
                // alert('ERROR')
                return;
            } else {
                this.inputError = false;
                this.requested = true;
            }
            try {
                const sIp = this.gHost.split(':')[0];
                const sKey = "Basic " + btoa(`${this.id}@${sIp}:${this.pw}`);
                const sHeader = {
                    headers: {"Authorization": sKey},
                };
                sResult = (await axios.get('http://' + this.gHost + '/machbase?q=select count(*) from M$SYS_USERS', sHeader)).data;
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
            if(sResult.error_code == 0){

                console.log(sResult)

                this.$store.commit('setId', this.id);
                this.$store.commit('setPw', this.pw);

                this.$cookies.set("accesstoken", this.$store.state.gId + '||' + this.$store.state.gPw, 60*60*12);
                axios.defaults.headers.common["x-access-token"] = this.token;
                alert("Welcome!");
                this.$store.commit('setAuth', true);
                this.$store.commit('setCurrentPage', 'main');
            }else{
                alert('User does not Exist or Wrong Password');
                this.id = "";
                this.pw = "";
            }
        }
    },
    data() {
        return {
            id: '',
            pw: '',
            token: ''
        }
    },
    mounted : async function(){
        let sAuth = {};
        try {
                const sIp = this.gHost.split(':')[0];
                const sKey = "Basic " + btoa(`${this.$store.state.gId}@${sIp}:${this.$store.state.gPw}`);
                const sHeader = {
                    headers: {"Authorization": sKey},
                };
                sAuth = (await axios.get('http://' + this.gHost + '/machbase?q=select count(*) from M$SYS_USERS', sHeader)).data;
        } catch (err) {
            console.log('Axios Api Request Error!');
            console.error(err);
        }
        if(sAuth.error_code == 0){

                this.$cookies.set("accesstoken", this.$store.state.gId, 60*60*12);
                axios.defaults.headers.common["x-access-token"] = this.token;
                this.$store.commit('setAuth', true);
                this.$store.commit('setCurrentPage', 'main');
        }

        
    }
}
</script>

<style lang="scss">
    .login_container { width: 100%; height: 100%;}
    .logo_div {margin: 20px 0 10px;}
    .logo { width: 320px;}
    .login_box { padding: 50px 20px; background-color: #161421; border: 1px solid black; text-align: center; width: 500px; margin: 10% auto 0; box-shadow: 0px 20px 30px 0 #22222f;}
    .login_box .login_text { display: block; color: #5686b3; font-size: 12px; font-weight: bold; letter-spacing: 0.3px; margin-bottom: 30px !important; text-align: center; box-sizing: border-box; }
    .login_input { background-color: #5686b3; border: 1px solid #161421; height: 40px; color: #fff; padding: 6px 12px; font-size: 14px; border-radius: 4px; box-shadow: inset 0 1px 1px rgb(0 0 0 / 8%); box-sizing: border-box;}
    .login_input:first-child { margin-right: 2px;}
    .login_input::placeholder { color: #fff;}
    .send_btn { background-color: #2d488d; color: #fff; font-size: 18px; display: inline-block; padding: 6px 12px; margin-bottom: 0; font-weight: 400; line-height: 1.4; text-align: center; white-space: nowrap; vertical-align: top; user-select: none; border: 1px solid transparent;border-radius: 4px; margin-left: 2px;}
</style>