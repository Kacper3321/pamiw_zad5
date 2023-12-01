// axios-config.js
import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:1972/api', // Bazowy URL Twojego backendu
});

export default instance;
