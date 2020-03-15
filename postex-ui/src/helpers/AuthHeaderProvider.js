import {authenticationService} from "../services";

export function authHeader() {
    const token = authenticationService.authToken;
    if (token) {
        return { Authorization: token };
    } else {
        return {};
    }
}
