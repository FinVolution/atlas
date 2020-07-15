<template>
    <div class="login-wrapper">
        <div v-if="this.loading && !this.invalidLogin">
            正在登陆中，请稍后...
        </div>
        <div v-else>
            <div><img src="../assets/img/dog.jpg"/></div>
            <div>当前登录失败，请回到首页重新尝试登录。</div>
            <el-button @click="returnHome">回到首页</el-button>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'

    export default {
        data: function () {
            return {
                loading: true
            }
        },
        computed: {
            ...mapGetters({
                isLogin: 'getLoginState',
                invalidLogin: 'getInvalidLoginState',
                promptMessage: 'getPromptMessage',
                lastVisit: 'getLastVisit'
            })
        },
        methods: {
            ...mapActions(['fetchToken', 'initLoginInfo']),
            returnHome() {
                this.$router.push('/');
            },
        },
        created () {
            this.$store.dispatch('fetchLastVisit');
        },
        mounted: function () {
            let query = this.$route.query;

            if (query.error != null) {
                this.loading = false;
            } else if (query.code != null) {
                this.loading = true;
                // fetch access/refresh token by auth code
                this.$store.dispatch('fetchToken', {'code': query.code});
            } else {
                this.loading = false;
//                this.$store.dispatch('readLoginInfo');
            }
        },
        watch: {
            isLogin: function () {
                // 当获取用户登录信息完成，则进行跳转
                if (this.isLogin) {
                    this.loading = true;
                    // 跳转回用户最近一次访问的地址
                    if (this.lastVisit) {
                        this.$router.push(this.lastVisit);
                    } else {
                        this.$router.push('/');
                    }
                }
            },
            promptMessage: function (newMessage) {
                if (newMessage.code != null) {
                    if (newMessage.code >= 0) {
                        this.$message.success(newMessage.details);
                    } else {
                        this.$message.error(newMessage.details);
                    }
                }
            }
        }
    }
</script>

<style scoped>

    .login-wrapper {
        position: relative;
        height: 100%;
        width: 100%;
        /*background-color: #8a8a8a;*/
        text-align: center;
        padding-top: 100px;
    }

    .login-btn button {
        width: 100%;
        height: 36px;
    }


</style>