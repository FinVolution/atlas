import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getEnvById(envId) {
        let url = '/web/envs/' + envId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllEnvs(request = {}) {
        let url = '/web/envs/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteEnv(request = {}) {
        let url = "/web/envs/" + request.envId;
        return restApi.doDeleteRequest(url);
    },
    createEnv(request = {}) {
        let url = "/web/envs";
        return restApi.doPostRequest(url, request.newEnv);
    },
    searchEnvs(request = {}) {
        let url = '/web/envs/condition?page=' + (request.page - 1) + '&size=' + request.pageSize ;
        if(request.queryEnv.registryCenterId != null)
            url = url + '&registryCenterId=' + request.queryEnv.registryCenterId;

        if(request.queryEnv.cloudId != null)
            url = url + '&cloudId=' + request.queryEnv.cloudId;


        return restApi.doGetRequest(url);
    },

    updateEnv(request = {}) {
        let url = "/web/envs";
        return restApi.doPutRequest(url, request.newEnv)
    }

}
