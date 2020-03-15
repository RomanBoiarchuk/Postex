import {constants} from "../constants";
import {authHeader, handleResponse} from "../helpers";

export const accountService = {
    getProfile
}

function getProfile() {
    return fetch(`${constants.apiBaseUrl}/profile`, {headers: authHeader()})
        .then(handleResponse);
}
