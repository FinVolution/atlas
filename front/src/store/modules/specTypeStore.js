import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    specTypes: [],
    totalSpecTypes: null
};

// getters
const getters = {
    getAllSpecTypes: state => state.specTypes,
    getTotalSpecTypes: state => state.totalSpecTypes
};

// actions
const actions = {

    fetchAllSpecTypes({commit}, data) {
        api.specTypeService.getAllSpecTypes(data).then(function (res) {
            commit("updateSpecTypeList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteSpecTypeById({dispatch}, data) {
        api.specTypeService.deleteSpecType(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllSpecTypes", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    updateSpecType({dispatch}, data) {
        api.specTypeService.updateSpecType(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllSpecTypes", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewSpecType({dispatch}, data) {
        api.specTypeService.createSpecType(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllSpecTypes", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    querySpecTypes({commit}, data) {
        api.specTypeService.searchSpecTypes(data).then(function (res) {
            commit("updateSpecTypeList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateSpecTypeList(state, data) {
        state.specTypes = data.content;
        state.totalSpecTypes = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}