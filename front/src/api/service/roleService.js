import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getRoleById(roleId) {
        let url = '/web/roles/' + roleId;
        return restApi.doGetRequest(url);
    },
    getAllRoles(request = {}) {
        let url = '/web/roles/all';
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllRolesByPage(request = {}) {
        let url = '/web/roles/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteRole(request = {}){
        let url = "/web/roles/" + request.roleId;
        return restApi.doDeleteRequest(url);
    },
    createRole(request = {}) {
        let url = "/web/roles";
        return restApi.doPostRequest(url, request.newRole);
    },
    searchRoles(request = {}) {
        let url = '/web/roles/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&name=' + request.queryRole.name;

        return restApi.doGetRequest(url);
    },

    updateRole(request = {}) {
        //debugger
        let url = "/web/roles";
        return restApi.doPutRequest(url, request.newRole)
    }

}
