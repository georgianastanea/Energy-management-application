import { HOST } from '../../commons/hosts.js';
import { performRequest } from "../../commons/api/rest-client.js";
import Axios from 'axios';

const endpoint = {
    device: '/device',
    delete: '/delete',
    update: '/update',
    create: '/create'
};


function getDevices(token, callback) {
    const url = HOST.backend_person_api + endpoint.device;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    console.log("request " + request);
    performRequest(request, callback);
}

function getDevicesByPersonId(params, token, callback) {
    console.log("params in getDevicesByPersonId " + params)
    if (params) {
        const url = HOST.backend_person_api + endpoint.device + '/person/' + params;
        let request = new Request(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        performRequest(request, callback);
    } else {
        console.error('Invalid params. Make sure params.id is defined.');
    }
}


function postDevice(device, token, callback) {
    const url = HOST.backend_person_api + endpoint.device;
    let request = new Request(url, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(device)
    });
    performRequest(request, callback);
}

function deleteDeviceById(params, token) {
    console.log("delete device by id " + params);
    Axios.delete(HOST.backend_person_api + endpoint.device + '/' + params, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
}

function updateDevice(params, device, token, callback) {
    let request = new Request(HOST.backend_person_api + endpoint.device + '/' + params,{
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(device)
    });
    performRequest(request, callback);
}


export {
    getDevices,
    postDevice,
    deleteDeviceById,
    updateDevice,
    getDevicesByPersonId,
};
