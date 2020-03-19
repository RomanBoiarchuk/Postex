import {BehaviorSubject} from 'rxjs';
import {constants} from "../constants";
import {handleResponse} from "../helpers";

const isSignedInSubject = new BehaviorSubject(localStorage.getItem("token") != null);
const accountSubject = new BehaviorSubject(JSON.parse(localStorage.getItem("account")));

export const authenticationService = {
    signUp,
    signIn,
    signOut,
    isSignedInSubject,
    accountSubject,
    get authToken() {
        return localStorage.getItem("token");
    },
    get isSignedIn() {
        return isSignedInSubject.value;
    },
    get account() {
        return accountSubject.value;
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
        .then(body => {
            localStorage.setItem('token', body.token.toString());
            localStorage.setItem('account', JSON.stringify(body.account))
            isSignedInSubject.next(true);
            return body.account;
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
        .then(body => {
            localStorage.setItem('token', body.token.toString());
            localStorage.setItem('account', JSON.stringify(body.account))
            isSignedInSubject.next(true);
            return body.account;
        });
}

function signOut() {
    localStorage.removeItem("token");
    localStorage.removeItem("account");
    isSignedInSubject.next(false);
}
