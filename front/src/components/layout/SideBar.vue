<template>
    <div class="sidebar">
        <el-menu router>
<!--            <el-menu-item index="/clouds">-->
<!--                <i class="el-icon-setting"></i>云列表-->
<!--            </el-menu-item>-->
            <el-menu-item index="/applies">
                <i class="el-icon-setting"></i>应用申请
            </el-menu-item>
            <el-menu-item index="/envs">
                <i class="el-icon-setting"></i>环境管理
            </el-menu-item>
            <el-menu-item index="/zones">
                <i class="el-icon-setting"></i>zone管理
            </el-menu-item>
            <el-menu-item index="/apps">
                <i class="el-icon-setting"></i>应用管理
            </el-menu-item>
            <el-menu-item index="/orgs">
                <i class="el-icon-setting"></i>组织管理
            </el-menu-item>
            <el-menu-item index="/roles">
                <i class="el-icon-setting"></i>角色管理
            </el-menu-item>
            <el-menu-item index="/users">
                <i class="el-icon-setting"></i>用户管理
            </el-menu-item>
            <el-menu-item index="/specTypes">
                <i class="el-icon-setting"></i>规格管理
            </el-menu-item>
            <el-menu-item index="/appquotas">
                <i class="el-icon-setting"></i>应用配额管理
            </el-menu-item>
        </el-menu>
    </div>
</template>
<script>
    export default {}
</script>
<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom: 0;
        background: #2E363F;
    }

    .sidebar > ul {
        height: 100%;
    }

    .sidebar i {
        margin-top: -3px;
    }
</style>
