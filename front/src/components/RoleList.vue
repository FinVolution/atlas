<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>角色列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <el-col :span="4">
                    <el-input v-model="queryItem.name" placeholder="请输入角色"></el-input>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>
                <el-button type="primary" fixed="right" @click="addRole" class="add_button" v-if="isAdmin">添加角色</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="roles" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="角色名称" prop="name" align="center"></el-table-column>
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

        <el-dialog title="角色信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditRole" ref="inEditRoleForm" :rules="rules">
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditRole.name"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="inEditRole.description"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditRoleForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditRole.id">重置</el-button>
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
            handleEdit(roleDto) {
                this.dialogVisible = true;
                this.inEditRole = {
                    id: roleDto.id,
                    name: roleDto.name,
                    description: roleDto.description
                };
            },
            handleDelete(roleId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            roleId: roleId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteRoleById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('queryRoles', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryRole: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('queryRoles', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryRole: this.queryItem
                });
            },
            addRole() {
                this.inEditRole = {
                    id: null,
                    name: '',
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
                            newRole: self.inEditRole,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newRole.id == null) {
                            this.$store.dispatch('createNewRole', data);
                        } else {
                            this.$store.dispatch('updateRole', data);
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
                this.$store.dispatch('queryRoles', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryRole: this.queryItem
                });
            },
            resetForm() {
                this.inEditRole = {
                    id: null,
                    name: '',
                    description: ''
                }
            },
            resetQueryForm() {
                this.queryItem = {
                    name: '',
                }
            },
        },

        data: function () {
            return {
                dialogVisible: false,
                currentField: 'id',
                currentValue: '',
                queryItem: {
                    name: ''
                },
                currentPage: 1,
                pageSize: 10,
                inEditRole: {
                    id: null,
                    name: '',
                    description: ''
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
                roles: 'getAllRoles',
                total: 'getTotalRoles',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllRoles', {
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

