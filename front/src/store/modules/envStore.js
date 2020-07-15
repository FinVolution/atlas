import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    envs: [],
    totalEnvs: null
};

// getters
const getters = {
    getAllEnvs: state => state.envs,
    getTotalEnvs: state => state.totalEnvs
};

// actions
const actions = {

    fetchAllEnvs({commit}, data) {
        api.envService.getAllEnvs(data).then(function (res) {
            commit("updateEnvsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteEnvById({dispatch}, data) {
        api.envService.deleteEnv(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllEnvs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateEnv({dispatch}, data) {
        api.envService.updateEnv(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryEnvs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewEnv({dispatch}, data) {
        api.envService.createEnv(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllEnvs", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    queryEnvs({commit}, data) {
        api.envService.searchEnvs(data).then(function (res) {
            commit("updateEnvsList", res.data.detail);

        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateEnvsList(state, data) {
        state.envs = data.content;
        state.totalEnvs = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}