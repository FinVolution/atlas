<template>
    <div class="content-panel">
        <el-row>
            <el-col>
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>申请列表</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
        </el-row>

        <br/>

        <el-table :data="applies" style="width: 100%" border fit>
            <el-table-column type="expand">
                <template slot-scope="props">
                    <table class="job-expand-table" cellpadding="10px" cellspacing="0" border="0">
                        <tr>
                            <td width="20%" align="center">
                                <div>申请参数</div>
                            </td>
                            <td width="80%" >
                                <vue-json-pretty :path="'res'" :data="JSON.parse(props.row.request == null ? '{}' : props.row.request)"></vue-json-pretty>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%" align="center">
                                <div>申请结果</div>
                            </td>
                            <td width="80%" >
                                <vue-json-pretty :path="'res'" :data="JSON.parse(props.row.result == null ? '{}' : props.row.result)"></vue-json-pretty>
                            </td>
                        </tr>
                    </table>
                </template>
            </el-table-column>
            <el-table-column label="申请类型" prop="type" align="center"></el-table-column>
            <el-table-column label="申请状态" prop="status" align="center">
                <template slot-scope="props">
                    <el-tag type="success" v-if="props.row.status=='NEW'">{{props.row.status}}</el-tag>
                    <el-tag type="danger" v-else-if="props.row.status=='REJECTED'">{{props.row.status}}</el-tag>
                    <el-tag type="warning" v-else-if="props.row.status=='ACCEPTED'">{{props.row.status}}</el-tag>
                    <el-tag type="info" v-else>{{props.row.status}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="申请人" prop="applyUser" align="center"></el-table-column>
            <el-table-column label="申请部门" prop="applyDepartment" align="center"></el-table-column>
            <el-table-column label="操作人" prop="opUser" align="center"></el-table-column>
            <el-table-column label="申请时间" prop="applyTime" align="center" :formatter="dateFormatter"></el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="props">
                    <el-button @click="handleApply(props.row)" size="small" type="primary" plain>处理</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div align='center' style="margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 30, 50, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="处理申请" :visible.sync="dialogVisible" width="600px" :before-close="onClose">
            <el-form label-width="80px" label-position="left">
                <el-form-item label="更新状态" prop="handleData.status">
                    <el-select v-model="apply.status" placeholder="请选择状态" filterable style="width: 100%">
                        <el-option v-for="item in statusList" :key="item" :value="item"
                                   :label="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="处理结果" prop="handleData.result">
                    <el-input v-model="apply.result" type="textarea" :autosize="{minRows: 4}" placeholder='请输入内容，e.g.{"key":"value"}'></el-input>
                </el-form-item>
                <el-form-item style="margin-bottom: 0">
                    <el-button @click="onClose" style="float: right">关闭</el-button>
                    <el-button type="primary" @click="onSubmit" style="float:right;margin:0 10px 0 0;">更新</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import {mapGetters, mapActions} from 'vuex'
    import VueJsonPretty from 'vue-json-pretty'

    export default {
        components: {
            VueJsonPretty
        },
        data: function () {
            return {
                currentPage: 1,
                pageSize: 10,
                applyUser: "",
                status: "",
                statusList: ["ACCEPTED", "REJECTED", "DONE", "NEW"],
                dialogVisible: false,
                handleData: {},
                apply: {}
            }
        },

        computed: {
            ...mapGetters({
                applies: 'getApplyList',
                total: 'getApplyCount'
            })
        },

        methods: {
            dateFormatter(row, column, cellValue) {
                return new Date(cellValue).toLocaleString();
            },
            handleApply(applyRow) {
                this.apply = {id: applyRow.id, status: applyRow.status, result: applyRow.result};
                this.dialogVisible = true;
            },
            onSubmit() {
                this.$store.dispatch('updateApplyStatus', {
                    updateRequest: {
                        applyId: this.apply.id,
                        status: this.apply.status,
                        result: this.apply.result,
                    },
                    queryRequest: {
                        applyUser: this.applyUser,
                        status: this.status,
                        page: this.currentPage - 1,
                        size: this.pageSize,
                    }
                });
                this.onClose();
            },
            onClose() {
                this.apply = {};
                this.dialogVisible = false;
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshApplyList();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshApplyList();
            },
            refreshApplyList() {
                this.$store.dispatch('fetchAppliesByPage', {
                    applyUser: this.applyUser,
                    status: this.status,
                    page: this.currentPage - 1,
                    size: this.pageSize,
                });
            },
        },

        created () {
            this.refreshApplyList();
        },
    }
</script>

<style>
    .job-expand-table {
        border-left: 1px solid #ebeef5;
        border-top: 1px solid #ebeef5;
        width: 100%;
        word-break: break-all;
    }

    .job-expand-table td div {
        padding: 0 10px;
        /*white-space: pre-wrap;*/
    }
</style>