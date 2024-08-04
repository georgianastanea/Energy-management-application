import { HOST } from '../../commons/hosts'
import { performRequest } from '../../commons/api/rest-client';
import { callback } from 'react';

const endpoint = {
    chat: '/chat',
};

function getConnectedPersons(token, callback) {
    const url = HOST.backend_person_api + endpoint.chat;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    performRequest(request, callback);
}

function getMessagesForPerson(token, selectedPerson, callback) {
    const loggedUser = sessionStorage.getItem("username");
    const url = HOST.backend_person_api + endpoint.chat + '/messages/' + loggedUser + '/' + selectedPerson;
    let request = new Request(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    performRequest(request, callback);
}

export {
    getConnectedPersons,
    getMessagesForPerson
};