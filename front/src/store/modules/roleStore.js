import {api} from '../../api'


// initial state
const state = {
    roles: [],
    totalRoles: null
};

// getters
const getters = {
    getAllRoles: state => state.roles,
    getRoleIds: state => state.roleIds,
    getTotalRoles: state => state.totalRoles
};

// actions
const actions = {

    //get all roles
    fetchRoles({commit}) {
        api.roleService.getAllRoles().then(function (res) {
            commit("updateRolesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    //get roles by page
    fetchAllRoles({commit}, data) {
        api.roleService.getAllRolesByPage(data).then(function (res) {
            commit("updateRolesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteRoleById({dispatch}, data) {
        api.roleService.deleteRole(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllRoles", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateRole({dispatch}, data) {
        api.roleService.updateRole(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllRoles", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewRole({dispatch}, data) {
        api.roleService.createRole(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllRoles", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    queryRoles({commit}, data) {
        api.roleService.searchRoles(data).then(function (res) {
            debugger;
            commit("updateRolesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateRolesList(state, data) {
        state.roles = data.content;
        state.totalRoles = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}