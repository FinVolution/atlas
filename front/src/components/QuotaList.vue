<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>配额列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <el-col :span="4">

                    <el-select v-model="queryItem.orgName" placeholder="选择组织" @change="onSearch">
                        <el-option v-for="item in orgs"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.name"></el-option>
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
                <el-button type="primary" fixed="right" @click="addQuota" class="add_button" v-if="isAdmin">添加配额
                </el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="quotas" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <!--<el-table-column label="应用Id" prop="orgDto.orgId" align="center"></el-table-column>-->
                <el-table-column label="组织" prop="orgDto.name" align="center"></el-table-column>
                <el-table-column label="环境" prop="envDto.name" align="center"></el-table-column>

                <!--                <el-table-column label="规格类型" prop="specTypeDto.name" align="center"></el-table-column>
                                <el-table-column label="配额总量" prop="limit" align="center"></el-table-column>
                                <el-table-column label="配额余额" prop="balance" align="center"></el-table-column>-->

                <el-table-column label="cpu(核)" prop="cpu" align="center"></el-table-column>
                <el-table-column label="内存(MB)" prop="memory" align="center"></el-table-column>
                <el-table-column label="磁盘(MB)" prop="disk" align="center"></el-table-column>

                <el-table-column label="描述" prop="description" align="center"></el-table-column>
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

        <el-dialog title="配额信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditQuota" ref="inEditQuotaForm"
                     :rules="rules">

                <el-form-item label="组织" prop="orgId">
                    <el-select v-model="inEditQuota.orgId"
                               placeholder="请选择"
                               filterable>
                        <el-option v-for="item in orgs"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">

                        </el-option>
                    </el-select>
                    <!--<el-input v-model="inEditQuota.limit"></el-input>-->
                </el-form-item>

                <el-form-item label="环境名称" prop="envId">
                    <el-select v-model="inEditQuota.envId" placeholder="请选择">
                        <el-option v-for="item in envs" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="cpu(核)" prop="cpu">
                    <el-input v-model="inEditQuota.cpu" type="number"></el-input>
                </el-form-item>

                <el-form-item label="内存(MB)" prop="memory">
                    <el-input v-model="inEditQuota.memory" type="number"></el-input>
                </el-form-item>

                <el-form-item label="磁盘(MB)" prop="disk">
                    <el-input v-model="inEditQuota.disk" type="number"></el-input>
                </el-form-item>

                <el-form-item label="描述" prop="description">
                    <el-input v-model="inEditQuota.description" type="textarea"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditQuotaForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditQuota.id">重置</el-button>
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
            handleEdit(quotaDto) {
                this.dialogVisible = true;
                //debugger
                this.inEditQuota = {
                    id: quotaDto.id,
                    orgId: quotaDto.orgId,
                    //orgName: quotaDto.appDto.name,
                    envId: quotaDto.envId,
                    //envName: quotaDto.envDto.name,
                    //specTypeId: quotaDto.specTypeId,
                    cpu: quotaDto.cpu,
                    memory: quotaDto.memory,
                    disk: quotaDto.disk,
                    description: quotaDto.description
                };
            },
            handleDelete(quotaId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            quotaId: quotaId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteQuotaById', data);
                    })
                    .catch(() => {
                    });
            },

            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('queryQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    quotaQuery: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('queryQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    quotaQuery: this.queryItem
                });
            },

            addQuota() {
                this.inEditQuota = {
                    id: null,
                    orgId: null,
                    envId: null,
                    cpu: null,
                    memory: null,
                    disk: null,
                    description: ''
                };
                this.dialogVisible = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        this.dialogVisible = false;
                        let data = {
                            quotaQuery: this.queryItem,
                            newQuota: self.inEditQuota,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newQuota.id == null) {
                            this.$store.dispatch('createNewQuota', data);
                        } else {
                            this.$store.dispatch('updateQuota', data);
                        }
                    } else {
                        return false;
                    }
                });
            },
            onClose() {
                this.dialogVisible = false;
            },
            onSearch() {
                this.currentPage = 1;
                this.$store.dispatch('queryQuotas', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    quotaQuery: this.queryItem,
                });
            },
            resetForm() {
                this.inEditQuota = {
                    id: null,
                    orgId: null,
                    //orgName: '',
                    envId: null,
                    //envName: '',
                    cpu: null,
                    memory: null,
                    disk: null,
                    description: ''
                };
            },
            resetQueryForm() {
                this.queryItem = {
                    orgName: '',
                    envId: null,
                };
                this.onSearch()
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
                currentField: 'id',
                currentValue: '',
                queryItem: {
                    //orgId: null,
                    orgName: '',
                    envId: null,
                    //specTypeId: null,
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
                inEditQuota: {
                    id: null,
                    orgId: null,
                    //orgName: '',
                    envId: null,
                    //envName: '',
                    cpu: null,
                    memory: null,
                    disk: null,
                    description: ''
                },
                rules: {
                    orgId: [
                        {type: 'number', required: true, message: 'orgId不能为空', trigger: 'blur'}
                    ],
                    envId: [
                        {type: 'number', required: true, message: 'envId不能为空', trigger: 'blur'}
                    ],
                    specTypeId: [
                        {type: 'number', required: true, message: 'specTypeId不能为空', trigger: 'blur'}
                    ],
                    cpu: [
                        {required: true, message: 'cpu 不能为空', trigger: 'blur'}
                    ],
                    memory: [
                        {required: true, message: 'memory 不能为空', trigger: 'blur'}
                    ],
                    disk: [
                        {required: true, message: 'disk 不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                quotas: 'getAllQuotas',
                envs: 'getAllEnvs',
                apps: 'getAllApps',
                orgs: 'getAllOrgs',
                specTypes: 'getAllSpecTypes',
                total: 'getTotalQuotas',
                isAdmin: 'getAdminResult'
            }),
            appList2: function () {
                let data = this.apps.map(item => {
                    return {
                        value: item.id,
                        label: item.name,
                        id: item.id
                    };
                });
                return data;
            }
        },
        created() {
            this.$store.dispatch('fetchAllQuotas', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllEnvs', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllSpecTypes', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllApps', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllOrgs', {
                pageSize: this.pageSize,
                page: this.currentPage,
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

