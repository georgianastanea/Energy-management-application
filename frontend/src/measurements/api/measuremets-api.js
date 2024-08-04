import {HOST} from '../../commons/hosts.js';
import { performRequest } from "../../commons/api/rest-client.js";


const endpoint = {
    measurements: '/measurements'
};

function getMeasurements(params, token, callback) {
    const url = HOST.backend_person_api + endpoint.measurements + '/' + params;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    performRequest(request, callback);
};

export{
    getMeasurements
};