<template>
    <div>
    <Row>
        <Col span="4">
            <span>角色：</span>
            <Select clearable v-model="params.roleType" placeholder="" @on-change="changeParam" style="width: 180px">
                <Option v-for="item in roleList" :value="item.roleValue" :key="item.roleValue">{{ item.roleName }}</Option>
            </Select>
        </Col>

        <Col span="4">
            <span>状态：</span>
                <Select clearable v-model="params.state" placeholder=""  @on-change="changeParam" style="width: 180px">
                    <Option v-for="item in stateList" :value="item.stateValue" :key="item.stateValue">{{ item.stateName }}</Option>
                </Select>
        </Col>
        <Col span="5">
            <Input v-model="params.keyword" placeholder="账号或手机号码" style="width: 280px"/>
        </Col>
        <Col span="2">
            <Button type="primary" icon="ios-search" v-on:click="changeParam">搜索</Button>
        </Col>
        <Col span="2">
            <Button type="primary" v-on:click="addUser('addUserItem')"><Icon type="plus-round"></Icon> 新增用户</Button>
        </Col>
    </Row>
    <br/>
    <Row>
        <Col span="24">
            <Table stripe :columns="userColumnNames" :data="userListData"></Table>
            <br/>
            <Page :total="totalCount" @on-change="changePage" @on-page-size-change="changePageSize"  size="small" show-total show-elevator show-sizer></Page>
        </Col>

        <Modal
                @on-ok="confirmAddUser('addUserItem')"
                title="新增用户"
                v-model="addUserModel"
                class-name="vertical-center-modal"
                :loading="addLoading"
                width="550px">
            <Form ref='addUserItem' :model="addUserItem" :rules="ruleValidate" :label-width="100">
                <FormItem label="用户名：" prop="name">
                    <Input v-model="addUserItem.name"/>
                </FormItem>
                <FormItem label="登录账号：" prop="account">
                    <Input v-model="addUserItem.account"/>
                </FormItem>
                <FormItem label="登录密码：" prop="password">
                    <Input v-model="addUserItem.password" type="password"/>
                </FormItem>
                <FormItem label="手机号码：" prop="mobile">
                    <Input v-model="addUserItem.mobile" />
                </FormItem>
                <FormItem label="状态：" prop="state">
                    <Select clearable v-model="addUserItem.state">
                        <Option v-for="item in stateList" :value="item.stateValue" :key="item.stateValue">{{ item.stateName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="角色：" prop="roleType">
                    <Select clearable v-model="addUserItem.roleType">
                        <Option v-for="item in roleList" :value="item.roleValue" :key="item.roleValue">{{item.roleName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="邮箱：" prop="email">
                    <Input v-model="addUserItem.email"/>
                </FormItem>
                <FormItem label="头像：" prop="headImage">
                    <Input v-model="addUserItem.headImage"/>
                </FormItem>
            </Form>
        </Modal>

        <Modal
                @on-ok="confirmUpdateUser('updateUserItem')"
                title="修改用户"
                v-model="updateUserModel"
                class-name="vertical-center-modal"
                :loading="updateLoading"
                width="550px">
            <Form ref='updateUserItem' :model="updateUserItem" :rules="ruleValidate" :label-width="100">
                <FormItem label="用户名：" prop="name">
                    <Input v-model="updateUserItem.name"/>
                </FormItem>
                <FormItem label="登录账号：" prop="account">
                    <Input v-model="updateUserItem.account"/>
                </FormItem>
                <FormItem label="登录密码：" prop="password">
                    <Input v-model="updateUserItem.password" type="password"/>
                </FormItem>
                <FormItem label="手机号码：" prop="mobile">
                    <Input v-model="updateUserItem.mobile" />
                </FormItem>
                <FormItem label="状态：" prop="state">
                    <Select clearable v-model="updateUserItem.state">
                        <Option v-for="item in stateList" :value="item.stateValue" :key="item.stateValue">{{ item.stateName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="角色：" prop="roleType">
                    <Select clearable v-model="updateUserItem.roleType">
                        <Option v-for="item in roleList" :value="item.roleValue" :key="item.roleValue">{{item.roleName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="邮箱：" prop="email">
                    <Input v-model="updateUserItem.email"/>
                </FormItem>
                <FormItem label="头像：" prop="headImage">
                    <Input v-model="updateUserItem.headImage"/>
                </FormItem>
            </Form>
        </Modal>

    </Row>
    </div>
</template>
<script>
    import md5 from 'js-md5';
    import api from '../libs/api';

    export default {
        mounted: function () {
            this.changeParam();
        },
        methods:{
            changePage (page){
                this.params.page = page - 1;
                this.changeParam();
            },
            changePageSize (count){
                this.params.count = count;
                this.changeParam();
            },
            changeParam: function () {
                if (!this.params.state){
                    this.params.state = null;
                }
                if (!this.params.roleType){
                    this.params.roleType = null;
                }
                this.$http.post(api.listUser, this.params).then(function (data) {
                    data = data.body;
                    if (data.code === 2000){
                        this.userListData = data.data;
                        this.totalCount = data.page_info.total
                    } else {
                        this.$Message.error(data.msg);
                    }
                })
            },
            addUser (name){
                this.$refs[name].resetFields();
                this.addUserModel = true;
            },
            confirmAddUser(name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        this.addUserItem.password = md5(this.addUserItem.password);
                        this.$http.post(api.addUser, this.addUserItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('新增成功');
                                    this.addUserModel = false;
                                    this.changeParam();
                                } else {
                                    this.$Message.error(data.msg);
                                }
                            })
                    }
                    this.addLoading = false;
                    this.$nextTick(() => {
                        this.addLoading = true;
                    });
                });
            },
            updateUser (index, userInfo) {
                this.updateUserModel = true;
                this.oldPwd = userInfo.password;
                this.updateUserItem.userId = userInfo.userId;
                this.updateUserItem.account = userInfo.account;
                this.updateUserItem.mobile = userInfo.mobile;
                this.updateUserItem.name = userInfo.name;
                this.updateUserItem.email = userInfo.email;
                this.updateUserItem.password = userInfo.password;
                this.updateUserItem.state = userInfo.state;
                this.updateUserItem.roleType = userInfo.roleType;
                this.updateUserItem.headImage = userInfo.headImage;
            },
            confirmUpdateUser (name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        if (this.updateUserItem.password !== this.oldPwd){
                            this.updateUserItem.password = md5(this.updateUserItem.password);
                        }
                        this.$http.post(api.updateUser, this.updateUserItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('修改成功');
                                    this.updateUserModel = false;
                                    this.changeParam();
                                } else {
                                    this.$Message.error(data.msg);
                                }
                            })
                    }
                    this.updateLoading = false;
                    this.$nextTick(() => {
                        this.updateLoading = true;
                    });
                });
            },
            enableUser (index, userInfo) {
                this.$Modal.confirm({
                    title: '解冻确认',
                    content: '<p>确定解冻该用户？</p>' +
                    '<p>用户名：' + userInfo.name + '</p>' +
                    '<p>登录账号：' + userInfo.account + '</p>' +
                    '<p>角色：' + (userInfo.roleType === 'SUPER_ADMIN' ? "超级管理员" : "普通用户")+ '</p>',
                    onOk: () => {
                        this.$http.post(api.enableUser, {
                            'userId': userInfo.userId
                        }).then(function (data) {
                            data = data.body;
                            if(data.code === 2000){
                                this.$Message.success('解冻成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    }
                });
            },
            frozenUser (index, userInfo) {
                this.$Modal.confirm({
                    title: '冻结确认',
                    content: '<p>确定冻结该用户？</p>' +
                    '<p>用户名：' + userInfo.name + '</p>' +
                    '<p>登录账号：' + userInfo.account + '</p>' +
                    '<p>角色：' + (userInfo.roleType === 'SUPER_ADMIN' ? "超级管理员" : "普通用户")+ '</p>',
                    onOk: () => {
                        this.$http.post(api.frozenUser, {
                            'userId': userInfo.userId
                        }).then(function (data) {
                            data = data.body;
                            if(data.code === 2000){
                                this.$Message.success('冻结成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    }
                });
            },
        },
        data () {
            return {
                totalCount: 0,
                addUserModel: false,
                updateUserModel: false,
                addLoading: true,
                updateLoading: true,
                oldPwd: null,
                roleList: [
                    {
                        roleName: '超级管理员',
                        roleValue: 'SUPER_ADMIN'
                    },
                    {
                        roleName: '普通用户',
                        roleValue: 'GENERAL'
                    }
                ],
                stateList: [
                    {
                        stateName: '正常',
                        stateValue: 'NORMAL'
                    },
                    {
                        stateName: '冻结',
                        stateValue: 'FROZEN'
                    }
                ],
                userColumnNames: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 80,
                        align: 'center'
                    },
                    {
                        title: '头像',
                        key: 'headImage',
                        width: 70,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Avatar', {
                                    attrs: {
                                        icon: 'ios-person',
                                        src: params.row.headImage
                                    },
                                    style: {
                                        width: '33px',
                                        height: '33px'
                                    }
                                }),
                            ]);
                        }
                    },
                    {
                        title: '用户名',
                        key: 'name',
                        width: 180
                    },
                    {
                        title: '登录账号',
                        key: 'account',
                        width: 180
                    },
                    {
                        title: '手机号码',
                        key: 'mobile',
                        width: 180
                    },
                    {
                        title: '邮箱',
                        key: 'email',
                        width: 250
                    },
                    {
                        title: '角色',
                        key: 'roleType',
                        width: 180,
                        render: (h, param) => {
                            if (param.row.roleType === 'SUPER_ADMIN'){
                                return h('div', '超级管理员')
                            } else {
                                return h('div', '普通用户')
                            }
                        }
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 150,
                        render: (h, param) => {
                            if (param.row.state === 'NORMAL'){
                                return h('span', '正常')
                            } else {
                                return h('span', {
                                    style: {
                                        color: ("red")
                                    }
                                },'冻结')
                            }
                        }
                    },
                    {
                        title: '创建时间',
                        key: 'createTime',
                        width: 250
                    },
                    {
                        title: '操作',
                        key: 'opts',
                        align: 'center',
                        render: (h, param) => {
                            let stateButton = null;
                            let updateButton = null;
                            if(param.row.state === 'NORMAL'){
                                stateButton = h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    },
                                    style: {
                                        margin: '0 5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.frozenUser(param.index, param.row)
                                        }
                                    }
                                }, '冻结');
                            } else {
                                stateButton = h('Button', {
                                    props: {
                                        type: 'warning',
                                        size: 'small'
                                    },
                                    style: {
                                        margin: '0 5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.enableUser(param.index, param.row)
                                        }
                                    }
                                }, '解冻');
                            }
                            updateButton = h('Button', {
                                props: {
                                    type: 'info',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.updateUser(param.index, param.row)
                                    }
                                }
                            }, '修改');
                            return h('div', [stateButton, updateButton]);
                        }
                    }
                ],
                userListData:[],
                updateUserItem : {
                    userId: null,
                    account: null,
                    mobile: null,
                    name: null,
                    email: null,
                    password: null,
                    state: null,
                    roleType: null,
                    headImage: null
                },
                addUserItem: {
                    account: null,
                    mobile: null,
                    name: null,
                    email: null,
                    password: null,
                    state: null,
                    roleType: null,
                    headImage: null
                },
                ruleValidate: {
                    account: [
                        {required: true, message: "请输入登录账号"}
                    ],
                    mobile: [
                        {required: true, message: "手机号码不能为空且必须为数字"},
                        {type: 'number', message: '请输入数字格式', transform(value) {
                            return Number(value);
                        }}
                    ],
                    name: [
                        {required: true, message: "请输入用户名"}
                    ],
                    password: [
                        {required: true, message: "请输入登录密码"}
                    ],
                    state: [
                        {required: true, message: "请选择状态"}
                    ],
                    roleType: [
                        {required: true, message: "请选择角色"}
                    ]
                },
                params: {
                    page: 0,
                    count: 10,
                    state: null,
                    roleType: null,
                    keyword: null
                }
            }
        }
    }
</script>
