<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>应用配额列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">

                <el-col :span="4">

                    <el-select v-model="queryItem.appId"
                               placeholder="输入应用名"
                               filterable
                               remote
                               :remote-method="handleAppAutoPrompt" @change="onSearch">
                        <el-option v-for="item in appsByName"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.appId">
                        </el-option>

                    </el-select>

                </el-col>

                <el-col :span="4">
                    <el-select v-model="queryItem.envId" placeholder="选择环境" @change="onSearch">
                        <el-option v-for="item in envs" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-col>

                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>

                <el-button type="primary" @click="addAppQuota" style="float: right;" v-if="isAdmin">添加应用配额</el-button>
                <el-button type="primary" @click="initAppQuota" style="float: right; margin-right: 10px;"
                           v-if="isAdmin">创建默认配额
                </el-button>
                <el-button type="danger" @click="initAllQuota" style="float: right;" v-if="isAdmin">初始化</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="appquotas" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="应用ID" prop="appId" align="center"></el-table-column>
                <el-table-column label="应用名" prop="appName" align="center"></el-table-column>
                <el-table-column label="环境" prop="envName" align="center"></el-table-column>
                <el-table-column label="规格" prop="spectypeName" align="center"></el-table-column>
                <el-table-column label="数量" prop="number" align="center"></el-table-column>

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

        <el-dialog title="应用配额信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditAppQuota" ref="inEditAppQuotaForm"
                     :rules="rules">

                <el-form-item label="应用名" prop="appName">

                    <el-select v-model="inEditAppQuota.appName"
                               placeholder="输入应用名"
                               filterable
                               remote
                               :remote-method="handleAppAutoPrompt" :disabled="inEditAppQuota.id != null">
                        <el-option v-for="item in appsByName"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.name">
                        </el-option>

                    </el-select>

                </el-form-item>

                <el-form-item label="环境名称" prop="envId">
                    <el-select v-model="inEditAppQuota.envId" placeholder="请选择" :disabled="inEditAppQuota.id != null">
                        <el-option v-for="item in envs" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="规格" prop="spectypeId">
                    <el-select v-model="inEditAppQuota.spectypeId" placeholder="请选择"
                               :disabled="inEditAppQuota.id != null">
                        <el-option v-for="item in specTypes" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="数量" prop="number">
                    <el-input v-model="inEditAppQuota.number" type="number"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditAppQuotaForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditAppQuota.id">重置</el-button>
                    <el-button @click="onClose" class="close-button">关闭</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-dialog title="创建默认配额" :visible.sync="initDialogVisible" :before-close="onCloseInit">
            <el-form label-width="80px" label-position="right" :model="inInitAppQuota" ref="inInitAppQuotaForm"
                     :rules="initRules">
                <el-form-item label="应用名" prop="appId">
                    <el-select v-model="inInitAppQuota.appId"
                               placeholder="输入应用名"
                               filterable
                               remote
                               :remote-method="handleAppAutoPrompt">
                        <el-option v-for="item in appsByName"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.appId">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmitInit">创建</el-button>
                    <el-button @click="onCloseInit" class="close-button">关闭</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>
    </div>
</template>

<script>

    import {mapGetters, mapActions} from 'vuex';

    export default {

        methods: {
            handleEdit(quotaDto) {
                //debugger
                this.inEditAppQuota = {
                    id: quotaDto.id,
                    appId: quotaDto.appId,
                    appName: quotaDto.appName,
                    orgId: quotaDto.orgId,
                    envId: quotaDto.envId,
                    envName: quotaDto.envName,
                    spectypeId: quotaDto.spectypeId,
                    spectypeName: quotaDto.spectypeName,
                    number: quotaDto.number,

                };
                this.dialogVisible = true;
            },

            handleDelete(quotaId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            appQuotaId: quotaId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                            appQuotaQuery: this.queryItem
                        };
                        this.$store.dispatch('deleteAppQuotaById', data);
                    })
                    .catch(() => {
                    });
            },

            //根据输入部分appName值异步自动弹出以其开头的appName
            handleAppAutoPrompt(data) {
                if (data !== '') {
                    this.$store.dispatch('fuzzyQueryAppsByAppName', {appName: data});
                }
            },

            handleSizeChange(data) {
                this.pageSize = data;

                this.$store.dispatch('queryAppQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    appQuotaQuery: this.queryItem,
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;

                this.$store.dispatch('queryAppQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    appQuotaQuery: this.queryItem,
                });

            },

            addAppQuota() {
                this.inEditAppQuota = {
                    id: null,
                    appId: null,
                    appName: '',
                    orgId: null,
                    envId: null,
                    envName: '',
                    spectypeId: null,
                    spectypeName: '',
                    number: null,
                };
                this.dialogVisible = true;
            },
            initAllQuota() {
                this.$confirm('确认初始化所有应用的配额？').then(() => {
                    let data = {
                        appQuotaQuery: this.queryItem,
                        pageSize: this.pageSize,
                        page: this.currentPage,
                    };
                    this.$store.dispatch('initAllQuotas', data);
                }).catch(() => {
                });
            },
            initAppQuota() {
                this.initDialogVisible = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        this.dialogVisible = false;
                        let data = {
                            appQuotaQuery: this.queryItem,
                            newAppQuota: self.inEditAppQuota,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newAppQuota.id == null) {
                            this.$store.dispatch('createNewAppQuota', data);
                        } else {
                            this.$store.dispatch('updateAppQuota', data);
                        }
                    } else {
                        return false;
                    }
                });
            },
            onClose() {
                this.dialogVisible = false;
            },
            onSubmitInit() {
                let self = this;
                this.$refs['inInitAppQuotaForm'].validate((valid) => {
                    if (valid) {
                        let data = {
                            initAppQuota: self.inInitAppQuota,
                            appQuotaQuery: this.queryItem,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('initAppQuotas', data);
                        this.onCloseInit();
                    } else {
                        return false;
                    }
                });
            },
            onCloseInit() {
                this.initDialogVisible = false;
                this.$refs['inInitAppQuotaForm'].resetFields();
            },
            onSearch() {
                this.currentPage = 1;
                this.$store.dispatch('queryAppQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    appQuotaQuery: this.queryItem,
                });
            },
            resetForm() {
                this.inEditAppQuota = {
                    id: null,
                    appId: null,
                    appName: '',
                    orgId: null,
                    envId: null,
                    envName: '',
                    spectypeId: null,
                    spectypeName: '',
                    number: null,
                };
            },
            resetQueryForm() {
                this.queryItem = {
                    appId: '',
                    envId: null
                };
                this.onSearch();
            },

            remoteMethod(query) {
                if (query !== '') {
                    this.loading = true;
                    setTimeout(() => {
                        this.loading = false;
                        this.filteredAppList = this.appList.filter(item => {
                            return item.label.toLowerCase()
                                .indexOf(query.toLowerCase()) > -1;
                        });
                    }, 200);
                } else {
                    this.filteredAppList = [];
                }
            }

        },

        data: function () {
            return {
                //auto complete in app name
                appList: [],
                filteredAppList: [],
                loading: false,

                dialogVisible: false,
                initDialogVisible: false,
                currentField: 'id',
                currentValue: '',
                queryItem: {
                    appId: '',
                    envId: null,
                },
                fieldList: [{
                    value: 'id',
                    label: 'ID'
                }, {
                    value: 'name',
                    label: '组织名称'
                }],
                currentPage: 1,
                pageSize: 10,
                inEditAppQuota: {
                    id: null,
                    appId: null,
                    appName: '',
                    orgId: null,
                    envId: null,
                    envName: '',
                    spectypeId: null,
                    spectypeName: '',
                    number: null,
                },
                inInitAppQuota: {
                    appId: ''
                },
                rules: {

                    envId: [
                        {type: 'number', required: true, message: 'envId不能为空', trigger: 'blur'}
                    ],
                    spectypeId: [
                        {type: 'number', required: true, message: 'specTypeId不能为空', trigger: 'blur'}
                    ],
                    number: [
                        {required: true, message: 'number不能为空', trigger: 'blur'}
                    ],
                    appId: [
                        {required: true, message: 'appId不能为空', trigger: 'blur'}
                    ],
                    appName: [
                        {required: true, message: 'appName不能为空', trigger: 'blur'}
                    ]
                },
                initRules: {
                    appId: [
                        {required: true, message: 'appId不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                appquotas: 'getAllAppQuotas',
                envs: 'getAllEnvs',
                appsByName: 'getAppsByName',
                specTypes: 'getAllSpecTypes',
                total: 'getTotalAppQuotas',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllAppQuotas', {
                pageSize: this.pageSize,
                page: this.currentPage
            });
            this.$store.dispatch('fetchAllEnvs', {
                pageSize: 999,
                page: 1
            });
            this.$store.dispatch('fetchAllSpecTypes', {
                pageSize: 999,
                page: 1
            });
        }

    }

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
</style>

