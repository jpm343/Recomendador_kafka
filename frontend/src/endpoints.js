import axios from 'axios';

let api = 'http://localhost:4040';
axios.defaults.baseURL = api;

export const DrinkResources = {
    index() {
        return axios.get(api + '/Drink/');
    },
    create(params) {
        return axios.post(api + '/Drink/', params);
    },
    delete(params) {
        return axios.delete(api + '/Drink/' + params.id);
    },
    edit(params, id) {
        return axios.put(api + '/Drink/' + id, { user: params });
    },
};