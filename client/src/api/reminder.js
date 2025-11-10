import axiosInstance from './axiosInstance';

// 项目管理API
export const reminderProjectAPI = {
  // 获取所有项目
  getAllProjects: async () => {
    const response = await axiosInstance.get('/reminder/projects/get-all');
    return response;
  },
  
  // 获取单个项目
  getProjectById: async (projectId) => {
    const response = await axiosInstance.get(`/reminder/projects/get/${projectId}`);
    return response;
  },
  
  // 创建项目
  createProject: async (projectData) => {
    const response = await axiosInstance.post('/reminder/projects/create', projectData);
    return response;
  },
  
  // 更新项目
  updateProject: async (projectData) => {
    const response = await axiosInstance.post('/reminder/projects/update', projectData);
    return response;
  },
  
  // 批量更新项目位置
  batchUpdatePosition: async (positionData) => {
    const response = await axiosInstance.post('/reminder/projects/batch-update-position', positionData);
    return response;
  }
};

// 任务管理API
export const reminderTaskAPI = {
  // 创建任务
  createTask: async (taskData) => {
    const response = await axiosInstance.post('/reminder/task/create', taskData);
    return response;
  },
  
  // 获取任务（可以根据后续controller补充）
  getTasks: async (params) => {
    const response = await axiosInstance.get('/reminder/task', { params });
    return response;
  }
};

// 循环任务API
export const recurrenceAPI = {
  // 创建循环任务配置
  createRecurrence: async (recurrenceData) => {
    const response = await axiosInstance.post('/reminder/recurrences', recurrenceData);
    return response;
  },
  
  // 获取即将发生的循环任务
  getUpcomingRecurrences: async (deadline) => {
    const response = await axiosInstance.get('/reminder/recurrences/upcoming', {
      params: { deadline }
    });
    return response;
  }
};

// 标签管理API
export const tagAPI = {
  // 获取所有标签（可以根据后续controller补充）
  getAllTags: async () => {
    const response = await axiosInstance.get('/reminder/tags');
    return response;
  }
};

export default {
  reminderProjectAPI,
  reminderTaskAPI,
  recurrenceAPI,
  tagAPI
};