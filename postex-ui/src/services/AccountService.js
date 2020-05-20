import {constants} from "../constants";
import {authHeader, handleResponse} from "../helpers";

export const accountService = {
    getProfile,
    searchProfiles,
    getFriends,
    getSubscribers,
    getRecommendations,
    checkIfFriend,
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

function searchProfiles(search) {
    let url = new URL(`${constants.apiBaseUrl}/profiles`);
    url.search = new URLSearchParams({"search": search}).toString();
    return fetch(url.toString()).then(handleResponse)
}

function getFriends(id) {
    return fetch(`${constants.apiBaseUrl}/profile/${id}/friends`, {headers: authHeader()})
        .then(handleResponse);
}

function getSubscribers(id) {
    return fetch(`${constants.apiBaseUrl}/profile/${id}/subscribers`, {headers: authHeader()})
        .then(handleResponse);
}

function getRecommendations() {
    return fetch(`${constants.apiBaseUrl}/friends/recommendations`, {headers: authHeader()})
        .then(handleResponse);
}

function checkIfFriend(id) {
    return fetch(`${constants.apiBaseUrl}/friends/check/${id}`, {headers: authHeader()})
        .then(handleResponse);
}

function addFriend(id) {
    return fetch(`${constants.apiBaseUrl}/friends/${id}`, {
        method: 'POST',
        headers: authHeader()
    }).then(handleResponse);
}

function removeFriend(id) {
    return fetch(`${constants.apiBaseUrl}/friends/${id}`, {
        method: 'DELETE',
        headers: authHeader()
    }).then(handleResponse);
}
