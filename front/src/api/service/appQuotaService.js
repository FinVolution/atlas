import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    deleteAppQuota(request = {}){
        let url = "/web/appquotas/" + request.appQuotaId;
        return restApi.doDeleteRequest(url);
    },
    createAppQuota(request = {}) {
        let url = "/web/appquotas";
        return restApi.doPostRequest(url, request.newAppQuota);
    },

    getAllAppQuotas(request = {}) {
        let url = '/web/appquotas/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },

    searchAppQuotas(request = {}) {
        let url = '/web/appquotas/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&appId=' + request.appQuotaQuery.appId;

        if(request.appQuotaQuery.envId != null)
            url =url + '&envId=' + request.appQuotaQuery.envId;

        return restApi.doGetRequest(url);
    },

    updateAppQuota(request = {}) {
        let url = "/web/appquotas";
        return restApi.doPutRequest(url, request.newAppQuota)
    },

    initAllQuotas(request = {}) {
        let url = '/web/appquotas/init';
        return restApi.doPostRequest(url);
    },

    initAppQuotas(request = {}) {
        let url = '/web/appquotas/init?appId=' + request.appId;
        return restApi.doPostRequest(url);
    }

}
