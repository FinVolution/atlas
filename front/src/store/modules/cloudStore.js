import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    clouds: [],
    totalClouds: null
};

// getters
const getters = {
    getAllClouds: state => state.clouds,
    getTotalClouds: state => state.totalClouds
};

// actions
const actions = {

    fetchAllClouds({commit}, data) {
        api.cloudService.getAllClouds(data).then(function (res) {
            commit("updateCloudsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteCloudById({dispatch}, data) {
        api.cloudService.deleteCloud(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllClouds", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateCloud({dispatch}, data) {
        api.cloudService.updateCloud(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryClouds", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewCloud({dispatch}, data) {
        api.cloudService.createCloud(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllClouds", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    queryClouds({commit}, data) {
        api.cloudService.searchClouds(data).then(function (res) {
            commit("updateCloudsList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateCloudsList(state, data) {
        state.clouds = data.content;
        state.totalClouds = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}