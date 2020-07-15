<template>
    <div class="app-table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>应用列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">

                <el-col :span="4">
                    <!--<el-input v-model="queryItem.appName" placeholder="输入应用名"></el-input>-->

                    <el-select v-model="queryItem.appName"
                               placeholder="输入应用名"
                               filterable
                               remote
                               :remote-method="handleAppAutoPrompt" @change="onSearch">
                        <el-option v-for="item in appsByName"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.name">
                        </el-option>

                    </el-select>

                </el-col>

                <el-col :span="4">
                    <el-input v-model="queryItem.appId" placeholder="输入AppId"></el-input>
                </el-col>

                <el-col :span="4">
                    <el-select v-model="queryItem.orgId" placeholder="选择组织" @change="onSearch">
                        <el-option v-for="item in orgs"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-col>

                <el-col :span="4">
                    <el-select v-model="queryItem.developers"
                               placeholder="输入负责人"
                               filterable
                               remote
                               :remote-method="handleUserAutoPrompt" @change="onSearch">
                        <el-option v-for="item in usersByName"
                                   :key="item.workNumber"
                                   :label="item.userName"
                                   :value="item.workNumber">
                        </el-option>
                    </el-select>
                </el-col>

                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>
                <el-button type="primary" fixed="right" @click="addApp" class="add_button" v-if="isAdmin">添加应用</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="apps" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <h5 style="margin-bottom: 10px">应用URL</h5>
                            <div v-for="envUrl in props.row.envUrls" style="height: 20px;">
                                <label style="font-weight: bold">{{ envUrl.envName + ":"}}</label>
                                <span style="font:italic bold 12px/30px arial,sans-serif;">{{ envUrl.url }} </span>
                            </div>
                        </el-form>
                    </template>
                </el-table-column>

                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="应用名称" prop="name" align="center"></el-table-column>
                <el-table-column label="应用ID" prop="appId" align="center"></el-table-column>
                <el-table-column label="组织名称" prop="orgDto.name" align="center"></el-table-column>

                <el-table-column label="开启HA" align="center">
                    <template slot-scope="props">
                        <el-tag type="success" v-if="props.row.enableHa">是</el-tag>
                        <el-tag type="danger" v-else>否</el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="负责人" align="center">
                    <template slot-scope="props">
                        <label v-for="item in props.row.userDtos">{{item.realName + " "}}</label>
                    </template>
                </el-table-column>

                <el-table-column label="测试人员" align="center">
                    <template slot-scope="props">
                        <label v-for="item in props.row.testUserDtos">{{item.realName + " "}}</label>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="center" width="200px" v-if="isAdmin">
                    <template slot-scope="props">
                        <el-button @click="handleEdit(props.row)" size="small">编辑</el-button>
                        <el-button @click="handleDelete(props.row.id)" type="danger" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div align='center' style="margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size.sync="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="应用信息" :visible.sync="dialogVisible" top="50px">
            <el-form label-width="80px" label-position="center" :model="inEditApp" ref="inEditAppForm" :rules="rules">
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditApp.name" @change="changeAppName"></el-input>
                </el-form-item>

                <el-form-item label="应用ID" prop="appId">
                    <el-input v-model="inEditApp.appId"></el-input>
                </el-form-item>

                <el-form-item label="所属组织" prop="orgId" class="app-drop-down">
                    <el-select v-model="inEditApp.orgId" placeholder="选择所属组织">
                        <el-option v-for="item in orgs" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="环境Url" prop="envUrls" class="app-drop-down">

                    <el-select v-model="inEditAddEnvName" placeholder="选择环境" @change="addEnvUrl(inEditAddEnvName)"
                               class="selectEnv">
                        <el-option v-for="item in tempEnvs" :key="item.envName" :label="item.envName"
                                   :value="item.envName">
                        </el-option>
                    </el-select>

                    <div v-for="envUrl in inEditEnvUrls">
                        <el-form-item>
                            <el-row>
                                <el-col :span="4">
                                    <!--<span>{{envUrl.envName}}</span>-->
                                    <el-input v-model="envUrl.envName" :readonly="true"></el-input>
                                </el-col>
                                <el-col :span="16">
                                    <el-input v-model="envUrl.url" placeholder="输入url"></el-input>
                                </el-col>
                                <el-col :span="4">
                                    <el-button @click="handleDeleteEnvUrl(envUrl.envName)" type="danger" size="small"
                                               class="deleteEnv">删除
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-form-item>
                    </div>
                </el-form-item>

                <el-form-item label="开启HA" prop="enableHa">
                    <el-switch
                            v-model="inEditApp.enableHa"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                </el-form-item>

                <el-form-item label="测试人员" prop="tests">
                    <el-select v-model="inEditApp.tests"
                               placeholder="输入测试人员"
                               multiple
                               filterable
                               remote
                               :remote-method="handleTestUserAutoPrompt">
                        <el-option v-for="item in testUsersByName"
                                   :key="item.workNumber"
                                   :label="item.userName"
                                   :value="item.workNumber">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="负责人" prop="developers">
                    <el-select v-model="inEditApp.developers"
                               placeholder="输入负责人"
                               multiple
                               filterable
                               remote
                               :remote-method="handleUserAutoPrompt">
                        <el-option v-for="item in usersByName"
                                   :key="item.workNumber"
                                   :label="item.userName"
                                   :value="item.workNumber">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="描述" prop="description">
                    <el-input v-model="inEditApp.description" type="textarea"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditAppForm')">申请</el-button>
                    <el-button @click="resetForm" v-if="!inEditApp.id">重置</el-button>
                    <el-button @click="onClose" class="close-button">关闭</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';

    export default {

        methods: {
            zoneTypeFormat:function(row, column) {
                var type = row[column.property];
                if (type == 'capital') {
                    return '资金专区';
                }

                return '默认';
            },
            handleEdit(appDto) {
                this.inEditApp = {
                    id: appDto.id,
                    name: appDto.name,
                    appId: appDto.appId,
                    appType: appDto.appType,
                    orgId: appDto.orgId,
                    appLevel: appDto.appLevel,
                    influenceScope: appDto.influenceScope,
                    serviceType: appDto.serviceType,
                    zoneType: appDto.zoneType,
                    enableHa: appDto.enableHa,
                    //组件中用数组保存 后台返回的包含多个工号的字符串,在发送给后台时再重新转化成字符串
                    developers: appDto.developers.split(","),
                    tests: appDto.tests != "" ? appDto.tests.split(",") : [],
                    envUrls: appDto.envUrls,
                    description: appDto.description,
                };

                //初始化 inEditEnvUrls, 并用中间变量实现，防止不能更改
                //清除原始数据,否则数组不断 append
                this.inEditEnvUrls = [];

                let data = {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                };

                this.tempEnvs = [];
                //复制env name 数组
                for (var item of this.envs) {
                    let envItem = {
                        envName: item.name,
                    };
                    this.tempEnvs.push(envItem);
                }

                if (appDto.envUrls != null) {
                    for (var value of appDto.envUrls) {

                        //去掉已存在的env 元素,作为下拉框内容
                        this.tempEnvs.splice(this.tempEnvs.findIndex(item => item.envName === value.envName), 1);

                        //构建每个对象Item插入到数组中, 避免引用传递
                        let envItem = {
                            envName: value.envName,
                            url: value.url
                        };
                        //edit 时用于展现所有已存在 的env-url
                        this.inEditEnvUrls.push(envItem);
                    }
                }

                //用当前 app 的负责人初始化 usersByName
                this.$store.dispatch('initUsersByName', appDto.userDtos);

                this.$store.dispatch('initTestUsersByName', appDto.testUserDtos);

                //每次清空下拉框保留选项
                this.inEditAddEnvName = null;
                this.dialogVisible = true;
            },

            addEnvUrl(envName) {
                //获取下拉框所选 envName, 然后添加该 env-url item
                let newEnvItem = {
                    envName: envName,
                    url: ""
                };
                //添加一条env-url时, 加入到 inEditEnvUrls
                this.inEditEnvUrls.push(newEnvItem);

            },

            handleDeleteEnvUrl(envName) {
                //删除一条env-url, 从 inEditEnvUrls 去除改名字的 env-url
                this.inEditEnvUrls.splice(this.inEditEnvUrls.findIndex(item => item.envName === envName), 1);
            },

            handleDelete(appId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            appId: appId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteAppById', data);
                    })
                    .catch(() => {
                    });
            },

            handleSizeChange(data) {
                this.pageSize = data;

                this.$store.dispatch('queryApps', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryApp: this.queryItem
                });

            },

            handleCurrentChange(data) {
                this.currentPage = data;

                this.$store.dispatch('queryApps', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryApp: this.queryItem
                });

            },

            //根据输入部分appName值异步自动弹出以其开头的appName
            handleAppAutoPrompt(data) {
                if (data !== '') {
                    this.$store.dispatch('fuzzyQueryAppsByAppName', {appName: data});
                }
            },

            //根据输入值异步自动弹出appName
            handleAppAutoPromptAppId(data) {
                if (data !== '') {
                    this.$store.dispatch('queryAppsByAppId', {appId: data});
                }
            },

            //根据输入值异步自动弹出userName
            handleUserAutoPrompt(data) {
                if (data !== '') {
                    this.$store.dispatch('queryUsersByUserName', {userName: data});
                }
            },

            //根据输入值异步自动弹出测试userName
            handleTestUserAutoPrompt(data) {
                if (data !== '') {
                    this.$store.dispatch('queryUsersByUserName', {userName: data, type: "test"});
                }
            },

            addApp() {
                this.inEditApp = {
                    id: null,
                    name: '',
                    appId: '',
                    appType: 'SpringBoot',
                    orgId: null,
                    appLevel: '核心业务',
                    influenceScope: '',
                    serviceType: '服务提供',
                    zoneType: 'default',
                    enableHa: true,
                    developers: [],
                    tests: [],
                    envUrls: [],
                    description: ''
                };

                //每次清空下拉框保留选项
                this.inEditAddEnvName = null;

                //添加新 app 时, 先清空 tempEnvs
                this.tempEnvs = [];
                //从envs 复制所有 env name, 用于下拉框展示所有可选 env
                for (var item of this.envs) {
                    let envItem = {
                        envName: item.name,
                    };
                    this.tempEnvs.push(envItem);
                }

                //同时清空某个app当前已存在的env-url
                this.inEditEnvUrls = [];

                for (let env of this.envs) {
                    let envItem = {
                        envName: env.name,
                        url: ''
                    };
                    this.inEditEnvUrls.push(envItem);
                }

                this.dialogVisible = true;
            },

            changeAppName(value) {
                for (let inEditEnvUrl of this.inEditEnvUrls) {
                    inEditEnvUrl.url = value;
                }
            },

            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        for (var item of this.inEditEnvUrls) {
                            if (item.url == null || item.url === '') {
                                //alert("url can not be empty");
                                this.$message.error(
                                    'url can not be empty',
                                );
                                return;
                            }
                        }
                        this.dialogVisible = false;
                        //设置 env url
                        self.inEditApp.envUrls = this.inEditEnvUrls;
                        //组件中是用数组暂存工号的，交给后台转化 工号数组 为字符串, 同时支持add/update
                        //转成**,**,**格式
                        let workNumbers = self.inEditApp.developers.join();
                        self.inEditApp.developers = workNumbers;

                        let testUserNumbers = self.inEditApp.tests.join();
                        self.inEditApp.tests = testUserNumbers;

                        let data = {
                            //用以搜索后编辑结束后的刷新
                            queryApp: this.queryItem,
                            newApp: self.inEditApp,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newApp.id == null) {
                            this.$store.dispatch('createNewApp', data);
                        } else {
                            this.$store.dispatch('updateApp', data);
                        }
                    } else {
                        return false;
                    }
                });
            },

            appNameCheck() {
                this.$store.dispatch('queryAppsByAppName', data);
                //获取apps 判断名字是否重复，然后弹出警示消息
            },

            onClose() {
                this.dialogVisible = false;
            },

            onSearch() {
                this.currentPage = 1;
                this.$store.dispatch('queryApps', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryApp: this.queryItem
                });
            },
            resetForm() {
                this.inEditApp = {
                    id: null,
                    name: '',
                    appId: '',
                    appType: '',
                    orgId: null,
                    appLevel: '',
                    influenceScope: '',
                    serviceType: '',
                    zoneType: null,
                    enableHa: false,
                    developers: [],
                    envUrls: [],
                    tests: [],
                    description: ''
                }
            },
            resetQueryForm() {
                this.queryItem = {
                    appId: null,
                    appName: '',
                    orgId: null,
                    developers: ''
                };
                this.onSearch();
            }
        },

        data: function () {
            return {
                dialogVisible: false,
                currentField: 'appName',
                currentValue: '',

                queryItem: {
                    appId: null,
                    appName: '',
                    orgId: null,
                    developers: ''
                },

                currentPage: 1,
                pageSize: 10,

                inEditEnvUrls: [],
                //用于下拉框添加envUrl 中间temp 变量使用
                inEditAddEnvName: null,

                tempEnvs: [],

                inEditApp: {
                    id: null,
                    name: '',
                    appId: '',
                    appType: '',
                    orgId: null,
                    appLevel: '',
                    influenceScope: '',
                    serviceType: '',
                    zoneType: null,
                    enableHa: false,
                    //多个负责人,用数组表示
                    developers: [],
                    tests: [],
                    envUrls: [],
                    description: ''
                },
                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ],
                    appId: [
                        {required: true, message: 'appId不能为空', trigger: 'blur'}
                    ],
                    appType: [
                        {required: true, message: '应用类型不能为空', trigger: 'blur'}
                    ],
                    orgId: [
                        {type: 'number', required: true, message: 'orgId不能为空', trigger: 'blur'}
                    ],
                    developers: [
                        {required: true, message: '负责人不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                apps: 'getAllApps',
                appsFuzzyByName: 'getFuzzyAppsByName',
                appsByName: 'getAppsByName',
                usersByName: 'getUsersByName',
                //搜索test负责人
                testUsersByName: 'getTestUsersByName',
                total: 'getTotalApps',
                orgs: 'getAllOrgs',
                envs: 'getAllEnvs',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllApps', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllOrgs', {
                pageSize: 9999,
                page: 1,
            });
            this.$store.dispatch('fetchAllEnvs', {
                pageSize: 9999,
                page: 1,
            });
        }
    };

</script>

<style>
    .query-form {
        margin-top: 20px;
        padding: 25px;
        background-color: #eef1f6;
    }

    .close-button {
        float: right;
    }

    .add_button {
        float: right;
    }

    div.el-form-item:last-child {
        margin-bottom: 0;
    }

    /*    .el-dialog__body {
            padding: 30px 200px;
        }*/

    .app-drop-down .el-select {
        width: 100%;
    }

    .selectEnv {
        margin-bottom: 5px;
    }

    .deleteEnv {
        margin-left: 2px;
        height: 38px;
        width: 76px;
    }

    .app-table-scroll-div {
        max-width: 1600px;
        min-width: 1200px;
    }

</style>

