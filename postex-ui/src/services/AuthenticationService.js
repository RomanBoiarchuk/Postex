import {BehaviorSubject} from 'rxjs';
import {constants} from "../constants";
import {handleResponse} from "../helpers";

const isSignedInSubject = new BehaviorSubject(localStorage.getItem("token") != null);

export const authenticationService = {
    signUp,
    signIn,
    signOut,
    isSignedInSubject,
    get authToken() {
        return localStorage.getItem("token")
    },
    get isSignedIn() {
        return isSignedInSubject.value
    }
}

function signUp(account) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(account)
    };

    return fetch(`${constants.apiBaseUrl}/signup`, requestOptions)
        .then(handleResponse)
        .then(token => {
            localStorage.setItem('token', token.toString());
            isSignedInSubject.next(true);
            return token;
        });
}

function signIn(username, password) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    };

    return fetch(`${constants.apiBaseUrl}/signin`, requestOptions)
        .then(handleResponse)
        .then(token => {
            localStorage.setItem('token', token.toString());
            isSignedInSubject.next(true);
            return token;
        });
}

function signOut() {
    localStorage.removeItem("token");
    isSignedInSubject.next(false);
}
