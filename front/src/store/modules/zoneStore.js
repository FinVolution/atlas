import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    zones: [],
    totalZones: null
};

// getters
const getters = {
    getAllZones: state => state.zones,
    getTotalZones: state => state.totalZones
};

// actions
const actions = {

    fetchAllZones({commit}, data) {
        api.zoneService.getAllZones(data).then(function (res) {
            commit("updateZonesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteZoneById({dispatch}, data) {
        api.zoneService.deleteZone(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllZones", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },


    updateZone({dispatch}, data) {
        api.zoneService.updateZone(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryZones", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    createNewZone({dispatch}, data) {
        api.zoneService.createZone(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllZones", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    queryZones({commit}, data) {
        api.zoneService.searchZones(data).then(function (res) {
            commit("updateZonesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateZonesList(state, data) {
        state.zones = data.content;
        state.totalZones = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}