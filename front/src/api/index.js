import axios from 'axios'
import service from './service'

axios.defaults.timeout = 50000;
axios.defaults.headers.common['Authorization'] = "31313113";
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.put['Content-Type'] = 'application/json';

export const api = service;

