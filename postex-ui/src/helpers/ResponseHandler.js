import {authenticationService} from "../services";

export function handleResponse(response) {
    return response.text().then(text => {
        const data = text && (/^{|\[/.test(text) ? JSON.parse(text) : text);
        if (!response.ok) {
            if (response.status === 401) {
                authenticationService.signOut();
                window.location.reload();
            }
            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }
        return data;
    });
}
