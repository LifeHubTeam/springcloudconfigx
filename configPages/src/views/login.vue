<style scoped>
    .login{
        width: 100%;
        height: 100%;
        background-image: url('../images/login_bg.jpg');
        background-size: cover;
        background-position: center;
        position: relative;
        font-size: 16px;
        font-weight: 300;
        text-align: center;
        padding: 472px 0;
    }
    .login-con{
        position: absolute;
        right: 160px;
        top: 50%;
        transform: translateY(-60%);
        width: 300px;
    }
    .form-con{
        padding: 10px 0 0;
    }
</style>

<template>
    <div class="login" @keydown.enter="handleSubmit">
        <div class="login-con">
            <Card :bordered="false">
                <p slot="title">
                    <Icon type="log-in"></Icon>
                    欢迎登录配置中心
                </p>
                <div class="form-con">
                    <Form ref="loginForm" :model="loginForm" :rules="rules">
                        <FormItem prop="account">
                            <Input v-model="loginForm.account" placeholder="请输入账号">
                                <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="loginForm.password" placeholder="请输入密码">
                                <span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem>
                            <Button @click="handleSubmit" type="primary" long>登录</Button>
                        </FormItem>
                    </Form>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
import md5 from 'js-md5';
import api from '../libs/api';

export default {
    methods: {
        handleSubmit () {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.$http.post(api.login, {
                        account: this.loginForm.account,
                        password: md5(this.loginForm.password)
                    }).then(function (data) {
                        data = data.body;
                        if (data.code === 2000) {
                            sessionStorage.setItem("token", data.data.token);
                            sessionStorage.setItem("authUser", JSON.stringify(data.data));
                            if (data.data.roleType === 'SUPER_ADMIN'){
                                this.$router.push({
                                    path: '/userlist'
                                });
                            } else {
                                this.$router.push({
                                    path: '/appconfig'
                                });
                            }
                        } else {
                            this.$Message.error(data.msg);
                        }
                    })
                }
            });
        }
    },
    data () {
        return {
            loginForm: {
                account: '',
                password: ''
            },
            rules: {
                account: [
                    { required: true, message: '账号不能为空', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' }
                ]
            }
        };
    }
};
</script>

<style>

</style>
