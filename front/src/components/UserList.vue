<template>
    <div class="user-table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>用户列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">
                <el-col :span="4">
                    <!--<el-input v-model="queryItem.realName" placeholder="姓名"></el-input>-->
                </el-col>
                <el-col :span="4">

                    <el-select v-model="queryItem.userName"
                               placeholder="昵称"
                               filterable
                               remote
                               :remote-method="handleUserAutoPrompt" @change="onSearch">
                        <el-option v-for="item in usersByName"
                                   :key="item.workNumber"
                                   :label="item.userName"
                                   :value="item.userName">
                        </el-option>
                    </el-select>

                </el-col>
                <el-col :span="4">
                    <el-select v-model="queryItem.orgCode" placeholder="组织" @change="onSearch">
                        <el-option v-for="item in orgs" :key="item.orgCode" :label="item.name"
                                   :value="item.orgCode"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>

            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="users" highlight-current-row style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="工号" prop="workNumber" align="center"></el-table-column>
                <el-table-column label="姓名" prop="realName" align="center"></el-table-column>
                <el-table-column label="昵称" prop="userName" align="center"></el-table-column>
                <el-table-column label="组织" prop="orgDto.name" align="center"></el-table-column>
                <el-table-column label="邮件" prop="email" align="center" width="240px"></el-table-column>
                <el-table-column label="源头" prop="source" align="center"></el-table-column>
                <el-table-column label="当前角色" align="center">
                    <template slot-scope="props">
                        <span v-for=" item in props.row.roles">
                            {{item.name + " "}}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="240px" v-if="isAdmin">
                    <template slot-scope="props">
                        <el-button @click="editOrg(props.row)" size="small">编辑组织</el-button>
                        <el-button @click="editRole(props.row)" size="small">编辑角色</el-button>
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

        <el-dialog title="用户信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditUser" ref="inEditUserForm" :rules="rules">

                <el-form-item label="工号" prop="workNumber">
                    <el-input v-model="inEditUser.workNumber"></el-input>
                </el-form-item>

                <el-form-item label="姓名" prop="realName">
                    <el-input v-model="inEditUser.realName"></el-input>
                </el-form-item>

                <el-form-item label="昵称" prop="userName">
                    <el-input v-model="inEditUser.userName"></el-input>
                </el-form-item>

                <!--                <el-form-item label="组织" prop="orgId">
                                    <el-select v-model="inEditUser.orgId" placeholder="请选择">
                                        <el-option v-for="item in orgs" :key="item.id" :label="item.name"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                </el-form-item>-->

                <el-form-item label="email" prop="email">
                    <el-input v-model="inEditUser.email"></el-input>
                </el-form-item>

                <el-form-item label="数据源" prop="source">
                    <el-input v-model="inEditUser.source"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditUserForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditUser.id">重置</el-button>
                    <el-button @click="onClose" class="close-button">关闭</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="编辑角色" :visible.sync="roleDialogVisible" class="user-dialog">

            <el-row class="rolesClass">
                <el-col>
                    <div align="center">
                        <el-checkbox-group v-model="checkedRoles" @change="handleCheckedRolesChange">
                            <el-checkbox v-for="role in roles" :label="role.id" :key="role.name"
                                         style="float:left; text-align: center">{{role.name}}
                            </el-checkbox>
                        </el-checkbox-group>
                    </div>
                </el-col>
            </el-row>
            <el-row class="rolesClass">
                <el-col :span="4">
                    <el-button type="primary" @click="onSubmitRole('inEditRoleCheckForm')">保存</el-button>
                </el-col>
            </el-row>
        </el-dialog>

        <el-dialog title="编辑组织" :visible.sync="orgDialogVisible" class="user-dialog">

            <el-row class="rolesClass">
                <el-form label-width="80px" label-position="center" :model="inEditUser" :rules="rules">
                    <el-col>
                        <div align="center">
                            <el-form-item label="所属组织" prop="orgId" class="app-drop-down">
                                <el-select v-model="inEditUser.orgId" placeholder="选择所属组织">
                                    <el-option v-for="item in orgs" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-form>
            </el-row>
            <el-row class="rolesClass">
                <el-col :span="4">
                    <el-button type="primary" @click="onSubmitOrg('inEditOrgCheckForm')">保存</el-button>
                </el-col>
            </el-row>
        </el-dialog>

    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';

    export default {

        methods: {

            editOrg(userDto) {
                this.orgDialogVisible = true;
                let tempOrgId = null;
                if (userDto.orgDto != null) {
                    tempOrgId = userDto.orgDto.id;
                }
                this.inEditUser = {
                    id: userDto.id,
                    workNumber: userDto.workNumber,
                    realName: userDto.realName,
                    userName: userDto.userName,
                    //orgId: userDto.orgDto.id,
                    orgId: tempOrgId,
                    email: userDto.email,
                    source: userDto.source
                };
            },
            handleDelete(userId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            userId: userId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteUserById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('queryUsers', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryUser: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('queryUsers', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryUser: this.queryItem
                });
            },

            editRole(user) {
                this.roleDialogVisible = true;
                this.currentUserWorkNumber = user.workNumber;
                //get roles for a user
                if (user.roles == null) {
                    this.checkedRoles = [];
                } else {
                    let roleIds = user.roles.map(function (item) {
                        return item.id;
                    });
                    this.checkedRoles = roleIds;
                }
            },

            handleCheckedRolesChange(value) {
                this.checkedRoles = value;
            },

            onSubmitOrg(formName) {
                this.orgDialogVisible = false;
                let newUserExt = {
                    userWorkNumber: this.inEditUser.workNumber,
                    orgId: this.inEditUser.orgId
                };

                let data = {
                    queryUser: this.queryItem,
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    newUserExt: newUserExt
                };

                this.$store.dispatch('addUserOrg', data);
            },

            onSubmitRole(formName) {
                this.roleDialogVisible = false;

                let data = {
                    queryUser: this.queryItem,
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    roleIds: this.checkedRoles,
                    userWorkNumber: this.currentUserWorkNumber
                };
                this.$store.dispatch('addUserRole', data);
            },

            addUser() {
                this.inEditUser = {
                    id: null,
                    workNumber: '',
                    realName: '',
                    userName: '',
                    orgId: null,
                    email: '',
                    source: '',
                };
                this.dialogVisible = true;
            },

            //edit user not used in atlas
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let self = this;
                        this.dialogVisible = false;
                        let data = {
                            newUser: self.inEditUser,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newUser.id == null) {
                            this.$store.dispatch('createNewUser', data);
                        } else {
                            this.$store.dispatch('updateUser', data);
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
                this.$store.dispatch('queryUsers', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    queryUser: this.queryItem
                });
            },
            resetForm() {
                this.inEditUser = {
                    id: null,
                    workNumber: null,
                    realName: '',
                    userName: '',
                    orgId: null,
                    email: '',
                    source: '',
                };
            },

            //根据输入值异步自动弹出userName
            handleUserAutoPrompt(data) {
                if (data !== '') {
                    this.$store.dispatch('queryUsersByUserName', {userName: data});
                }
            },

            resetQueryForm() {
                this.queryItem = {
                    orgCode: '',
                    realName: '',
                    userName: ''
                };
                this.onSearch();
            },
        },

        data: function () {
            return {
                dialogVisible: false,
                currentField: 'id',
                currentValue: '',
                queryItem: {
                    orgCode: '',
                    realName: '',
                    userName: ''
                },
                currentPage: 1,
                pageSize: 10,
                inEditUser: {
                    id: null,
                    workNumber: null,
                    realName: '',
                    userName: '',
                    orgId: null,
                    email: '',
                    source: '',
                },
                //role dialog setting
                roleDialogVisible: false,
                orgDialogVisible: false,
                checkAll: false,
                checkedRoles: [],
                currentUserWorkNumber: null,

                rules: {
                    realName: [
                        {required: true, message: '真实姓名不能为空', trigger: 'blur'}
                    ],
                    userName: [
                        {required: true, message: '昵称不能为空', trigger: 'blur'}
                    ],

                    /*                    orgId: [
                                            {type: 'number', required: true, message: 'orgId不能为空', trigger: 'blur'}
                                        ],*/

                    source: [
                        {required: true, message: 'source不能为空', trigger: 'blur'}
                    ],
                }
            }
        },

        computed: {
            ...mapGetters({
                users: 'getAllUsers',
                orgs: 'getAllOrgs',
                roles: 'getAllRoles',
                total: 'getTotalUsers',
                loginUsername: 'getUserName',
                isAdmin: 'getAdminResult',
                usersByName: 'getUsersByName',
            })
        },

        created() {
            this.$store.dispatch('fetchAllUsers', {
                pageSize: this.pageSize,
                page: this.currentPage,
            });
            this.$store.dispatch('fetchAllOrgs', {
                //todo, 修改获取所有org，pageSize设为
                pageSize: 100,
                page: 1,
            });
            //this.$store.dispatch('fetchRoles');
            this.$store.dispatch('fetchAllRoles', {
                pageSize: 100,
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

    .user-dialog .el-dialog--small {
        width: 40%;

    }

    .user-dialog .el-dialog__body {
        padding: 5% 8%
    }

    .rolesClass {
        padding: 5% 8%;
    }

    .user-table-scroll-div {
        max-width: 1600px;
        min-width: 1400px;
    }
</style>

