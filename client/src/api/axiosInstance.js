// axios实例配置及拦截器
import axios from 'axios';

// 创建axios实例
const api = axios.create({
  // Apifox提供的API地址
  // baseURL: 'http://127.0.0.1:4523/m1/7205231-6931300-default/api',
  // 部署到服务器后，需要修改为服务器地址
  baseURL: 'http://127.0.0.1:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器，添加token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器，处理错误
api.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    if (error.response) {
      // 服务器返回错误状态码
      switch (error.response.status) {
        case 401:
          // 未授权，清除本地存储并重定向到登录页
          localStorage.removeItem('isLoggedIn');
          localStorage.removeItem('usernameOrEmail');
          localStorage.removeItem('token');
          window.location.href = '/login';
          break;
        case 403:
          console.error('Forbidden');
          break;
        case 404:
          console.error('Not Found');
          break;
        case 500:
          console.error('Internal Server Error');
          break;
        default:
          console.error(error.response.data.message || 'Request failed');
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('Network Error');
    } else {
      // 设置请求时发生错误
      console.error('Request Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;