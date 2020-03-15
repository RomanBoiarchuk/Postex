import {constants} from "../constants";
import {authHeader, handleResponse} from "../helpers";

export const accountService = {
    getProfile,
    getFriends,
    getSubscribers,
    addFriend,
    removeFriend
}

function getProfile(id = null) {
    if (id === null) {
        return fetch(`${constants.apiBaseUrl}/profile`, {headers: authHeader()})
            .then(handleResponse);
    } else {
        return fetch(`${constants.apiBaseUrl}/profile/${id}`, {headers: authHeader()})
            .then(handleResponse);
    }
}

function getFriends(id) {
    return fetch(`${constants.apiBaseUrl}/profile/${id}/friends`, {headers: authHeader()})
        .then(handleResponse);
}

function getSubscribers(id) {
    return fetch(`${constants.apiBaseUrl}/profile/${id}/subscribers`, {headers: authHeader()})
        .then(handleResponse);
}

function addFriend(id) {
    return fetch(`${constants.apiBaseUrl}/friends/${id}`, {
        method: 'POST',
        headers: authHeader()
    })
        .then(handleResponse);
}

function removeFriend(id) {
    return fetch(`${constants.apiBaseUrl}/friends/${id}`, {
        method: 'DELETE',
        headers: authHeader()
    })
        .then(handleResponse);
}
