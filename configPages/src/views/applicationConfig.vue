<template>
    <div>
    <Row>
        <Col span="5">
            <span>应用名：</span>
            <Select clearable v-model="params.appName" placeholder="" @on-change="changeParam" style="width: 280px">
                <Option v-for="item in appNames" :value="item.appValue" :key="item.appValue">{{ item.appName }}</Option>
            </Select>
        </Col>

        <Col span="3">
            <span>环境：</span>
                <Select clearable v-model="params.appProfile" placeholder=""  @on-change="changeParam" style="width: 150px">
                    <Option v-for="item in profileNames" :value="item.profileValue" :key="item.profileValue">{{ item.profileName }}</Option>
                </Select>
        </Col>
        <Col span="3">
            <span>标签：</span>
            <Select clearable v-model="params.appLabel" placeholder="" @on-change="changeParam" style="width: 150px">
                <Option v-for="item in appLabels" :value="item.labelValue" :key="item.labelValue">{{ item.labelName }}</Option>
            </Select>
        </Col>
        <Col span="3">
            <span>状态：</span>
            <Select clearable v-model="params.state" placeholder="" @on-change="changeParam" style="width: 150px">
                <Option v-for="item in appStatus" :value="item.statusValue" :key="item.statusValue">{{ item.statusName }}</Option>
            </Select>
        </Col>
        <Col span="2">
            <Button type="primary" icon="ios-search" v-on:click="changeParam">搜索</Button>
        </Col>
        <Col span="2">
            <Button type="primary" v-on:click="addApp('addAppItem')"><Icon type="plus-round"></Icon> 新增应用</Button>
        </Col>
        <Col span="2">
            <Button type="info" v-on:click="refreshConfig" :loading="refreshLoading"> 刷新配置</Button>
        </Col>
    </Row>
    <br/>
    <Row>
        <Col span="24">
            <Table stripe :columns="appColumnNames" :data="applicationDetailData"></Table>
            <br/>
            <Page :total="totalCount" @on-change="changePage" @on-page-size-change="changePageSize"  size="small" show-total show-elevator show-sizer></Page>
        </Col>

        <Modal
                @on-ok="confirmModifyApp('modifyAppItem')"
                title="修改应用"
                v-model="modifyAppModel"
                class-name="vertical-center-modal"
                :loading="modifyLoading">
            <Form ref='modifyAppItem' :model="modifyAppItem" :rules="ruleValidate" :label-width="80">
                <FormItem label="应用名：" prop="appName">
                    <Input v-model="modifyAppItem.appName"/>
                </FormItem>
                <FormItem label="环境：" prop="profileType">
                    <Select clearable v-model="modifyAppItem.profileType">
                        <Option v-for="item in profileNames" :value="item.profileValue" :key="item.profileValue">{{ item.profileName }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="标签：" prop="appLabel">
                    <Input v-model="modifyAppItem.appLabel"/>
                </FormItem>
                <FormItem label="状态：" prop="appState">
                    <Select clearable v-model="modifyAppItem.appState">
                        <Option v-for="item in appStatus" :value="item.statusValue" :key="item.statusValue">{{item.statusName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="描述：" prop="appDesc">
                    <Input v-model="modifyAppItem.appDesc"/>
                </FormItem>
            </Form>
        </Modal>

        <Modal
                @on-ok="confirmAddApp('addAppItem')"
                title="新增应用"
                v-model="addAppModel"
                class-name="vertical-center-modal"
                :loading="addLoading">
            <Form ref='addAppItem' :model="addAppItem" :rules="ruleValidate" :label-width="80">
                <FormItem label="应用名：" prop="appName">
                    <Input v-model="addAppItem.appName"/>
                </FormItem>
                <FormItem label="环境：" prop="profileType">
                    <Select clearable v-model="addAppItem.profileType">
                        <Option v-for="item in profileNames" :value="item.profileValue" :key="item.profileValue">{{ item.profileName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="标签：" prop="appLabel">
                    <Input v-model="addAppItem.appLabel"/>
                </FormItem>
                <FormItem label="状态：" prop="appState">
                    <Select clearable v-model="addAppItem.appState">
                        <Option v-for="item in appStatus" :value="item.statusValue" :key="item.statusValue">{{item.statusName}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="描述：" prop="appDesc">
                    <Input v-model="addAppItem.appDesc"/>
                </FormItem>
            </Form>
        </Modal>

        <Modal title="YML预览"
               v-model="showYmlModel"
               width="1000px"
               :mask-closable="false"
               :style="{top: '5px'}"
               class-name="vertical-center-modal">
               <Input v-model="ymlData" readonly type="textarea" :autosize="{minRows: 13,maxRows: 32}" placeholder="暂无数据......"/>
        </Modal>
    </Row>
    </div>
</template>
<script>
    import api from '../libs/api';

    export default {
        mounted: function () {
            console.log("*****" + this.configDef.base_request_url)
            this.$http.post(api.findAllAppNames, null).then(function (data) {
                data = data.body;
                if(data.code === 2000){
                    this.appNames = data.data;
                }
            });
            this.$http.post(api.findAllAppProfileNames, null).then(function (data) {
                data = data.body;
                if(data.code === 2000){
                    this.profileNames = data.data;
                }
            });
            this.$http.post(api.findAllLabels, null).then(function (data) {
                data = data.body;
                if(data.code === 2000){
                    this.appLabels = data.data;
                }
            });
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
                if (!this.params.appProfile){
                    this.params.appProfile = null;
                }
                if (!this.params.state){
                    this.params.state = null;
                }
                this.$http.post(api.findAppList, this.params)
                    .then(function (data) {
                        data = data.body;
                    if (data.code === 2000){
                        this.applicationDetailData = data.data;
                        this.totalCount = data.page_info.total
                    } else {
                        this.$Message.error(data.msg);
                    }
                })
            },
            refreshConfig: function () {
                this.refreshLoading = true;
                this.$http.post(api.refresh, null).then(function (data) {
                    this.refreshLoading = false;
                        data = data.body;
                        if (data.code === 2000){
                            //this.$Message.success('刷新成功');
                            this.$Notice.success({
                                duration: 2,
                                title: '刷新配置成功'
                            });
                        } else {
                            //this.$Message.error(data.status + "：" + data.statusText);
                            this.$Notice.error({
                                duration: 2,
                                title: data.msg
                            });
                        }
                    })

            },
            addApp (name){
                this.$refs[name].resetFields();
                this.addAppModel = true;
            },
            deleteApp (index, appInfo) {
                console.log("delete appId:" + appInfo.appId);
                this.$Modal.confirm({
                    title: '删除确认',
                    content: '<p>确定删除该应用？</p>' +
                             '<p>应用名：' + appInfo.appName + '</p>' +
                             '<p>环境：' + appInfo.profileType + '</p>' +
                             '<p>标签：' + appInfo.appLabel + '</p>',
                    onOk: () => {
                        this.$http.post(api.delApp, {
                            'appId': appInfo.appId
                        }).then(function (data) {
                            data = data.body;
                            if(data.code === 2000){
                                this.$Message.success('删除成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    },
                    onCancel: () => {
                        // this.$Message.info('Clicked cancel');
                    }
                });
            },
            effectApp (index, appInfo) {
                this.$Modal.confirm({
                    title: '生效确认',
                    content: '<p>确定生效该应用？</p>' +
                             '<p>应用名：' + appInfo.appName + '</p>' +
                             '<p>环境：' + appInfo.profileType + '</p>' +
                             '<p>标签：' + appInfo.appLabel + '</p>',
                    onOk: () => {
                        this.$http.post(api.effectApp, {
                            'appId': appInfo.appId
                        }).then(function (data) {
                            data = data.body;
                            if(data.code === 2000){
                                this.$Message.success('生效成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    },
                    onCancel: () => {
                        // this.$Message.info('Clicked cancel');
                    }
                });
            },
            modifyApp (index, appInfo) {
                this.modifyAppModel = true;
                this.modifyAppItem.appId = appInfo.appId;
                this.modifyAppItem.appName = appInfo.appName;
                this.modifyAppItem.profileType = appInfo.profileTypeNum;
                this.modifyAppItem.appState = appInfo.appState;
                this.modifyAppItem.appLabel = appInfo.appLabel;
                this.modifyAppItem.appDesc = appInfo.appDesc;
            },
            viewYml (index, appInfo) {
                let url = this.configDef.base_request_url + '/' + appInfo.appLabel + '/' + appInfo.appName + '-' + appInfo.profileType + '.yml';
                this.$http.get(url,{
                    'appId': appInfo.appId
                }).then(function (data) {
                    if (Object.keys(data.body).length === 0){
                        this.ymlData = null;
                    } else {
                        this.ymlData = data.body;
                    }
                    this.showYmlModel = true;
                })
            },
            confirmModifyApp(name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        this.$http.post(api.modifyApp, this.modifyAppItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('修改成功');
                                    this.modifyAppModel = false;
                                    this.changeParam();
                                } else {
                                    this.$Message.error(data.msg);
                                }
                        })
                    }
                    this.modifyLoading = false;
                    this.$nextTick(() => {
                        this.modifyLoading = true;
                    });
                });
            },
            confirmAddApp(name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        this.$http.post(api.addApp, this.addAppItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('新增成功');
                                    this.addAppModel = false;
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
            }
        },
        watch:{
            profileNameModel: function (currentValue) {
                console.log("watch-pnm:" + currentValue)
            }
        },
        data () {
            return {
                totalCount: 0,
                addAppModel: false,
                modifyAppModel: false,
                addLoading: true,
                modifyLoading: true,
                refreshLoading: false,
                showYmlModel: false,
                ymlData: '',
                appNames: [{
                    appName: '',
                    appValue: ''
                }],
                profileNames: [{
                    profileValue: '',
                    profileName: ''
                }],
                appLabels: [{
                    labelName:'',
                    labelValue: ''
                }],
                appStatus: [
                    {
                        statusName: '有效',
                        statusValue: 'EFFECT'
                    },
                    {
                        statusName: '无效',
                        statusValue: 'DELETED'
                    }
                ],
                appColumnNames: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 80,
                        align: 'center'
                    },
                    {
                        title: '应用名',
                        key: 'appName',
                        width: 250
                    },
                    {
                        title: '环境',
                        key: 'profileType',
                        width: 150
                    },
                    {
                        title: '标签',
                        key: 'appLabel',
                        width: 150
                    },
                    {
                        title: '状态',
                        key: 'appState',
                        width: 150,
                        render: (h, param) => {
                            if (param.row.appState === 'EFFECT'){
                                return h('span', '有效')
                            } else {
                                return h('span', {
                                    style: {
                                        color: ("red")
                                    }
                                },'无效')
                            }
                        }
                    },
                    {
                        title: '描述',
                        key: 'appDesc',
                        width: 350
                    },
                    {
                        title: '最近修改时间',
                        key: 'updateTime',
                        width: 250
                    },
                    {
                        title: '操作',
                        key: 'opts',
                        align: 'center',
                        render: (h, param) => {
                            let stateButton = null;
                            let modifyButton = null;
                            let profileButton = null;
                            let viewButton = null;
                            if(param.row.appState === 'EFFECT'){
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
                                            this.deleteApp(param.index, param.row)
                                        }
                                    }
                                }, '删除');
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
                                            this.effectApp(param.index, param.row)
                                        }
                                    }
                                }, '生效');
                            }
                            modifyButton = h('Button', {
                                props: {
                                    type: 'info',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.modifyApp(param.index, param.row)
                                    }
                                }
                            }, '修改');
                            profileButton = h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.$router.push({
                                            path: '/profileconfig',
                                            // name: 'TEST',
                                            query: {
                                                appId: param.row.appId
                                            }
                                        })
                                    }
                                }
                            }, '配置列表');
                            viewButton = h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.viewYml(param.index, param.row)
                                    }
                                }
                            }, 'YML预览');
                            return h('div', [stateButton, modifyButton, profileButton, viewButton]);
                        }
                    }
                ],
                applicationDetailData:[],
                modifyAppItem : {
                    'appId': '',
                    'appName': '',
                    'profileType': null,
                    'appState': '',
                    'appLabel': '',
                    'appDesc': ''
                },
                addAppItem : {
                    'appName': '',
                    'profileType': null,
                    'appLabel': '',
                    'appState' : 'DELETED',
                    'appDesc': ''
                },
                ruleValidate: {
                    appName: [
                        {required: true, message: "请输入应用名"}
                    ],
                    profileType: [
                        {required: true, message: "请选择环境"}
                    ],
                    appLabel: [
                        {required: true, message: "请输入标签"}
                    ],
                    appState: [
                        {required: true, message: "请选择状态"}
                    ]
                },
                params: {
                    page: 0,
                    count: 10,
                    appName: '',
                    appProfile: null,
                    appLabel: '',
                    state: null
                }
            }
        }
    }
</script>
