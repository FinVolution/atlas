
import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/Login.vue'
import Home from '../components/layout/Home.vue'
import Introduction from '../components/Introduction.vue'
import ZoneList from '../components/ZoneList.vue'
import AppList from '../components/AppList.vue'
import EnvList from '../components/EnvList.vue'
import OrgList from '../components/OrgList.vue'
import RoleList from '../components/RoleList.vue'
import UserList from '../components/UserList.vue'
import QuotaList from '../components/QuotaList.vue'
import AppQuotaList from '../components/AppQuotaList.vue'

import SpecTypeList from '../components/SpecTypeList.vue'
import ApplyList from '../components/ApplyList.vue'

Vue.use(Router);

export default new Router({
    mode: "hash",
    routes: [
        {
            path: '/',
            component: Home,
            children: [
                {
                    path: '',
                    name: 'Introduction',
                    component: Introduction
                },
                {
                    path: '/apps',
                    name: 'AppList',
                    component: AppList
                },
                {
                    path: '/zones',
                    name: 'ZoneList',
                    component: ZoneList
                },
                {
                    path: '/orgs',
                    name: 'OrgList',
                    component: OrgList
                },
                {
                    path: '/envs',
                    name: 'EnvList',
                    component: EnvList
                },
                {
                    path: '/roles',
                    name: 'RoleList',
                    component: RoleList
                },
                {
                    path: '/specTypes',
                    name: 'SpecTypeList',
                    component: SpecTypeList
                },
                {
                    path: '/quotas',
                    name: 'QuotaList',
                    component: QuotaList
                },
                {
                    path: '/appquotas',
                    name: 'AppQuotaList',
                    component: AppQuotaList
                },
                {
                    path: '/users',
                    name: 'UserList',
                    component: UserList
                },
                {
                    path: '/applies',
                    name: 'ApplyList',
                    component: ApplyList
                }
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
        }
    ],
    linkActiveClass: 'active'
})
