import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getCloudById(cloudId) {
        let url = '/web/sites/' + cloudId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllClouds(request = {}) {
        let url = '/web/clouds/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteCloud(request = {}){
        let url = "/web/clouds/" + request.cloudId;
        return restApi.doDeleteRequest(url);
    },
    createCloud(request = {}) {
        let url = "/web/clouds";
        return restApi.doPostRequest(url, request.newCloud);
    },
    searchClouds(request = {}) {
        let url = '/web/clouds/condition?page=' + (request.page - 1) + '&size=' + request.pageSize;
        url = url + '&cloudName=' + request.cloudName;
        return restApi.doGetRequest(url);
    },

    updateCloud(request = {}) {
        let url = "/web/clouds";
        return restApi.doPutRequest(url, request.newCloud)
    }

}
