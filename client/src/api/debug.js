import axiosInstance from './axiosInstance';

// Debug相关API接口
export const debugAPI = {
  /**
   * 清除所有数据库表内容
   * @returns {Promise} 清除结果
   */
  clearAllData: () => {
    return axiosInstance.post('/debug/clear-all-data');
  },

  /**
   * 清除所有提醒模块数据表内容
   * @returns {Promise} 清除结果
   */
  clearReminderData: () => {
    return axiosInstance.post('/debug/clear-reminder-data');
  }
};

export default debugAPI;