import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    appQuotas: [],
    totalAppQuotas: null
};

// getters
const getters = {
    getAllAppQuotas: state => state.appQuotas,
    getTotalAppQuotas: state => state.totalAppQuotas
};

// actions
const actions = {

    fetchAllAppQuotas({commit}, data) {
        api.appQuotaService.getAllAppQuotas(data).then(function (res) {
            commit("updateAppQuotasList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    queryAppQuotas({commit}, data) {
        api.appQuotaService.searchAppQuotas(data).then(function (res) {
            commit("updateAppQuotasList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteAppQuotaById({dispatch}, data) {
        api.appQuotaService.deleteAppQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryAppQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    updateAppQuota({dispatch}, data) {
        api.appQuotaService.updateAppQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryAppQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));

    },

    initAllQuotas({dispatch}, data) {
        api.appQuotaService.initAllQuotas().then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryAppQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    initAppQuotas({dispatch}, data) {
        api.appQuotaService.initAppQuotas(data.initAppQuota).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryAppQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewAppQuota({dispatch}, data) {
        api.appQuotaService.createAppQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryAppQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateAppQuotasList(state, data) {
        state.appQuotas = data.content;
        state.totalAppQuotas = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}