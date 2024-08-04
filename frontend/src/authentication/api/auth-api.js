import {HOST} from '../../commons/hosts.js';
import { performRequest } from "../../commons/api/rest-client.js";


const endpoint = {
    login: '/login',
};

function performLogin(username, password, callback){
    const requestBody = {
        username: username,
        password: password
    };

    let request = new Request(HOST.backend_person_api + endpoint.login, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody) 
    });
    performRequest(request, callback);
}


export {
    performLogin,
};