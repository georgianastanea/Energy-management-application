import {HOST} from '../../commons/hosts.js';
import { performRequest } from "../../commons/api/rest-client.js";


const endpoint = {
    person: '/person',
    register: '/register',
};

function getPersons(token, callback) {
    const url = HOST.backend_person_api + endpoint.person;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    performRequest(request, callback);
}


function getPersonByUsername(params, token, callback) {
    if (params) {
        const url = HOST.backend_person_api + endpoint.person + '/' + params;

        let request = new Request(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        performRequest(request, callback);
    } else {
        console.error('Invalid params. Make sure params.username is defined.');
    }
}

function getPersonIdByUsername(params, token, callback) {
    if (params) {
        const url = HOST.backend_person_api + endpoint.person + '/id/' + params;

        let request = new Request(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        performRequest(request, callback);
    } else {
        console.error('Invalid params. Make sure params.username is defined.');
    }
}

function postPerson(user, callback){
    let request = new Request(HOST.backend_person_api + endpoint.register , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });
    performRequest(request, callback);
}

function deletePerson(params, token, callback){
   const url = HOST.backend_person_api + endpoint.person + '/' + params;

   let request = new Request(url, {
       method: 'DELETE',
       headers: {
           'Authorization': `Bearer ${token}`
       }
   });

   performRequest(request, callback);
}

function updatePerson(params, user, token, callback){
    const url = HOST.backend_person_api + endpoint.person + '/' + params;

    let request = new Request(url, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(user)
    });

    performRequest(request, callback);
}



export {
    getPersons,
    getPersonByUsername,
    postPerson,
    deletePerson,
    updatePerson,
    getPersonIdByUsername
};
