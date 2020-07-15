import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getOrgById(orgId) {
        let url = '/web/orgs/' + orgId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllOrgs(request = {}) {
        let url = '/web/orgs/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteOrg(request = {}) {
        let url = "/web/orgs/" + request.orgId;
        return restApi.doDeleteRequest(url);
    },
    createOrg(request = {}) {
        let url = "/web/orgs";
        return restApi.doPostRequest(url, request.newOrg);
    },
    searchOrgs(request = {}) {
        let url = '/web/orgs/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&name=' + request.queryOrg.name;

        if (request.queryOrg.userId != null) {
            url = url + "&userWorkNumber=" + request.queryOrg.userWorkNumber;
        }

        return restApi.doGetRequest(url);
    },

    updateOrg(request = {}) {
        let url = "/web/orgs";
        return restApi.doPutRequest(url, request.newOrg)
    }

}
