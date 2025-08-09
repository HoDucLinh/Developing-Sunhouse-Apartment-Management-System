import axios from "axios";
import cookie from 'react-cookies';

const BASE_URL = 'http://localhost:8081/api/';

export const endpoints = {
  login: 'user/login',
  profile: 'user/secure/profile',
  changePassword: (userId) => `user/change-password/${userId}`,
  editProfile: (userId) => `user/update-profile/${userId}`,
  createAppointment : 'appointment/create-appointment',
  addRelative: 'relative/add-relative',
  getRelatives: (userId) => `relative/get-relatives/${userId}`,
  getfeedbacks: (userId) => `feedback/get-feedback/${userId}`,
  createFeedback :'feedback/create-feedback',
  deleteFeedback :(userId) => `feedback/delete-feedback/${userId}`,
  updateFeedback : (userId) => `feedback/update-feedback/${userId}`,
  getPackages : (userId) => `package/get-packages/${userId}`
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
