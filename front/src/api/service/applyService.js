import restApi from '../restApi'

export default {

    queryByPage(request = {}) {
        let url = '/web/apply/queryByPage?applyUser=' + request.applyUser
            + '&status=' + request.status
            + '&page=' + request.page
            + '&size=' + request.size;
        return restApi.doGetRequest(url);
    },

    queryById(request = {}) {
        let url = '/admin/users';
        return restApi.doGetRequest(url);
    },

    updateStatus(request = {}) {
        let url = '/web/apply/updateStatus';
        return restApi.doPostRequest(url, request);
    }

}