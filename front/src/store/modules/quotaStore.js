import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    quotas: [],
    totalQuotas: null
};

// getters
const getters = {
    getAllQuotas: state => state.quotas,
    getTotalQuotas: state => state.totalQuotas
};

// actions
const actions = {

    fetchAllQuotas({commit}, data) {
        api.quotaService.getAllQuotas(data).then(function (res) {
            commit("updateQuotasList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    queryQuotas({commit}, data) {
        api.quotaService.searchQuotas(data).then(function (res) {
            commit("updateQuotasList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteQuotaById({dispatch}, data) {
        api.quotaService.deleteQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateQuota({dispatch}, data) {
        api.quotaService.updateQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));

    },

    createNewQuota({dispatch}, data) {
        api.quotaService.createQuota(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllQuotas", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateQuotasList(state, data) {
        state.quotas = data.content;
        state.totalQuotas = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}