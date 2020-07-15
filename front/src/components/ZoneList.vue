<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>zone列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <el-col :span="4">
                    <el-select v-model="currentEnv" placeholder="选择环境" @change="onSearch">
                        <el-option v-for="item in envs" :key="item.id" :label="item.name"
                                   :value="item.name"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="currentValue" placeholder="输入zone名称"></el-input>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>
                <el-button type="primary" fixed="right" @click="addZone" class="add_button" v-if="isAdmin">添加zone
                </el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="zones" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable width="80px"></el-table-column>
                <el-table-column label="环境" prop="envName" align="center"></el-table-column>
                <el-table-column label="zone名称" prop="name" align="center"></el-table-column>
                <el-table-column label="k8shttp" prop="k8s" align="center"></el-table-column>
                <el-table-column label="k8srpc" prop="extensions.rpc" align="center"></el-table-column>
                <el-table-column label="所属网络" prop="extensions.network" align="center"></el-table-column>
                <el-table-column label="k8s版本" prop="k8sVersion" align="center"></el-table-column>
                <el-table-column label="dashboard" prop="extensions.dashboard" align="center"></el-table-column>
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

        <el-dialog title="zone信息" :visible.sync="dialogVisible">
            <el-form label-width="100px" label-position="right" :model="inEditZone" ref="inEditZoneForm"
                     :rules="rules">
                <el-form-item label="环境" prop="env">
                    <el-select v-model="inEditZone.env" placeholder="请选择" value-key="id">
                        <el-option v-for="item in envs" :key="item.id" :label="item.name"
                                   :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditZone.name"></el-input>
                </el-form-item>
                <el-form-item label="k8shttp" prop="k8s">
                    <el-input v-model="inEditZone.k8s"></el-input>
                </el-form-item>
                <el-form-item label="k8srpc" prop="rpc">
                    <el-input v-model="inEditZone.extensions.rpc"></el-input>
                </el-form-item>
                <el-form-item label="所属网络" prop="network">
                    <el-input v-model="inEditZone.extensions.network"></el-input>
                </el-form-item>
                <el-form-item label="k8s版本" prop="version">
                    <el-input v-model="inEditZone.k8sVersion"></el-input>
                </el-form-item>
                <el-form-item label="dashboard" prop="dashboard">
                    <el-input v-model="inEditZone.extensions.dashboard"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="inEditZone.description" type="textarea"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditZoneForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditZone.id">重置</el-button>
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
            handleEdit(zoneDto) {
                this.dialogVisible = true;
                this.inEditZone = {
                    id: zoneDto.id,
                    name: zoneDto.name,
                    env: {
                        id: zoneDto.envId,
                        name: zoneDto.envName
                    },
                    envId: zoneDto.envId,
                    envName: zoneDto.envName,
                    k8s: zoneDto.k8s,
                    k8sVersion: zoneDto.k8sVersion,
                    extensions: {
                        dashboard: zoneDto.extensions && zoneDto.extensions.dashboard ? zoneDto.extensions.dashboard : '',
                        network: zoneDto.extensions && zoneDto.extensions.network ? zoneDto.extensions.network : '',
                        rpc: zoneDto.extensions && zoneDto.extensions.rpc ? zoneDto.extensions.rpc : ''
                    },
                    description: zoneDto.description
                };
            },
            handleDelete(zoneId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            zoneId: zoneId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteZoneById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('fetchAllZones', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('fetchAllZones', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                });
            },
            addZone() {
                this.inEditZone = {
                    id: null,
                    name: '',
                    env: null,
                    k8s: '',
                    extensions: {
                        dashboard: '',
                        network: '',
                        rpc: ''
                    },
                    description: ''
                };
                this.dialogVisible = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;

                        if (!self.inEditZone.extensions.network) {
                            this.$message.warning('请输入所属网络');
                        } else if (!self.inEditZone.extensions.dashboard) {
                            this.$message.warning('请输入dashboard');
                        } else {
                            let data = {
                                envName: this.currentEnv,
                                zoneName: this.currentValue,
                                newZone: self.inEditZone,
                                pageSize: this.pageSize,
                                page: this.currentPage,
                            };

                            data.newZone.envId = self.inEditZone.env.id;
                            data.newZone.envName = self.inEditZone.env.name;
                            delete data.newZone.env;

                            if (data.newZone.id == null) {
                                this.$store.dispatch('createNewZone', data);
                            } else {
                                this.$store.dispatch('updateZone', data);
                            }

                            this.dialogVisible = false;
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
                this.$store.dispatch('queryZones', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    envName: this.currentEnv,
                    zoneName: this.currentValue
                });
            },
            resetForm() {
                this.inEditZone = {
                    id: null,
                    name: '',
                    k8s: '',
                    extensions: {
                        dashboard: '',
                        network: '',
                        rpc: ''
                    },
                    description: ''
                }
            },
            resetQueryForm() {
                this.currentEnv = '';
                this.currentValue = '';
                this.onSearch();
            }
        },

        data: function () {
            return {
                dialogVisible: false,
                currentField: 'id',
                currentValue: '',
                currentEnv: '',
                fieldList: [{
                    value: 'id',
                    label: 'ID'
                }, {
                    value: 'name',
                    label: 'zone名称'
                }],
                currentPage: 1,
                pageSize: 10,
                inEditZone: {
                    id: null,
                    name: '',
                    env: null,
                    k8s: '',
                    extensions: {
                        dashboard: '',
                        network: '',
                        rpc: ''
                    },
                    description: ''
                },
                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ],
                    env: [
                        {required: true, message: '环境不能为空', trigger: 'change'}
                    ],
                    k8s: [
                        {required: true, message: 'k8s不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                envs: 'getAllEnvs',
                zones: 'getAllZones',
                total: 'getTotalZones',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllEnvs', {
                pageSize: 9999,
                page: 1,
            });
            this.$store.dispatch('fetchAllZones', {
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

