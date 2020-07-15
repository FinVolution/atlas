
import Vue from 'vue'
import Vuex from 'vuex'
import cloudList from './modules/cloudStore'
import zoneList from './modules/zoneStore'
import appList from './modules/appStore'
import envList from './modules/envStore'
import orgList from './modules/orgStore'
import roleList from './modules/roleStore'
import quotaList from './modules/quotaStore'
import appQuotaList from './modules/appQuotaStore'

import userList from './modules/userStore'
import specTypeList from './modules/specTypeStore'
import pauth from './modules/pauth'
import apply from './modules/apply'

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
    modules: {
        cloudList,
        zoneList,
        appList,
        envList,
        orgList,
        userList,
        roleList,
        quotaList,
        appQuotaList,
        specTypeList,
        apply,
        pauth
    },
    strict: debug
})

