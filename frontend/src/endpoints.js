import axios from 'axios';

let api = 'http://localhost:3000/';
axios.defaults.baseURL = api;

export const DrinkResources = {
    index() {
        return axios.get(api + 'drink');
    },
    create(params) {
        return axios.post(api + 'drink', params);
    },
    delete(params) {
        return axios.delete(api + 'drink/' + params.id);
    },
    edit(params, id) {
        return axios.put(api + 'drink/' + id, { user: params });
    },
};