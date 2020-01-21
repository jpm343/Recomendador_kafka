import axios from 'axios';

let api = 'http://localhost:3000/';
axios.defaults.baseURL = api;

export const usuariosResources = {
    user(token) {
        axios.defaults.headers.Authorization = 'Bearer ' + token;
        return axios.get(api + 'auth/user');
    },
    index() {
        return axios.get(api + 'users');
    },
    create(params) {
        return axios.post(api + 'users', params);
    },
    delete(params) {
        return axios.delete(api + 'users/' + params.id);
    },
    edit(params, id) {
        return axios.put(api + 'users/' + id, { user: params });
    },
};