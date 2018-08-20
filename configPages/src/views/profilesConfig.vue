<template>
    <div>
        <Row>
            <Col span="4" style="padding-right: 10px; ">
                <Card>
                    <p slot="title">
                        <Icon type="navicon-round"></Icon>
                        应用信息
                    </p>
                    <p>
                        <Icon type="heart"></Icon>&nbsp;&nbsp;应用名：{{appInfo.appName}}
                    </p>
                    <br/>
                    <p>
                        <Icon type="waterdrop"></Icon>&nbsp;&nbsp;环&nbsp;&nbsp;&nbsp;境：{{appInfo.appProfile}}
                    </p>
                    <br/>
                    <p>
                        <Icon type="leaf"></Icon>&nbsp;&nbsp;标&nbsp;&nbsp;&nbsp;签：{{appInfo.appLabel}}
                    </p>
                    <br/>
                    <p>
                        <Icon type="flag"></Icon>&nbsp;&nbsp;状&nbsp;&nbsp;&nbsp;态：{{appInfo.appState === 'EFFECT' ? '有效' : '无效'}}
                    </p>
                    <br/>
                    <p>
                        <Icon type="document-text"></Icon>&nbsp;&nbsp;描&nbsp;&nbsp;&nbsp;述：{{appInfo.appDesc}}
                    </p>
                </Card>
            </Col>
            <Col span="20">
                <Row>
                    <Col span="6">
                        <span>Key：</span>
                        <Input v-model="params.key" style="width: 250px"/>
                    </Col>
                    <Col span="6">
                        <span>Value：</span>
                        <Input v-model="params.value" style="width: 250px"/>
                    </Col>

                    <Col span="4">
                        <span>状态：</span>
                        <Select clearable v-model="params.state"  @on-change="changeParam" style="width: 150px">
                            <Option v-for="item in profileStates" :value="item.stateValue"
                                    :key="item.stateValue">{{ item.stateName }}
                            </Option>
                        </Select>
                    </Col>

                    <Col span="2" style="padding-left: 10px">
                        <Button type="primary" icon="ios-search" v-on:click="changeParam">搜索</Button>
                    </Col>

                    <Col span="2" >
                        <Button type="primary" v-on:click="addAppProfile('addAppProfileItem')"><Icon type="plus-round"></Icon> 新增配置</Button>
                    </Col>
                </Row>
                <br/>
                <Row>
                    <Col span="24">
                        <Table :columns="profileColumns" :data="profileData"></Table>
                        <br/>
                        <Page :total="totalCount" @on-change="changePage" @on-page-size-change="changePageSize" size="small" show-total show-elevator show-sizer></Page>
                    </Col>

                    <Modal  :width="900"
                            @on-ok="confirmModifyProfile('formProfileItem')"
                            title="修改配置"
                            v-model="modifyProfileModel"
                            class-name="vertical-center-modal"
                            :loading="modifyLoading">
                        <Form ref="formProfileItem" :model="formProfileItem" :rules="ruleValidate" :label-width="80">
                            <FormItem label="Key：" prop="key">
                                <Input v-model="formProfileItem.key" placeholder="Enter something..." />
                            </FormItem>
                            <FormItem label="Value：" prop="value">
                                <Input v-model="formProfileItem.value" type="textarea" :autosize="{minRows: 3,maxRows: 5}" placeholder="Enter something..." />
                                <P>注意：隐藏字符（空格、换行符、制表符Tab）容易导致配置出错，如需检测Value中的隐藏字符可点击
                                    <Button type="primary" shape="circle" size="small" v-on:click="checkValue('modify')">检测隐藏字符</Button>
                                </P>
                            </FormItem>
                            <FormItem label="状态：" prop="state">
                                <Select clearable v-model="formProfileItem.state" placeholder="Select one...">
                                    <Option v-for="item in profileStates" :value="item.stateValue"
                                            :key="item.stateValue">{{ item.stateName }}
                                    </Option>
                                </Select>
                            </FormItem>
                            <FormItem label="描述：" prop="describle">
                                <Input v-model="formProfileItem.describle" placeholder="Enter something..." />
                            </FormItem>
                        </Form>
                    </Modal>

                    <Modal  :width="900"
                            @on-ok="confirmAddAppProfile('addAppProfileItem')"
                            title="新增配置"
                            v-model="addAppProfileModel"
                            class-name="vertical-center-modal"
                            :loading="addLoading">
                        <Form ref="addAppProfileItem" :model="addAppProfileItem" :rules="ruleValidate" :label-width="80">
                            <FormItem label="Key：" prop="key">
                                <Input v-model="addAppProfileItem.key" placeholder="Enter something..." />
                            </FormItem>
                            <FormItem label="Value：" prop="value">
                                <Input v-model="addAppProfileItem.value" type="textarea" :autosize="{minRows: 3,maxRows: 5}" placeholder="Enter something..." />
                                <P>注意：隐藏字符（空格、换行符、制表符Tab）容易导致配置出错，如需检测Value中的隐藏字符可点击
                                    <Button type="primary" shape="circle" size="small" v-on:click="checkValue('add')">检测隐藏字符</Button>
                                </P>
                            </FormItem>
                            <FormItem label="状态：" prop="state">
                                <Select clearable v-model="addAppProfileItem.state" placeholder="Select one...">
                                    <Option v-for="item in profileStates" :value="item.stateValue"
                                            :key="item.stateValue">{{ item.stateName }}
                                    </Option>
                                </Select>
                            </FormItem>
                            <FormItem label="描述：" prop="describle">
                                <Input v-model="addAppProfileItem.describle" placeholder="Enter something..." />
                            </FormItem>
                            <!--<FormItem label="配置拷贝：" prop="profiles">-->
                                <!--<CheckboxGroup v-model="addAppProfileItem.profiles">-->
                                    <!--<Checkbox label="dev"></Checkbox>-->
                                    <!--<Checkbox label="test"></Checkbox>-->
                                    <!--<Checkbox label="beta"></Checkbox>-->
                                    <!--<Checkbox label="exp"></Checkbox>-->
                                    <!--<Checkbox label="prod"></Checkbox>-->
                                <!--</CheckboxGroup>-->
                                <!--<P>注意：选择该项会将配置拷贝至该应用其它环境，请确保当前环境与所选环境的Value是否一致</P>-->
                            <!--</FormItem>-->
                        </Form>
                    </Modal>

                    <Modal
                            title="历史记录"
                            width="1300"
                            v-model="showAppProfileHistoryModel"
                            class-name="vertical-center-modal">
                        <Table  :columns="profileHistoryColumns" :data="profileHistoryData"></Table>
                        <br/>
                        <Page :total="hisTotalCount" :current="hisCurrPage" @on-change="changeHisPage" @on-page-size-change="changeHisPageSize" size="small" show-total show-elevator></Page>
                    </Modal>
                </Row>
            </Col>
        </Row>
    </div>
</template>
<script>
    import util from '../libs/util';
    import api from '../libs/api';

    export default {
        name: '',
        mounted: function () {
            util.setBreadcrumbs(this);
            // 取到路由带过来的参数
            let appId = this.$route.query.appId;
            // 将数据放在当前组件的数据内
            // debugger;
            this.appId = appId;
            this.params.appId = this.appId;
            this.addAppProfileItem.appId = this.appId;
            this.findAppById(this.appId);
            this.changeParam();
        },
        methods: {
            changePage (page){
                this.params.page = page - 1;
                this.changeParam();
            },
            changePageSize (count){
                this.params.count = count;
                this.changeParam();
            },
            changeHisPage (page){
                this.hisCurrPage = page;
                this.hisParams.page = page - 1;
                this.queryHistory();
            },
            changeHisPageSize (count){
                this.hisParams.count = count;
                this.queryHistory();
            },
            changeParam (){
                if (!this.params.state){
                    this.params.state = null;
                }
                this.$http.post(api.findPropertiesList, this.params)
                    .then(function (data) {
                        data = data.body;
                        if (data.code === 2000) {
                            this.profileData = data.data;
                            this.totalCount = data.page_info.total
                        } else {
                            this.$Message.error(data.msg);
                        }
                })
            },
            findAppById(appId) {
                this.$http.post(api.findAppById, {
                    'appId': appId
                }).then(function (data) {
                    data = data.body;
                    if (data.code === 2000) {
                        this.appInfo = data.data;
                    } else {
                        this.$Message.error(data.msg);
                    }
                })
            },
            deleteProfile(index, profileInfo) {
                this.$Modal.confirm({
                    title: '删除确认',
                    content: '<p>确定删除该配置？</p>' +
                    '<p>Key : ' + profileInfo.key + ' </p>' +
                    //'<p>Value : ' + profileInfo.value + '</p>' +
                    '<p>描述 : ' + profileInfo.describle + '</p>',
                    onOk: () => {
                        this.$http.post(api.deleteAppProperties, {
                            profileId: profileInfo.propertiesId
                        }).then(function (data) {
                            data = data.body;
                            if (data.code === 2000) {
                                this.$Message.success('删除成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    }
                });
            },
            disableProfile(index, profileInfo) {
                this.$Modal.confirm({
                    title: '禁用确认',
                    content: '<p>确定禁用该配置？</p>' +
                             '<p>Key : ' + profileInfo.key + ' </p>' +
                             //'<p>Value : ' + profileInfo.value + '</p>' +
                             '<p>描述 : ' + profileInfo.describle + '</p>',
                    onOk: () => {
                        this.$http.post(api.disableAppProperties, {
                            profileId: profileInfo.propertiesId
                        }).then(function (data) {
                            data = data.body;
                            if (data.code === 2000) {
                                this.$Message.success('禁用成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    }
                });
            },
            effectProfile(index, profileInfo) {
                this.$Modal.confirm({
                    title: '启用确认',
                    content: '<p>确定启用该配置？</p>' +
                             '<p>Key : ' + profileInfo.key + ' </p>' +
                             //'<p>Value : ' + profileInfo.value + '</p>' +
                             '<p>描述 : ' + profileInfo.describle + '</p>',
                    onOk: () => {
                        this.$http.post(api.effectAppProperties, {
                            profileId: profileInfo.propertiesId
                        }).then(function (data) {
                            data = data.body;
                            if (data.code === 2000) {
                                this.$Message.success('启用成功');
                                this.changeParam();
                            } else {
                                this.$Message.error(data.msg);
                            }
                        })
                    }
                })
            },
            modifyProfile(index, profileInfo) {
                this.modifyProfileModel = true;
                this.formProfileItem.profileId = profileInfo.propertiesId;
                this.formProfileItem.key = profileInfo.key;
                this.formProfileItem.value = profileInfo.value;
                this.formProfileItem.state = profileInfo.state;
                this.formProfileItem.describle = profileInfo.describle;
            },
            confirmModifyProfile(name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        this.$http.post(api.modifyAppProperties, this.formProfileItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('修改成功');
                                    this.modifyProfileModel = false;
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
            addAppProfile(name) {
                this.$refs[name].resetFields();
                this.addAppProfileModel = true;
            },
            confirmAddAppProfile(name) {
                this.$refs[name].validate(valid => {
                    if (valid) {
                        this.$http.post(api.addAppProperties, this.addAppProfileItem)
                            .then(function (data) {
                                data = data.body;
                                if (data.code === 2000) {
                                    this.$Message.success('新增成功');
                                    this.addAppProfileModel = false;
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
            showHistory(index, profileInfo) {
                this.hisCurrPage = 1;
                this.hisParams.page = this.hisCurrPage - 1;
                this.hisParams.profileId = profileInfo.propertiesId;
                this.queryHistory();
            },
            queryHistory(){
                this.$http.post(api.findHistoryByProfileId, this.hisParams)
                    .then(function (data) {
                        data = data.body;
                        if (data.code === 2000) {
                            this.profileHistoryData = data.data;
                            this.hisTotalCount = data.page_info.total
                        } else {
                            this.$Message.error(data.msg);
                        }
                        this.showAppProfileHistoryModel = true;
                })
            },
            checkValue(flag){
                let valueStr = "";
                if (flag === 'add') {
                    valueStr = this.addAppProfileItem.value;
                } else {
                    valueStr = this.formProfileItem.value;
                }
                if (valueStr === "")
                    this.$Message.info("Value为空");
                else if (valueStr.indexOf(" ") !== -1)
                    this.$Message.error("含有空格");
                else if (/\n/.test(valueStr))
                    this.$Message.error("含有换行符");
                else if (/\t/.test(valueStr))
                    this.$Message.error("含有制表符");
                else
                    this.$Message.success("无隐藏字符");
            }
        },

        data() {
            return {
                totalCount: 0,
                hisTotalCount: 0,
                hisCurrPage: 1,
                addLoading: true,
                modifyLoading: true,
                appId: '',
                appInfo: {
                    'id': '',
                    'appName': '',
                    'appProfile': '',
                    'appLabel': '',
                    'appState': 'DELETED',
                    'appVersion': '',
                    'appDesc': '',
                    'createTime': '',
                    'updateTime': ''
                },
                modifyProfileModel: false,
                addAppProfileModel: false,
                showAppProfileHistoryModel: false,
                formProfileItem: {
                    profileId: '',
                    key: '',
                    value: '',
                    state: '',
                    describle: ''
                },
                addAppProfileItem: {
                    appId: '',
                    key: '',
                    value: '',
                    describle: '',
                    state: 'DELETED'
                },
                profileStates: [
                    {
                        'stateValue': 'EFFECT',
                        'stateName': '启用'
                    },
                    {
                        'stateValue': 'DELETED',
                        'stateName': '禁用'
                    }
                ],
                profileColumns: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: 'Key',
                        key: 'key',
                        width: 240
                    },
                    {
                        title: 'Value',
                        key: 'value',
                        width: 350
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 90,
                        render: (h, param) => {
                            if (param.row.state === 'EFFECT'){
                                return h('span', '启用')
                            } else {
                                return h('span', {
                                    style: {
                                        color: ("red")
                                    }
                                },'禁用')
                            }
                        }
                    },
                    {
                        title: '描述',
                        key: 'describle',
                        width: 200
                    },
                    {
                        title: '最后修改人',
                        key: 'modifyMan',
                        width: 100
                    },
                    {
                        title: '最后修改时间',
                        key: 'updateTime',
                        width: 150
                    },
                    {
                        title: '操作',
                        key: 'opts',
                        align: 'center',
                        render: (h, param) => {
                            let stateButton = null;
                            let modifyButton = null;
                            let deleteButton = null;
                            let historyButton = null;
                            if(param.row.state === 'EFFECT'){
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
                                            this.disableProfile(param.index, param.row)
                                        }
                                    }
                                }, '禁用');
                            } else {
                                stateButton = h('Button', {
                                    props: {
                                        type: 'success',
                                        size: 'small'
                                    },
                                    style: {
                                        margin: '0 5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.effectProfile(param.index, param.row)
                                        }
                                    }
                                }, '启用');
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
                                        this.modifyProfile(param.index, param.row)
                                    }
                                }
                            }, '修改');
                            deleteButton = h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.deleteProfile(param.index, param.row)
                                    }
                                }
                            }, '删除');
                            historyButton = h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    margin: '0 5px'
                                },
                                on: {
                                    click: () => {
                                        this.showHistory(param.index, param.row)
                                    }
                                }
                            }, '修改历史');

                            return h('div', [stateButton, modifyButton, deleteButton, historyButton]);
                        }
                    }
                ],
                profileData: [],
                profileHistoryColumns: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: 'Key',
                        key: 'key',
                        width: 240
                    },
                    {
                        title: 'Value',
                        key: 'value',
                        width: 320
                    },
                    {
                        title: '状态',
                        key: 'state',
                        width: 100,
                        render: (h, param) => {
                            if (param.row.state === 'EFFECT'){
                                return h('span', '启用')
                            } else {
                                return h('span', {
                                    style: {
                                        color: ("red")
                                    }
                                },'禁用')
                            }
                        }
                    },
                    {
                        title: '描述',
                        key: 'describle',
                        width: 200
                    },
                    {
                        title: '是否删除',
                        key: 'deleted',
                        width: 90,
                        align: 'center',
                        render: (h, param) => {
                            if (param.row.deleted === false){
                                return h('div', '否')
                            } else {
                                return h('div', '是')
                            }
                        }
                    },
                    {
                        title: '最后修改人',
                        key: 'modifyMan',
                        width: 100
                    },
                    {
                        title: '最后修改时间',
                        key: 'updateTime',
                        width: 150
                    }
                ],
                profileHistoryData: [],
                ruleValidate: {
                    key: [
                        {required: true, message: "Key不能为空"}
                    ],
                    value: [
                        {required: true, message: "Value不能为空"}
                    ],
                    state: [
                        {required: true, message: "请选择状态"}
                    ]
                },
                params: {
                    page: 0,
                    count: 10,
                    appId: '',
                    state: null,
                    key: '',
                    value: ''
                },
                hisParams:{
                    page: 0,
                    count: 10,
                    profileId: ''
                }
            }
        }
    }
</script>