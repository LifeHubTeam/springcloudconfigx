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

            <Col span="4">
                <span>应用状态：</span>
                <Select clearable v-model="params.appState" placeholder="" @on-change="changeParam" style="width: 150px">
                    <Option v-for="item in appStatus" :value="item.statusValue" :key="item.statusValue">{{ item.statusName }}</Option>
                </Select>
            </Col>
        </Row>
        <br/>
        <Row>
            <Col span="5">
                <span>Key：</span>
                <Input v-model="params.key" style="width: 300px"/>
            </Col>

            <Col span="6">
                <span>Value：</span>
                <Input v-model="params.value" style="width: 370px"/>
            </Col>

            <Col span="4">
                <span>配置状态：</span>
                <Select clearable v-model="params.profileState" placeholder="" @on-change="changeParam" style="width: 150px">
                    <Option v-for="item in profileStates" :value="item.stateValue" :key="item.stateValue">{{ item.stateName }}</Option>
                </Select>
            </Col>

            <Col span="2">
                <Button type="primary" icon="ios-search" v-on:click="changeParam">搜索</Button>
            </Col>
        </Row>

        <br/>

        <Row>
            <Col span="24">
                <Table stripe :columns="profileCloumeNames" :data="profileDetailData"></Table>
                <br/>
                <Page :total="totalCount" @on-change="changePage" @on-page-size-change="changePageSize" size="small" show-total show-elevator show-sizer></Page>
            </Col>

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
    </div>
</template>

<script>
    import expandRow from './profileExpand.vue';
    import api from '../libs/api';

    export default {
        components: {
            expandRow
        },
        mounted: function () {
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
        methods: {
            changeParam: function () {
                if(this.params.appProfile === ''){
                    this.params.appProfile = null;
                }
                if(this.params.appState === ''){
                    this.params.appState = null;
                }
                if (this.params.profileState === ''){
                    this.params.profileState = null;
                }
                this.$http.post(api.findAllProperties, this.params)
                    .then(function (data) {
                        data = data.body;
                        if(data.code === 2000){
                            this.profileDetailData = data.data;
                            this.totalCount = data.page_info.total
                        } else {
                            this.$Message.error(data.msg);
                        }
                })
            },
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
            showHistory(index, profileInfo) {
                this.hisCurrPage = 1;
                this.hisParams.page = this.hisCurrPage - 1;
                this.hisParams.profileId = profileInfo.profileId;
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
        },
        data () {
            return {
                totalCount: 0,
                hisTotalCount: 0,
                hisCurrPage: 1,
                showAppProfileHistoryModel: false,
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
                profileStates: [
                    {
                        stateValue: 'EFFECT',
                        stateName: '启用'
                    },
                    {
                        stateValue: 'DELETED',
                        stateName: '禁用'
                    }
                ],
                profileDetailData: [],
                profileCloumeNames: [
                    {
                        type: 'expand',
                        width: 50,
                        render: (h, params) => {
                            return h(expandRow, {
                                props: {
                                    row: params.row
                                }
                            })
                        }
                    },
                    {
                        title: '序号',
                        type: 'index',
                        width: 80,
                        align: 'center'
                    },
                    {
                        title: '应用名',
                        key: 'appName',
                        width: 200
                    },
                    {
                        title: '环境',
                        key: 'appProfile',
                        width: 100
                    },
                    {
                        title: '标签',
                        key: 'appLabel',
                        width: 100
                    },
                    {
                        title: '应用状态',
                        key: 'appState',
                        width: 100,
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
                        title: 'Key',
                        key: 'key',
                        width: 280
                    },
                    {
                        title: 'Value',
                        key: 'value',
                        width: 350
                    },
                    {
                        title: '配置状态',
                        key: 'profileState',
                        width: 100,
                        render: (h, param) => {
                            if (param.row.profileState === 'EFFECT'){
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
                        key: 'profileDesc',
                        width: 250
                    },
                    {
                        title: '操作',
                        key: 'opts',
                        align: 'center',
                        render: (h, param) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'info',
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
                                }, '修改历史')
                            ])
                        }
                    }
                ],
                profileHistoryData: [],
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
                params: {
                    page: 0,
                    count: 10,
                    appName: '',
                    appProfile: null,
                    appLabel: '',
                    appState: null,
                    key: '',
                    value: '',
                    profileState: null
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
