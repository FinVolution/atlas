import {api} from '../../api'


// initial state
const state = {
    apps: [],
    appsByName: [],
    fuzzyAppsByName: [],
    totalApps: null
};

// getters
const getters = {
    getAllApps: state => state.apps,
    getFuzzyAppsByName: state => state.fuzzyAppsByName,
    getAppsByName: state => state.appsByName,
    getTotalApps: state => state.totalApps
};

// actions
const actions = {

    fetchAllApps({commit}, data) {
        api.appService.getAllApps(data).then(function (res) {
            commit("updateAppsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteAppById({dispatch}, data) {
        api.appService.deleteApp(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllApps", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateApp({dispatch}, data) {
        api.appService.updateApp(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryApps", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    createNewApp({dispatch}, data) {
        api.appService.createApp(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllApps", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    //模糊查询appName
    fuzzyQueryAppsByAppName({commit}, data) {
        api.appService.fuzzySearchAppByAppName(data).then(function (res) {
            commit("updateAppsByName", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    queryApps({commit}, data) {
        api.appService.searchApps(data).then(function (res) {
            commit("updateAppsList", res.data.detail);

        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));

    },

    queryAppsByAppName({commit}, data) {
        api.appService.queryAppsByAppName(data).then(function (res) {
            commit("updateAppsByName", res.data.detail);
        }).bind(this).catch(function (err) {
            console.log(err);
        }.bind(this));
    }
}

// mutations
const mutations = {
    updateAppsList(state, data) {
        state.apps = data.content;
        state.totalApps = data.totalElements;
    },

    updateAppsByName(state, data) {
        state.appsByName = data;
        //state.totalApps = data.totalElements;
    },

};

export default {
    state,
    getters,
    actions,
    mutations
}