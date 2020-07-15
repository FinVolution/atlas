<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>组织列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <el-col :span="4">
                    <el-input v-model="queryItem.name" placeholder="输入组织名"></el-input>
                </el-col>

                <el-col :span="4">
                    <el-select v-model="queryItem.userWorkNumber"
                               placeholder="输入负责人"
                               filterable
                               remote
                               :remote-method="handleUserAutoPrompt">
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
                <el-button type="primary" fixed="right" @click="addOrg" class="add_button" v-if="isAdmin">添加组织</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="orgs" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="组织名称" prop="name" align="center"></el-table-column>
                <el-table-column label="组织代码" prop="orgCode" align="center"></el-table-column>
                <el-table-column label="负责人" prop="userDto.realName" align="center"></el-table-column>
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

        <el-dialog title="组织信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditOrg" ref="inEditOrgForm" :rules="rules">
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditOrg.name"></el-input>
                </el-form-item>

                <el-form-item label="组织代号" prop="orgCode">
                    <el-input v-model="inEditOrg.orgCode"></el-input>
                </el-form-item>

                <el-form-item label="负责人" prop="userWorkNumber">

                    <el-select v-model="inEditOrg.userWorkNumber"
                               placeholder="输入负责人"
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
                    <el-input v-model="inEditOrg.description" type="textarea"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditOrgForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditOrg.id">重置</el-button>
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
            handleEdit(orgDto) {
                this.dialogVisible = true;
                this.inEditOrg = {
                    id: orgDto.id,
                    name: orgDto.name,
                    orgCode: orgDto.orgCode,
                    parentOrgId: orgDto.parentOrgId,
                    userWorkNumber: orgDto.userDto.workNumber,
                    description: orgDto.description
                };

                //初始化 usersByName
                this.$store.dispatch('initUsersByName', [orgDto.userDto]);

            },
            handleDelete(orgId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            orgId: orgId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteOrgById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('queryOrgs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryOrg: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('queryOrgs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryOrg: this.queryItem
                });
            },

            //根据输入值异步自动弹出userName
            handleUserAutoPrompt(data) {
                if (data !== '') {
                    //this.appLoading = true;
                    this.$store.dispatch('queryUsersByUserName', {userName: data});
                }
            },

            addOrg() {
                this.inEditOrg = {
                    id: null,
                    name: '',
                    orgCode: '',
                    parentOrgId: null,
                    userWorkNumber: '',
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
                            queryOrg: this.queryItem,
                            newOrg: self.inEditOrg,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newOrg.id == null) {
                            this.$store.dispatch('createNewOrg', data);
                        } else {
                            this.$store.dispatch('updateOrg', data);
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
                this.$store.dispatch('queryOrgs', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryOrg: this.queryItem
                });
            },
            resetForm() {
                this.inEditOrg = {
                    id: null,
                    name: '',
                    orgCode: '',
                    parentOrgId: null,
                    userWorkNumber: '',
                    description: ''
                };
            },
            resetQueryForm() {
                this.queryItem = {
                    name: '',
                    userWorkNumber: '',
                }
            }
        },

        data: function () {
            return {
                dialogVisible: false,
                currentField: 'id',
                currentValue: '',
                queryItem: {
                    name: '',
                    userWorkNumber: ''
                },
                currentPage: 1,
                pageSize: 10,
                inEditOrg: {
                    id: null,
                    name: '',
                    orgCode: '',
                    parentOrgId: null,
                    userWorkNumber: '',
                    description: ''
                },
                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ],
                    orgCode: [
                        {required: true, message: '组织代号不能为空', trigger: 'blur'}
                    ],
                    parentOrgId: [
                        {type: 'number', required: true, message: 'parentOrgId不能为空', trigger: 'blur'}
                    ],
                }
            }
        },

        computed: {
            ...mapGetters({
                orgs: 'getAllOrgs',
                usersByName: 'getUsersByName',
                total: 'getTotalOrgs',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
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