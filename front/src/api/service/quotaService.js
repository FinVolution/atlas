import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getQuotaById(quotaId) {
        let url = '/web/quotas/' + quotaId;
        return restApi.doGetRequest(url);
    },

    deleteQuota(request = {}){
        let url = "/web/quotas/" + request.quotaId;
        return restApi.doDeleteRequest(url);
    },
    createQuota(request = {}) {
        let url = "/web/quotas";
        return restApi.doPostRequest(url, request.newQuota);
    },

    //get all by page
    getAllQuotas(request = {}) {
        let url = '/web/quotas/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },

    //get all quota when quotaQuery is null
    searchQuotas(request = {}) {
        let url = '/web/quotas/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&orgName=' + request.quotaQuery.orgName;

        if(request.quotaQuery.envId != null)
            url =url + '&envId=' + request.quotaQuery.envId;

        return restApi.doGetRequest(url);
    },

    updateQuota(request = {}) {
        let url = "/web/quotas";
        return restApi.doPutRequest(url, request.newQuota)
    }

}
