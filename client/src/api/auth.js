// 认证相关API接口
import api from './axiosInstance';

// 认证相关API接口
export const authAPI = {
  /**
   * 用户注册
   * @param {Object} userData - 用户注册数据
   * @param {string} userData.username - 用户名
   * @param {string} userData.email - 邮箱
   * @param {string} userData.password - 密码
   * @param {string} userData.verificationCode - 验证码
   * @returns {Promise} 注册结果
   */
  register: (userData) => {
    return api.post('/auth/register', userData);
  },

  /**
   * 用户登录
   * @param {Object} credentials - 登录凭证
   * @param {string} credentials.email - 邮箱（仅支持邮箱登录）
   * @param {string} credentials.password - 密码
   * @returns {Promise} 登录结果，包含token
   */
  login: (credentials) => {
    return api.post('/auth/login', credentials);
  },

  /**
   * 发送验证码
   * @param {Object} data - 发送验证码数据
   * @param {string} data.email - 邮箱地址
   * @returns {Promise} 发送结果
   */
  sendVerificationCode: (data) => {
    return api.post('/auth/send-code', { email: data.email });
  },

  /**
   * 忘记密码/重置密码
   * @param {Object} data - 重置密码数据
   * @param {string} data.email - 邮箱
   * @param {string} data.verificationCode - 验证码
   * @param {string} data.newPassword - 新密码
   * @returns {Promise} 重置结果
   */
  resetPassword: (data) => {
    return api.post('/auth/reset-password', data);
  },

  /**
   * 用户登出
   * @returns {Promise} 登出结果
   */
  logout: () => {
    return api.post('/auth/logout');
  },

  /**
   * 获取当前用户信息
   * @returns {Promise} 用户信息
   */
  getCurrentUser: () => {
    return api.get('/auth/profile');
  },

  /**
   * 刷新Token
   * @param {string} refreshToken - 刷新token
   * @returns {Promise} 新的token
   */
  refreshToken: (refreshToken) => {
    return api.post('/auth/refresh', { refreshToken });
  }
};

export default authAPI;