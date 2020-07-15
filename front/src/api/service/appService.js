import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    //get all by page
    getAllApps(request = {}) {
        let url = '/web/apps/page/plus?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteApp(request = {}) {
        let url = "/web/apps/" + request.appId;
        return restApi.doDeleteRequest(url);
    },
    createApp(request = {}) {
        let url = "/web/apps";
        return restApi.doPostRequest(url, request.newApp);
    },

    fuzzySearchAppByAppName(request = {}) {
        let url = '/web/apps/fuzzy/appname?appName=' + request.appName;
        return restApi.doGetRequest(url);
    },

    queryAppsByAppName(request = {}) {
        let url = '/web/apps/appname?appName=' + request.appName;
        return restApi.doGetRequest(url);
    },

    searchApps(request = {}) {
        let url = '/web/apps/condition/plus?page=' + (request.page - 1) + '&size=' + request.pageSize;

        //判断查询参数是否为空
        if (request.queryApp.appId != null) {
            url = url + '&appId=' + request.queryApp.appId;
        }

        url = url + '&userWorkNumber=' + request.queryApp.userWorkNumber;
        url = url + '&appName=' + request.queryApp.appName;

        if (request.queryApp.orgId != null) {
            url = url + '&orgId=' + request.queryApp.orgId;
        }

        return restApi.doGetRequest(url);
    },

    updateApp(request = {}) {
        let url = "/web/apps";
        return restApi.doPutRequest(url, request.newApp)
    }

}
