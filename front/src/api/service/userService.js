import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getUserById(userId) {
        let url = '/web/users/' + userId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllUsers(request = {}) {
        let url = '/web/users/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteUser(request = {}) {
        let url = "/web/users/" + request.userId;
        return restApi.doDeleteRequest(url);
    },
    createUser(request = {}) {
        let url = "/web/users";
        return restApi.doPostRequest(url, request.newUser);
    },

    searchUsers(request = {}) {
        let url = '/web/users/condition?page=' + (request.page - 1) + '&size=' + request.pageSize + '&realName=' + request.queryUser.realName + '&userName=' + request.queryUser.userName;
        //if(request.queryUser.orgId != null)
        url = url + '&orgCode=' + request.queryUser.orgCode;

        return restApi.doGetRequest(url);
    },

    //模糊查询
    fuzzySearchUsersByName(request = {}) {
        let url = '/web/users/fuzzy/username/?userName=' + request.userName;
        return restApi.doGetRequest(url);
    },

    updateUser(request = {}) {
        let url = "/web/users";
        return restApi.doPutRequest(url, request.newUser)
    },

    //get roles by userId
    getRolesByUserId(request = {}) {
        let url = "/web/userroles/roles/" + request.userId;
        return restApi.doGetRequest(url)
    },

    //add UserRole
    addUserRole(request = {}) {
        let url = '/web/userroles/' + request.userWorkNumber;
        return restApi.doPostRequest(url, request.roleIds);
    },
    
    //add user org
    addUserOrg(request = {}) {
        let url = '/web/userexts/';
        return restApi.doPostRequest(url, request.newUserExt);
    }

}
