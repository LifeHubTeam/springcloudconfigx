<style scoped>
    .layout{
        border: 1px solid #d7dde4;
        background: #f5f7f9;
        position: relative;
        border-radius: 4px;
        overflow: hidden;
    }
    .layout-logo{
        width: 100px;
        height: 30px;
        background: #5b6270;
        border-radius: 3px;
        float: left;
        position: relative;
        top: 15px;
        left: 20px;
    }
    .layout-nav{
        margin: 0 auto;
        margin-right: 20px;
    }
    .layout-footer-center{
        text-align: center;
    }
</style>
<template>
    <div class="layout">
        <Layout>
            <Header>
                <Menu mode="horizontal" theme="dark">
                    <!--<div class="layout-logo"></div>-->
                    <Row>
                        <Col span="4">
                            <h2 style="color: white">配 置 中 心 - Config Center</h2>
                        </Col>
                        <Col style="float: right">
                            <div class="layout-nav">
                                <router-link to="/userlist" v-if="userInfo.roleType === 'SUPER_ADMIN'">
                                    <MenuItem name="0">
                                        <Icon type="android-contact"></Icon>
                                        用户管理
                                    </MenuItem>
                                </router-link>
                                <router-link to="/appconfig">
                                    <MenuItem name="1">
                                        <Icon type="ios-navigate"></Icon>
                                        应用列表
                                    </MenuItem>
                                </router-link>
                                <router-link to="/profiles">
                                    <MenuItem name="2">
                                        <Icon type="ios-keypad"></Icon>
                                        配置查询
                                    </MenuItem>
                                </router-link>
                                <MenuItem name="3">
                                    <!--<Icon type="ios-analytics"></Icon>-->
                                    <!--Login/Logout-->
                                    <Dropdown transfer @on-click="userClick" placement="bottom-start">
                                        <Icon type="arrow-down-b"></Icon>
                                        {{ userInfo.name }}
                                        <DropdownMenu slot="list">
                                            <!--<DropdownItem name="ownSpace">个人信息</DropdownItem>-->
                                            <DropdownItem name="logout" divided>注销登录</DropdownItem>
                                        </DropdownMenu>
                                    </Dropdown>
                                    <Avatar :src="userInfo.headImage" style="margin-left: 10px;" icon="ios-person"></Avatar>
                                </MenuItem>
                            </div>
                        </Col>
                    </Row>
                </Menu>
            </Header>
            <Content :style="{padding: '0 50px'}">
                <Breadcrumb :style="{margin: '20px 0'}">
                    <BreadcrumbItem v-for="item in breadcrumbs" :to="item.path" :key="item.index">{{item.title}}</BreadcrumbItem>
                </Breadcrumb>
                <Card>
                    <div style="min-height: 200px;">
                        <router-view></router-view>
                    </div>
                </Card>
            </Content>
            <Footer class="layout-footer-center">2018-2018 &copy; LifeHub.top</Footer>
        </Layout>
    </div>
</template>
<script>
    import util from '../libs/util';
    import api from '../libs/api';

    export default {
        mounted: function () {
            let authUser = sessionStorage.getItem("authUser");
            if (authUser){
                this.userInfo = JSON.parse(authUser);
            }
            util.setBreadcrumbs(this);
        },
        computed: {
              breadcrumbs (){
                  return this.$store.state.app.breadcrumbs;
              }
        },
        methods: {
            userClick: function (name) {
                if (name === 'ownSpace') {

                } else if (name === 'logout') {
                    this.$http.post(api.logout, null).then(function (data) {
                        data = data.body;
                        if (data.code === 2000) {
                            sessionStorage.removeItem("token");
                            sessionStorage.removeItem("authUser");
                            this.$router.push({
                                path: '/login'
                            });
                            this.$Message.success('注销成功');
                        } else {
                            this.$Message.error(data.msg);
                        }
                    })
                }
            }
        },
        watch: {
            '$route' (to) {
                util.setBreadcrumbs(this);
            }
        },
        data () {
            return {
                userInfo:{
                    name: '',
                    headImage: ''
                }
            }
        }
    }
</script>