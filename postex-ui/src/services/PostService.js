import {constants} from "../constants";
import {authHeader, handleResponse} from "../helpers";

export const postService = {
    feed,
    getMyPosts,
    getPostsByAuthor,
    findPostsByTag,
    getPost,
    createPost,
    updatePost,
    deletePost,
    setLike,
    removeLike,
    getCommentsByPost,
    createComment
}

function feed() {
    return fetch(`${constants.apiBaseUrl}/feed`, {headers: authHeader()})
        .then(handleResponse);
}

function getMyPosts() {
    return fetch(`${constants.apiBaseUrl}/profile/me/posts`, {headers: authHeader()})
        .then(handleResponse);
}

function getPostsByAuthor(id) {
    return fetch(`${constants.apiBaseUrl}/profile/${id}/posts`, {headers: authHeader()})
        .then(handleResponse);
}

function findPostsByTag(tag) {
    let url = new URL(`${constants.apiBaseUrl}/posts`);
    url.search = new URLSearchParams({"tag": tag}).toString();
    return fetch(url.toString()).then(handleResponse);
}

function getPost(id) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}`, {headers: authHeader()})
        .then(handleResponse);
}

function createPost(post) {
    let headers = authHeader();
    headers['Content-Type'] = 'application/json';
    return fetch(`${constants.apiBaseUrl}/posts`, {
        method: 'POST',
        headers,
        body: JSON.stringify(post)
    })
        .then(handleResponse);
}

function updatePost(id, post) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}`, {
        method: 'PUT',
        headers: authHeader(),
        body: JSON.stringify(post)
    })
        .then(handleResponse);
}

function deletePost(id) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}`, {
        method: 'DELETE',
        headers: authHeader()
    })
        .then(handleResponse);
}

function setLike(id) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}/likes`, {
        method: 'POST',
        headers: authHeader(),
    })
        .then(handleResponse);
}

function removeLike(id) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}/likes`, {
        method: 'DELETE',
        headers: authHeader(),
    })
        .then(handleResponse);
}

function getCommentsByPost(id) {
    return fetch(`${constants.apiBaseUrl}/posts/${id}/comments`, {headers: authHeader()})
        .then(handleResponse);
}

function createComment(id, comment) {
    let headers = authHeader();
    headers['Content-Type'] = 'application/json';
    return fetch(`${constants.apiBaseUrl}/posts/${id}/comments`, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(comment)
    })
        .then(handleResponse);
}
