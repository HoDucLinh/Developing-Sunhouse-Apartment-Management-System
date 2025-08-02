import axios from "axios";
import cookie from 'react-cookies';

const BASE_URL = 'http://localhost:8081/api/';

export const endpoints = {
  login: 'user/login',
  profile: 'user/secure/profile',
  changePassword: (userId) => `user/change-password/${userId}`,
  editProfile: (userId) => `user/update-profile/${userId}`,
  createAppointment : 'appointment/create-appointment'
};

// Axios không có token (dùng cho login, register,...)
export const publicApi = axios.create({
  baseURL: BASE_URL
});

// Axios có token (dùng sau login)
export const authApis = (token = null) => {
  if (!token) {
    token = cookie.load('token');
  }

  return axios.create({
    baseURL: BASE_URL,
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
};
