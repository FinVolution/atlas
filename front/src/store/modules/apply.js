import * as types from '../mutation-types'
import {api} from '../../api'

const state = {
    applyList: [],
    applyCount: 0,
    promptMessage: {
        code: null,
        details: null
    }
};

const getters = {
    getApplyList: state => state.applyList,
    getApplyCount: state => state.applyCount,
    getPromptMessage: state => state.promptMessage
};

const actions = {
    displayPromptByResponseMsg({commit}, response){
        if (response != null && response.status != null && response.status == 200) {
            let data = response.data;
            commit("refreshPromptMessage", {code: data.code, details: data.details});
        } else {
            let errorMsg = "请求失败，";
            if (response == null) {
                errorMsg += "访问后端服务返回异常。";
            } else if (response.status != null && response.data.details != null) {
                // 发生后端处理过的错误
                errorMsg += "返回码：" + response.status + "，返回消息：" + response.data.details;
            } else if (response.status != null && response.status >= 400 && response.status < 500) {
                // 发生4XX错误
                errorMsg += "返回码：" + response.status + "，返回消息：" + response.statusText;
            } else if (response.status != null && response.status >= 500 && response.status < 600) {
                // 发生5XX错误
                errorMsg += "请检查后端服务是否工作正常。";
                errorMsg += "返回码：" + response.status + "，返回消息：" + response.statusText;
            } else if (response.status != null) {
                errorMsg += "返回码：" + response.status + "，返回消息：" + response.statusText;
            } else {
                errorMsg += "请检查后端服务是否工作正常。";
                errorMsg += "消息：" + response;
            }
            commit("refreshPromptMessage", {code: -1, details: errorMsg});
        }
    },

    fetchAppliesByPage({commit, dispatch}, data) {
        api.applyService.queryByPage(data).then(function (resp) {
            commit(types.REFRESH_APPLY_LIST, resp.data.details);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },

    updateApplyStatus({commit, dispatch}, data) {
        api.applyService.updateStatus(data.updateRequest).then(function (resp) {
            dispatch("fetchAppliesByPage", data.queryRequest);
            dispatch("displayPromptByResponseMsg", resp);
        }.bind(this)).catch(function (err) {
            dispatch("displayPromptByResponseMsg", err.response);
        }.bind(this));
    },
};

const mutations = {
    refreshPromptMessage(state, data) {
        state.promptMessage = data;
    },
    [types.REFRESH_APPLY_LIST] (state, data) {
        state.applyList = data.content;
        state.applyCount = data.totalElements;
    },
};

export default {
    state,
    getters,
    actions,
    mutations
}