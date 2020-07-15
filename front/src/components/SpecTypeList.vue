<template>
    <div class="table-scroll-div">
        <div>
            <el-row>
                <el-col>
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>规格列表</el-breadcrumb-item>
                    </el-breadcrumb>
                </el-col>
            </el-row>
            <el-row class="query-form">

                <el-col :span="3">
                    <el-input v-model="queryItem.name" placeholder="输入名字"></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="queryItem.cpu" placeholder="cpu"></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="queryItem.memory" placeholder="内存大小"></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="queryItem.disk" placeholder="磁盘大小"></el-input>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button @click="resetQueryForm">重置</el-button>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                </el-col>
                <el-button type="primary" fixed="right" @click="addSpecType" class="add_button" v-if="isAdmin">添加规格</el-button>
            </el-row>
        </div>
        <br/>

        <div>
            <el-table :data="specTypes" style="width: 100%" empty-text="暂无数据" border fit>
                <el-table-column label="ID" prop="id" align="center" sortable></el-table-column>
                <el-table-column label="规格名称" prop="name" align="center"></el-table-column>
                <el-table-column label="CPU(核)" prop="cpu" align="center"></el-table-column>
                <el-table-column label="内存大小(MB)" prop="memory" align="center"></el-table-column>
                <el-table-column label="磁盘空间(MB)" prop="disk" align="center"></el-table-column>
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

        <el-dialog title="规格信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="right" :model="inEditSpecType" ref="inEditSpecTypeForm"
                     :rules="rules">
                <el-form-item label="名字" prop="name">
                    <el-input v-model="inEditSpecType.name"></el-input>
                </el-form-item>
                <el-form-item label="cpu(核)" prop="cpu">
                    <el-input v-model="inEditSpecType.cpu"></el-input>
                </el-form-item>
                <el-form-item label="内存(MB)" prop="memory">
                    <el-input v-model="inEditSpecType.memory"></el-input>
                </el-form-item>
                <el-form-item label="磁盘(MB)" prop="disk">
                    <el-input v-model="inEditSpecType.disk"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="inEditSpecType.description"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit('inEditSpecTypeForm')">保存</el-button>
                    <el-button @click="resetForm" v-if="!inEditSpecType.id">重置</el-button>
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
            handleEdit(specTypeDto) {
                this.dialogVisible = true;
                this.inEditSpecType = {
                    id: specTypeDto.id,
                    name: specTypeDto.name,
                    cpu: specTypeDto.cpu.toString(),
                    memory: specTypeDto.memory.toString(),
                    disk: specTypeDto.disk.toString(),
                    description: specTypeDto.description
                };
            },
            handleDelete(specTypeId) {
                this.$confirm('确认删除？')
                    .then(() => {
                        let data = {
                            specTypeId: specTypeId,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        this.$store.dispatch('deleteSpecTypeById', data);
                    })
                    .catch(() => {
                    });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.$store.dispatch('querySpecTypes', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    specTypeQuery: this.queryItem
                });
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.$store.dispatch('querySpecTypes', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    specTypeQuery: this.queryItem
                });
            },
            addSpecType() {
                this.inEditSpecType = {
                    id: null,
                    name: '',
                    cpu: null,
                    memory: null,
                    disk: '',
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
                            newSpecType: self.inEditSpecType,
                            pageSize: this.pageSize,
                            page: this.currentPage,
                        };
                        if (data.newSpecType.id == null) {
                            this.$store.dispatch('createNewSpecType', data);
                        } else {
                            this.$store.dispatch('updateSpecType', data);
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
                this.$store.dispatch('querySpecTypes', {
                    pageSize: this.pageSize,
                    page: this.currentPage,
                    specTypeQuery: this.queryItem
                });
            },
            resetForm() {
                this.inEditSpecType = {
                    id: null,
                    name: '',
                    cpu: null,
                    memory: null,
                    disk: null,
                    description: ''
                }
            },
            resetQueryForm() {
                this.queryItem = {
                    name: '',
                    cpu: null,
                    memory: null,
                    disk: null
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
                    cpu: null,
                    memory: null,
                    disk: null
                },
                currentPage: 1,
                pageSize: 10,
                inEditSpecType: {
                    id: null,
                    name: '',
                    cpu: null,
                    memory: null,
                    disk: null,
                    description: ''
                },
                rules: {
                    name: [
                        {required: true, message: '名字不能为空', trigger: 'blur'}
                    ],
                    cpu: [
                        {required: true, message: 'cpu不能为空', trigger: 'blur'}
                    ],
                    memory: [
                        {required: true, message: 'memory不能为空', trigger: 'blur'}
                    ],
                    disk: [
                        {required: true, message: 'disk不能为空', trigger: 'blur'}
                    ],
                }
            }
        },

        computed: {
            ...mapGetters({
                specTypes: 'getAllSpecTypes',
                total: 'getTotalSpecTypes',
                isAdmin: 'getAdminResult'
            })
        },
        created() {
            this.$store.dispatch('fetchAllSpecTypes', {
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

