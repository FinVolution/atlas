import * as types from '../mutation-types'
import {api} from '../../api'
import Vue from 'vue'


// initial state
const state = {
    users: [],
    usersByName: [],
    testUsersByName: [],
    totalUsers: null,
};

// getters
const getters = {
    getAllUsers: state => state.users,
    getUsersByName: state => state.usersByName,
    getTestUsersByName: state => state.testUsersByName,
    getTotalUsers: state => state.totalUsers,
};

// actions
const actions = {

    fetchAllUsers({commit}, data) {
        api.userService.getAllUsers(data).then(function (res) {
            let userArray = res.data.detail;
            commit("updateUsersList", userArray);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },


    initUsersByName({commit}, data) {
        commit("updateUsersByNameList", data);
    },

    initTestUsersByName({commit}, data) {
        commit("updateTestUsersByNameList", data);
    },

    deleteUserById({dispatch}, data) {
        api.userService.deleteUser(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllUsers", data);
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


    updateUser({dispatch}, data) {
        api.userService.updateUser(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllUsers", data);
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

    getRoleNamesByUserId({commit}, data) {
        api.userService.getRolesByUserId(data).then(function (res) {
            //get names only
            let roleNames = res.data.detail.map(function (item) {
                return item.name;
            });
            //let roleNames = Array.from(res.data.detail, m => m.name);
            commit("updateRolesForOneUser", roleNames);

        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    addUserRole({dispatch}, data) {
        api.userService.addUserRole(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryUsers", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    }
    ,

    createNewUser({dispatch}, data) {
        api.userService.createUser(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("fetchAllUsers", data);
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

    queryUsers({commit}, data) {
        api.userService.searchUsers(data).then(function (res) {
            commit("updateUsersList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    //名字模糊查询
    queryUsersByUserName({commit}, data) {
        api.userService.fuzzySearchUsersByName(data).then(function (res) {
            if(data.type==null) {
                commit("updateUsersByNameList", res.data.detail);
            }else{
                commit("updateTestUsersByNameList", res.data.detail);
            }

        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },

    //used to update org a user belonging to

    addUserOrg({dispatch}, data) {
        api.userService.addUserOrg(data).then(function (res) {
            if (res.data.code == 0) {
                dispatch("queryUsers", data);
            }
            dispatch("displayPromptByResponseMsg", res);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    }

};

// mutations
const mutations = {
    updateUsersList(state, data) {
        state.users = data.content;
        state.totalUsers = data.totalElements;

    },

    updateUsersByNameList(state, data) {
        state.usersByName = data;
    },

    updateTestUsersByNameList(state, data) {
        state.testUsersByName = data;
    },

    updateRolesForOneUser(state, data) {
        state.roleNamesOfOneUser = data;
    },
};

export default {
    state,
    getters,
    actions,
    mutations
}