import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getSpecTypeById(specTypeId) {
        let url = '/web/specTypes/' + specTypeId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllSpecTypes(request = {}) {
        let url = '/web/specTypes/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteSpecType(request = {}){
        let url = "/web/specTypes/" + request.specTypeId;
        return restApi.doDeleteRequest(url);
    },
    createSpecType(request = {}) {
        let url = "/web/specTypes";
        return restApi.doPostRequest(url, request.newSpecType);
    },
    searchSpecTypes(request = {}) {
        let url = '/web/specTypes/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&name=' + request.specTypeQuery.name;

        if(request.specTypeQuery.cpu != null)
            url = url + '&cpu='+ request.specTypeQuery.cpu;
        if(request.specTypeQuery.memory != null)
            url = url +'&memory=' + request.specTypeQuery.memory;
        if(request.specTypeQuery.disk != null)
            url = url + '&disk=' + request.specTypeQuery.disk;

        return restApi.doGetRequest(url);
    },

    updateSpecType(request = {}) {
        let url = "/web/specTypes";
        return restApi.doPutRequest(url, request.newSpecType)
    }

}
