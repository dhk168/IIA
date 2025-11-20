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
  },
  
  // 更新任务
  updateTask: async (taskData) => {
    const response = await axiosInstance.post('/reminder/task/update', taskData);
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
  // 获取所有标签
  getAllTags: async () => {
    const response = await axiosInstance.get('/reminder/tags/get-all');
    return response;
  },
  
  // 获取单个标签
  getTagById: async (tagId) => {
    const response = await axiosInstance.get(`/reminder/tags/get/${tagId}`);
    return response;
  },
  
  // 创建标签
  createTag: async (tagData) => {
    const response = await axiosInstance.post('/reminder/tags/create', tagData);
    return response;
  },
  
  // 更新标签
  updateTag: async (tagId, tagData) => {
    const response = await axiosInstance.put(`/reminder/tags/update/${tagId}`, tagData);
    return response;
  },
  
  // 检查标签名称是否存在
  existsByName: async (name) => {
    // 这个接口可能需要在后端补充，这里暂时通过获取所有标签来检查
    const response = await axiosInstance.get('/reminder/tags/get-all');
    return response.data.some(tag => tag.name === name);
  }
};

// 任务标签关联API
export const taskTagAPI = {
  // 创建任务-标签关联
  createTaskTag: async (taskTagData) => {
    const response = await axiosInstance.post('/reminder/task-tags/create', taskTagData);
    return response;
  },
  
  // 批量创建任务-标签关联
  createTaskTagBatch: async (batchData) => {
    // 转换为后端期望的List<TaskTag>格式
    const taskTagList = batchData.tagIds.map(tagId => ({
      taskId: batchData.taskId,
      tagId: tagId
    }));
    const response = await axiosInstance.post('/reminder/task-tags/create-batch', taskTagList);
    return response;
  },
  
  // 获取任务的所有标签关联
  getTaskTagsByTaskId: async (taskId) => {
    const response = await axiosInstance.get(`/reminder/task-tags/get-by-task/${taskId}`);
    return response;
  },
  
  // 获取标签的所有任务关联
  getTaskTagsByTagId: async (tagId) => {
    const response = await axiosInstance.get(`/reminder/task-tags/get-by-tag/${tagId}`);
    return response;
  },
  
  // 删除特定的任务-标签关联
  deleteTaskTag: async (taskId, tagId) => {
    const response = await axiosInstance.delete('/reminder/task-tags/delete', {
      params: { taskId, tagId }
    });
    return response;
  },
  
  // 批量删除任务-标签关联
  deleteTaskTagBatch: async (taskId, tagIds) => {
    const response = await axiosInstance.delete('/reminder/task-tags/delete-batch', {
      params: { taskId },
      data: tagIds
    });
    return response;
  },
  
  // 根据任务ID删除所有关联
  deleteTaskTagsByTaskId: async (taskId) => {
    const response = await axiosInstance.delete(`/reminder/task-tags/delete-by-task/${taskId}`);
    return response;
  },
  
  // 根据标签ID删除所有关联
  deleteTaskTagsByTagId: async (tagId) => {
    const response = await axiosInstance.delete(`/reminder/task-tags/delete-by-tag/${tagId}`);
    return response;
  }
};

export default {
  reminderProjectAPI,
  reminderTaskAPI,
  recurrenceAPI,
  tagAPI
};