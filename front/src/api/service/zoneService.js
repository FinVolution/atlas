import restApi from '../restApi'
import {handResponse} from "~/common/common";

export default {
    getZoneById(cloudId) {
        let url = '/web/zones/' + cloudId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllZones(request = {}) {
        let url = '/web/zones/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteZone(request = {}){
        let url = "/web/zones/" + request.zoneId;
        return restApi.doDeleteRequest(url);
    },
    createZone(request = {}) {
        let url = "/web/zones";
        return restApi.doPostRequest(url, request.newZone);
    },
    searchZones(request = {}) {
        let url = '/web/zones/condition?page=' + (request.page - 1) + '&size=' + request.pageSize;
        url = url + '&envName=' + request.envName + '&zoneName=' + request.zoneName;
        return restApi.doGetRequest(url);
    },

    updateZone(request = {}) {
        let url = "/web/zones";
        return restApi.doPutRequest(url, request.newZone)
    }

}
