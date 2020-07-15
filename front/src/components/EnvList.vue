<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>环境列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <!--
                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>
                -->
                <el-button type="primary" fixed="right" @click="addEnv" class="add_button"  v-if="isAdmin">添加环境</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="envs" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable width="80px"></el-table-column>
                <el-table-column label="环境" prop="name" align="center"></el-table-column>
                <el-table-column label="consul地址" prop="consul" align="center"></el-table-column>
                <el-table-column label="nginx地址" prop="nginx" align="center"></el-table-column>
                <el-table-column label="dns地址" prop="dns" align="center"></el-table-column>
                <el-table-column label="镜像仓库地址" prop="dockeryard" align="center"></el-table-column>
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

        <el-dialog title="环境信息" :visible.sync="dialogVisible">
            <el-form label-width="100px" label-position="right" :model="inEditEnv" ref="inEditEnvForm" :rules="rules">
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditEnv.name"></el-input>
                </el-form-item>
                <el-form-item label="consul地址" prop="consul">
                    <el-input v-model="inEditEnv.consul"></el-input>
                </el-form-item>
                <el-form-item label="nginx地址" prop="nginx">
                    <el-input v-model="inEditEnv.nginx"></el-input>
                </el-form-item>
                <el-form-item label="dns地址" prop="dns">
                    <el-input v-model="inEditEnv.dns"></el-input>
                </el-form-item>
                <el-form-item label="镜像仓库地址" prop="dockeryard">
                    <el-input v-model="inEditEnv.dockeryard"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="inEditEnv.description" type="textarea"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditEnvForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditEnv.id">重置</el-button>
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
            handleEdit(envDto) {
                this.inEditEnv = {
                    id: envDto.id,
                    name: envDto.name,
                    consul: envDto.consul,
                    nginx: envDto.nginx,
                    dns: envDto.dns,
                    dockeryard: envDto.dockeryard,
                    extensions: {
                    },
                    description: envDto.description
                };
                this.dialogVisible = true;
            },
            handleDelete(envId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            envId: envId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteEnvById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('queryEnvs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryEnv: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('queryEnvs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryEnv: this.queryItem
                });
            },
            addEnv() {
                this.inEditEnv = {
                    id: null,
                    name: '',
                    consul: '',
                    dns: '',
                    nginx: '',
                    dockeryard: '',
                    extensions: {
                    },
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
                            queryEnv: this.queryItem,
                            newEnv: self.inEditEnv,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newEnv.id == null) {
                            this.$store.dispatch('createNewEnv', data);
                        } else {
                            this.$store.dispatch('updateEnv', data);
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
                this.$store.dispatch('queryEnvs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryEnv: this.queryItem
                });
            },
            resetForm() {
                this.inEditEnv = {
                    id: null,
                    name: '',
                    consul: '',
                    dns: '',
                    nginx: '',
                    dockeryard: '',
                    extensions: {
                    },
                    description: ''
                }
            },

            resetQueryForm() {
                this.queryItem= {
                }
            },

        },

        data: function () {
            return {
                dialogVisible: false,
                //currentField: 'id',

                //currentValue: '',
                fieldList: [{
                    value: 'id',
                    label: 'ID'
                }, {
                    value: 'name',
                    label: '应用名称'
                }],
                currentPage: 1,
                pageSize: 10,
                inEditEnv: {
                    id: null,
                    name: '',
                    consul: '',
                    dns: '',
                    nginx: '',
                    dockeryard: '',
                    extensions: {
                    },
                    description: ''
                },
                queryItem:{
                },
                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ]
                }
            }
        },

        computed: {
            ...mapGetters({
                envs: 'getAllEnvs',
                clouds: 'getAllClouds',
                total: 'getTotalEnvs',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllEnvs', {
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

