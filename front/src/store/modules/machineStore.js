import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    machines: [],
    totalMachines: null
};

// getters
const getters = {
    getAllMachines: state => state.machines,
    getTotalMachines: state => state.totalMachines
};

// actions
const actions = {

    fetchAllMachines({commit}, data){
        api.machineService.getAllMachines(data).then(function (res) {
            commit("updateMachinesList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    deleteMachineById({dispatch}, data){
        api.machineService.deleteMachine(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllMachines", data);
                Vue.prototype.$message({
                    type: 'success',
                    message: res.data.message
                })
            } else {
                Vue.prototype.$message({
                    type: 'error',
                    message: res.data.message
                })
            }
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },


    updateMachine({dispatch}, data){
        api.machineService.updateMachine(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllMachines", data);
                Vue.prototype.$message({
                    type: 'success',
                    message: res.data.message
                });
            } else {
                Vue.prototype.$message({
                    type: 'error',
                    message: res.data.message
                })
            }
        });
    },

    createNewMachine({dispatch}, data){
        api.machineService.createMachine(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllMachines", data);
                Vue.prototype.$message({
                    type: 'success',
                    message: res.data.message
                })
            } else {
                Vue.prototype.$message({
                    type: 'error',
                    message: res.data.message
                })
            }
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    queryMachines({commit}, data) {
        api.machineService.searchMachines(data).then(function (res) {
            if (res.data.code == 0) {
                commit("updateMachinesList", res.data.detail);
                Vue.prototype.$message({
                    type: 'success',
                    message: res.data.message
                })
            } else {
                Vue.prototype.$message({
                    type: 'error',
                    message: res.data.message
                })
            }
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

};

// mutations
const mutations = {
    updateMachinesList(state, data) {
        state.machines = data.content;
        state.totalMachines = data.totalElements;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}