import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    orgs: [],
    totalOrgs: null
};

// getters
const getters = {
    getAllOrgs: state => state.orgs,
    getTotalOrgs: state => state.totalOrgs
};

// actions
const actions = {

    fetchAllOrgs({commit}, data) {
        api.orgService.getAllOrgs(data).then(function (res) {
            commit("updateOrgsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteOrgById({dispatch}, data) {
        api.orgService.deleteOrg(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllOrgs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateOrg({dispatch}, data) {
        api.orgService.updateOrg(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryOrgs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));

    },

    createNewOrg({dispatch}, data) {
        api.orgService.createOrg(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllOrgs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    queryOrgs({commit}, data) {
        api.orgService.searchOrgs(data).then(function (res) {
            commit("updateOrgsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateOrgsList(state, data) {
        state.orgs = data.content;
        state.totalOrgs = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}