<template>
  <div class="tasks-container">
    <div class="page-header">
      <div class="header-actions">
        <el-button type="primary" light @click="showAddTaskDialog = true"  class="light-button">
          New Task
        </el-button>
        <el-button light @click="showTagsDialog = true" class="light-button">
          Tags Management
        </el-button>
      </div>
    </div>
    
    <!-- 任务筛选 -->
    <div class="filter-section">
      <el-row :gutter="20">
        <el-col :md="6" :sm="12">
          <el-select v-model="filterOptions.status" placeholder="Task Status" clearable class="glass-filter">
            <el-option label="All" value="" />
            <el-option label="To Do" value="todo" />
            <el-option label="Completed" value="done" />
            <el-option label="Abandoned" value="abandoned" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="12">
          <el-select v-model="filterOptions.project" placeholder="Project" clearable class="glass-filter">
            <el-option label="All" value="" />
            <el-option v-for="project in projects" :key="project?.project_id || 'invalid'" :label="project?.name || 'Invalid Project'" :value="project?.project_id || ''" v-if="project" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="12">
          <el-select v-model="filterOptions.priority" placeholder="Priority" clearable class="glass-filter">
            <el-option label="All" value="" />
            <el-option label="None" value="none" />
            <el-option label="Low" value="low" />
            <el-option label="Medium" value="medium" />
            <el-option label="High" value="high" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="12">
          <el-select v-model="filterOptions.tag" placeholder="Tags Filter" clearable class="glass-filter">
            <el-option label="All" value="" />
            <el-option v-for="tag in tags" :key="tag.tag_id" :label="tag.name" :value="tag.tag_id" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="12">
          <el-date-picker v-model="filterOptions.dateRange" type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date" class="glass-filter" />
        </el-col>
      </el-row>
    </div>
    
    <!-- 任务列表 -->
    <el-card class="tasks-card">
      <template #header>
        <div class="card-header">
          <span>Task List</span>
        </div>
      </template>
      <el-table :data="filteredTasks" stripe style="width: 100%" default-expand-all>
        <el-table-column type="expand" width="40">
          <template #default="scope">
            <div class="task-detail">
              <div class="detail-item" v-if="scope.row.description">
                <strong>Description:</strong>
                <p>{{ scope.row.description }}</p>
              </div>
              <div class="detail-item">
                <strong>Created At:</strong>
                <span>{{ formatDate(scope.row.created_at) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.start_date">
                <strong>Start Date:</strong>
                <span>{{ formatDate(scope.row.start_date) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.completed_at">
                <strong>Completed At:</strong>
                <span>{{ formatDate(scope.row.completed_at) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.recurrence_info">
                <strong>Recurrence Rule:</strong>
                <span>{{ getRecurrenceText(scope.row.recurrence_info) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.tags && scope.row.tags.length > 0">
                <strong>Tags:</strong>
                <div class="task-tags">
                  <el-tag 
                    v-for="tag in scope.row.tags" 
                    :key="tag.tagId" 
                    :type="getTagType(tag.color)" 
                    size="small" 
                    effect="plain"
                  >
                    {{ tag.name }}
                  </el-tag>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="Task Name" min-width="300">
          <template #default="scope">
            <div class="task-title-container">
              <el-checkbox v-model="scope.row.status" :checked="scope.row.status === 'done'" @change="updateTaskStatus(scope.row)">
                <span :class="{ 'task-completed': scope.row.status === 'done' }" :style="{ paddingLeft: scope.row.level * 20 + 'px' }">{{ scope.row.title }}</span>
              </el-checkbox>
              <el-tag v-if="scope.row.category === 'note'" size="small" type="info" style="margin-left: 10px;">Note</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="project_name" label="Project" width="120" />
        <el-table-column prop="due_date" label="Due Date" width="120">
          <template #default="scope">
            <el-tag :type="getTaskDueType(scope.row)">
              {{ formatDate(scope.row.due_date) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="Priority" width="100">
          <template #default="scope">
            <el-tag :type="getPriorityType(scope.row.priority)">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editTask(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteTask(scope.row.task_id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加/编辑任务对话框 -->
    <el-dialog
      v-model="showAddTaskDialog"
      :title="isEditMode ? 'Edit Task' : 'New Task'"
      width="600px"
    >
      <el-form :model="taskForm" :rules="taskRules" ref="taskFormRef">
        <el-form-item label="Task Title" prop="title">
          <el-input v-model="taskForm.title" placeholder="Enter task title" />
        </el-form-item>
        <el-form-item label="Task Type" prop="category">
          <el-radio-group v-model="taskForm.category">
            <el-radio value="task">Task</el-radio>
            <el-radio value="note">Note</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="taskForm.description" type="textarea" placeholder="Enter task description" :rows="3" />
        </el-form-item>
        <el-form-item label="Project" prop="project_id">
          <el-select 
            v-model="taskForm.project_id" 
            placeholder="Select project" 
            clearable
            filterable
            :loading="isLoadingProjects"
            @focus="handleProjectSelectFocus"
          >
            <!-- 添加空选项，解决第一个选项无法选择的问题 -->
            <el-option :value="null" :label="'Select a project'" disabled />
            <!-- 确保每个项目都有唯一的key和正确格式化的值 -->
            <el-option 
              v-for="(project, index) in projects" 
              :key="`project-${project?.project_id || 'invalid'}-${index}`" 
              :label="project?.name || 'Unnamed Project'" 
              :value="String(project?.project_id || '')" 
              v-if="project?.project_id && project?.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Start Date" prop="start_date">
          <el-date-picker
            v-model="taskForm.start_date"
            type="datetime"
            placeholder="Select start date and time"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Due Date" prop="due_date">
          <el-date-picker
            v-model="taskForm.due_date"
            type="datetime"
            placeholder="Select due date and time"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Priority" prop="priority">
          <el-select v-model="taskForm.priority" placeholder="Select priority">
            <el-option label="None" value="none" />
            <el-option label="Low" value="low" />
            <el-option label="Medium" value="medium" />
            <el-option label="High" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item label="Recurrence Settings" prop="has_recurrence">
          <el-switch v-model="taskForm.has_recurrence" />
        </el-form-item>
        <template v-if="taskForm.has_recurrence">
          <el-form-item label="Recurrence Type" prop="recurrence_category">
            <el-select v-model="taskForm.recurrence_category" placeholder="Select recurrence type">
              <el-option label="Weekly" value="weekly" />
              <el-option label="Monthly" value="monthly" />
              <el-option label="Yearly" value="yearly" />
              <el-option label="Every N days" value="days" />
              <el-option label="Every N weeks" value="weeks" />
              <el-option label="Ebbinghaus" value="ebinghaus" />
            </el-select>
          </el-form-item>
          <el-form-item label="Recurrence Count" prop="recurrence_count">
          <el-input v-model.number="taskForm.recurrence_count" type="number" min="1" placeholder="Set recurrence count" />
        </el-form-item>
      </template>
      <el-form-item label="Tags">
          <el-select v-model="taskForm.selectedTags" multiple placeholder="Select tags">
          <el-option v-for="tag in tags" :key="tag.tagId" :label="tag.name" :value="tag.tagId" />
        </el-select>
      </el-form-item>
      </el-form>
      <template #footer>
          <el-button @click="showAddTaskDialog = false">Cancel</el-button>
          <el-button type="primary" @click="submitTaskForm">Confirm</el-button>
        </template>
    </el-dialog>
    <!-- Tags Management Dialog -->
    <el-dialog
      v-model="showTagsDialog"
      title="Tags Management"
      width="500px"
    >
      <tag-management 
        :tags="tags" 
        :tasks="tasks" 
        @tags-updated="handleTagsUpdated"
      />
    </el-dialog>
  </div>
</template>

<script>
import TagManagement from './components/TagManagement.vue'
import { reminderTaskAPI, tagAPI, taskTagAPI, reminderProjectAPI } from '../../api/reminder';
export default {
  name: 'Tasks',
  components: {
    TagManagement
  },
  created() {
    this.init()
  },
  data() {
    return {
      tasks: [{
        task_id: 3,
        user_id: 1,
        project_id: null,
        project_name: 'No Project',
        title: 'Weekly Fitness Record',
        description: 'Record weekly fitness activities',
        category: 'note',
        status: 'todo',
        is_archived: false,
        parent_task_id: null,
        level: 0,
        due_date: null,
        start_date: null,
        completed_at: null,
        created_at: '2023-11-01T10:00:00',
        priority: 'low',
        tags: [],
        recurrence_info: {
          category: 'weekly',
          schedule: [1, 3, 5],
          count: 52
        }
      }],
      projects: [],
      isLoadingProjects: false,
      lastLoadTime: 0,
      tags: [],
      filterOptions: {
        status: '',
        project: '',
        priority: '',
        dateRange: null,
        tag: ''
      },
      showAddTaskDialog: false,
      isEditMode: false,
      showTagsDialog: false,
      taskForm: {
        title: '',
        description: '',
        category: 'task',
        project_id: null,
        start_date: null,
        due_date: null,
        priority: 'none',
        has_recurrence: false,
        recurrence_category: 'weekly',
        recurrence_count: 1,
        selectedTags: []
      },
      taskRules: {
        title: [
          { required: true, message: 'Please enter task title', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    filteredTasks() {
      let filtered = [...this.tasks]
      
      // 状态筛选
      if (this.filterOptions.status) {
        filtered = filtered.filter(task => task.status === this.filterOptions.status)
      }
      
      // 项目筛选
      if (this.filterOptions.project) {
        filtered = filtered.filter(task => task && task.project_id === this.filterOptions.project)
      }
      
      // 优先级筛选
      if (this.filterOptions.priority) {
        filtered = filtered.filter(task => task.priority === this.filterOptions.priority)
      }
      
      // 日期范围筛选
      if (this.filterOptions.dateRange && this.filterOptions.dateRange.length === 2) {
        const [start, end] = this.filterOptions.dateRange
        filtered = filtered.filter(task => {
          if (!task.due_date) return false
          const dueDate = new Date(task.due_date)
          return dueDate >= start && dueDate <= end
        })
      }
      
      // 标签筛选
      if (this.filterOptions.tag) {
        filtered = filtered.filter(task => 
          task.tags && task.tags.some(tag => tag.tagId === this.filterOptions.tag)
        )
      }
      
      return filtered
    }
  },
  methods: {
    // 添加毛玻璃提示方法
    createGlassToast(type, message) {
      const toast = document.createElement('div');
      toast.className = `glass-toast glass-toast-${type}`;
      toast.textContent = message;
      document.body.appendChild(toast);
      
      // 自动移除
      setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
      }, 4000);
      
      setTimeout(() => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      }, 4500);
      
      toast.addEventListener('click', () => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      });
    },
    
    // 重新设计的项目加载逻辑
    async loadProjects() {
      // 重置项目列表
      this.projects = [];
      
      try {
        // 防止重复请求
        if (this.isLoadingProjects) return;
        this.isLoadingProjects = true;
        
        // 添加请求超时处理
        const timeoutPromise = new Promise((_, reject) => {
          setTimeout(() => reject(new Error('Request timeout')), 10000);
        });
        
        const response = await Promise.race([
          reminderProjectAPI.getAllProjects(),
          timeoutPromise
        ]);
        
        // 验证响应格式
        if (!response || typeof response !== 'object') {
          throw new Error('Invalid API response');
        }
        
        // 检查响应码
        if (response.code === 200 && Array.isArray(response.data)) {
          // 预处理项目数据，确保格式正确
          this.projects = response.data.map(project => ({
            ...project,
            // 确保project_id存在且为字符串类型
            project_id: String(project?.project_id || ''),
            name: project?.name || 'Unnamed Project'
          })).filter(project => project && project.project_id && project.name); // 过滤无效项目
          
          // 存储到本地缓存，提高下次加载速度
          try {
            localStorage.setItem('cachedProjects', JSON.stringify({
              data: this.projects,
              timestamp: Date.now()
            }));
          } catch (e) {
            // 忽略localStorage错误
          }
          
        } else {
          throw new Error(`API error: ${response.code || 'Unknown'}`);
        }
      } catch (error) {
        // 详细的错误处理
        const errorMessage = error.response?.status === 403 
          ? 'Authentication required. Please log in.' 
          : error.response?.status === 404 
            ? 'Projects endpoint not found.'
            : error.message === 'Request timeout'
              ? 'Request timed out. Please try again.'
              : 'Failed to load projects. Please try again.';
        
        // 尝试使用缓存数据
        const cacheLoaded = this.loadProjectsFromCache();
        
        // 根据错误类型和缓存状态显示不同的提示
        if (error.response?.status === 403) {
          // 403错误时，尝试刷新令牌或重定向到登录
          this.createGlassToast('error', errorMessage);
          // 可选：在实际应用中可以在这里实现令牌刷新逻辑
        } else if (cacheLoaded) {
          this.createGlassToast('warning', 'Using cached project data. ' + errorMessage);
        } else {
          this.createGlassToast('error', errorMessage);
        }
      } finally {
        this.isLoadingProjects = false;
      }
    },
    
    // 处理项目选择器聚焦事件
    handleProjectSelectFocus() {
      // 仅在数据为空或加载完成后尝试重新加载
      if (!this.isLoadingProjects && (this.projects.length === 0 || Date.now() - this.lastLoadTime > 60000)) {
        this.loadProjects();
        this.lastLoadTime = Date.now();
      }
    },
    
    // 从缓存加载项目数据
    loadProjectsFromCache() {
      try {
        const cachedData = localStorage.getItem('cachedProjects');
        if (cachedData) {
          const { data, timestamp } = JSON.parse(cachedData);
          const now = Date.now();
          const cacheExpiry = 30 * 60 * 1000; // 30分钟缓存
          
          if (now - timestamp < cacheExpiry && Array.isArray(data)) {
            this.projects = data;
            return true;
          }
        }
      } catch (e) {
        // 忽略解析错误
      }
      return false;
    }
  },
  
  methods: {
    async init() {
      // 并行加载标签、任务和项目数据
      await Promise.all([
        this.loadTags(),
        this.loadTasks(),
        this.loadProjects()
      ]);
    },
    
    createGlassToast(type, message) {
      const toast = document.createElement('div');
      toast.className = `glass-toast glass-toast-${type}`;
      toast.textContent = message;
      
      document.body.appendChild(toast);
      
      // 添加动画
      setTimeout(() => {
        toast.style.opacity = '1';
        toast.style.transform = 'translateX(0)';
      }, 10);
      
      // 自动移除
      setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
      }, 4000);
      
      setTimeout(() => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      }, 4500);
      
      toast.addEventListener('click', () => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      });
    },
    
    async loadTags() {
      try {
        const response = await tagAPI.getAllTags();
        // 根据Projects.vue的判断逻辑，检查response.code === 200
        if (response && response.code === 200) {
          this.tags = response.data || [];
        } else {
          console.warn('API returned non-success response:', response);
          this.tags = [];
          this.createGlassToast('error', 'Failed to load tags');
        }
      } catch (error) {
        console.error('Failed to load tags:', error);
        this.createGlassToast('error', 'Failed to load tags');
      }
    },
    
    async loadTasks() {
      try {
        const response = await reminderTaskAPI.getTasks();
        // 根据Projects.vue的判断逻辑，检查response.code === 200
        if (response && response.code === 200) {
          this.tasks = response.data || [];
          console.log('Tasks loaded successfully:', this.tasks);
        } else {
          console.warn('API returned non-success response:', response);
          this.tasks = [];
          this.createGlassToast('error', 'Failed to load tasks');
        }
      } catch (error) {
        console.error('Failed to load tasks data:', error);
        this.createGlassToast('error', 'Failed to load tasks');
      }
    },
    
    handleTagsUpdated(updatedTags) {
      // 更新本地标签数据
      this.tags = updatedTags;
    },
    
    getTagType(color) {
      // 根据颜色返回对应的Element Plus标签类型
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#e6a23c': 'warning',
        '#f56c6c': 'danger',
        '#909399': 'info'
      };
      return colorMap[color] || 'info';
    },
    
    formatDate(dateString) {
      if (!dateString) return 'None';
      const date = new Date(dateString);
      return date.toLocaleString('en-US');
    },
    getPriorityType(priority) {
      const typeMap = {
        'none': 'info',
        'low': 'success',
        'medium': 'warning',
        'high': 'danger'
      };
      return typeMap[priority] || 'info';
    },
    getPriorityText(priority) {
      const textMap = {
        'none': 'None',
        'low': 'Low',
        'medium': 'Medium',
        'high': 'High'
      };
      return textMap[priority] || 'None';
    },
    getTaskDueType(task) {
      if (!task.due_date) return 'info'
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      const tomorrow = new Date(today)
      tomorrow.setDate(tomorrow.getDate() + 1)
      const dueDate = new Date(task.due_date)
      
      if (task.status === 'done') return 'success'
      if (dueDate < today) return 'danger'
      if (dueDate < tomorrow) return 'warning'
      return 'info'
    },
    getRecurrenceText(recurrenceInfo) {
      if (!recurrenceInfo) return '';
      
      const { category, schedule, count } = recurrenceInfo;
      
      switch (category) {
        case 'weekly':
          const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
          const dayTexts = schedule.map(day => days[day]).join(', ');
          return `Every ${dayTexts}, ${count} times`;
        case 'monthly':
          return `Monthly on ${schedule.join(', ')}, ${count} times`;
        case 'yearly':
          return `Yearly on ${schedule[0]}, ${count} times`;
        case 'days':
          return `Every ${schedule || 1} days, ${count} times`;
        case 'weeks':
          return `Every ${schedule || 1} weeks, ${count} times`;
        case 'ebinghaus':
          return `Ebbinghaus Memory Curve, ${count} times`;
        default:
          return 'Custom recurrence';
      }
    },

    updateTaskStatus(task) {
      const taskIndex = this.tasks.findIndex(t => t.task_id === task.task_id)
      if (taskIndex !== -1) {
        if (task.status === 'done') {
          this.tasks[taskIndex].status = 'done'
          this.tasks[taskIndex].completed_at = new Date().toISOString()
        } else {
          this.tasks[taskIndex].status = 'todo'
          this.tasks[taskIndex].completed_at = null
        }
        this.createGlassToast('success', 'Task status updated successfully')
      }
    },
    editTask(task) {
      this.isEditMode = true
      this.taskForm = {
        task_id: task.task_id,
        title: task.title,
        description: task.description || '',
        category: task.category,
        project_id: task.project_id,
        start_date: task.start_date ? new Date(task.start_date) : null,
        due_date: task.due_date ? new Date(task.due_date) : null,
        priority: task.priority,
        has_recurrence: !!task.recurrence_info,
        recurrence_category: task.recurrence_info?.category || 'weekly',
        recurrence_count: task.recurrence_info?.count || 1,
        selectedTags: task.tags ? task.tags.map(tag => tag.tagId) : []
      }
      this.showAddTaskDialog = true
    },
    deleteTask(taskId) {
      this.$confirm('Are you sure you want to delete this task?', 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const index = this.tasks.findIndex(t => t.task_id === taskId);
        if (index !== -1) {
          this.tasks.splice(index, 1);
          this.createGlassToast('success', 'Task deleted successfully');
        }
      }).catch(() => {
        // 取消删除
      });
    },
    async submitTaskForm() {
      this.$refs.taskFormRef.validate(async (valid) => {
        if (valid) {
          try {
            // 根据后端CreateTaskRequest结构构建任务数据
            const taskData = {
              title: this.taskForm.title,
              description: this.taskForm.description,
              category: this.taskForm.category,
              projectId: this.taskForm.project_id || null,
              parentTaskId: null, // 暂时不支持子任务
              startDate: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
              dueDate: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
              reminderSentAt: null,
              priority: this.taskForm.priority
            }
            
            // 处理重复设置
            if (this.taskForm.has_recurrence) {
              taskData.has_recurrence = true
              taskData.recurrence_category = this.taskForm.recurrence_category
              taskData.recurrence_count = this.taskForm.recurrence_count
            }
            
            let savedTask
            
            if (this.isEditMode) {
              // 编辑现有任务
              taskData.taskId = this.taskForm.task_id
              // 注意：reminderTaskAPI中可能没有updateTask方法，暂时使用createTask
              const response = await reminderTaskAPI.createTask(taskData)
              
              // 根据Projects.vue的判断逻辑，检查response.code === 200
              if (response && response.code === 200) {
                savedTask = response.data || {};
                
                // 更新标签关联：先删除现有关联，再创建新关联
                await taskTagAPI.deleteTaskTagsByTaskId(this.taskForm.task_id)
                if (this.taskForm.selectedTags.length > 0) {
                  await taskTagAPI.createTaskTagBatch({
                    taskId: this.taskForm.task_id,
                    tagIds: this.taskForm.selectedTags
                  })
                }
              } else {
                this.createGlassToast('error', 'Failed to update task');
                throw new Error('Failed to update task')
              }
              
              // 更新本地任务列表
              const index = this.tasks.findIndex(t => t.task_id === savedTask.task_id)
              if (index !== -1) {
                this.tasks[index] = {
                  ...this.tasks[index],
                  ...savedTask,
                  tags: this.taskForm.selectedTags.map(tagId => 
                    this.tags.find(tag => tag.tagId === tagId)
                  ).filter(Boolean)
                }
              }
              
              this.createGlassToast('success', 'Task updated successfully')
            } else {
              // 创建新任务
              const response = await reminderTaskAPI.createTask(taskData)
              
              // 根据Projects.vue的判断逻辑，检查response.code === 200
              if (response && response.code === 200) {
                savedTask = response.data || {};
                
                // 创建标签关联
              if (this.taskForm.selectedTags.length > 0) {
                await taskTagAPI.createTaskTagBatch({
                  taskId: savedTask.taskId,
                  tagIds: this.taskForm.selectedTags
                });
              }
            } else {
              this.createGlassToast('error', 'Failed to create task');
              throw new Error('Failed to create task');
            }
            
            // 添加到本地任务列表
            this.tasks.push({
              task_id: savedTask.taskId, // 保持前端使用task_id的习惯
              user_id: savedTask.userId,
              project_id: savedTask.projectId,
              project_name: savedTask.projectName || 'No Project',
              title: savedTask.title,
              description: savedTask.description,
              category: savedTask.category,
              status: 'todo', // 默认状态
              is_archived: false,
              parent_task_id: savedTask.parentTaskId,
              level: 0,
              due_date: savedTask.dueDate,
              start_date: savedTask.startDate,
              completed_at: null,
              created_at: new Date().toISOString(),
              priority: savedTask.priority,
              tags: this.taskForm.selectedTags.map(tagId => 
                this.tags.find(tag => tag.tagId === tagId)
              ).filter(Boolean)
            })
              
              this.createGlassToast('success', 'Task created successfully')
            }
            
            // 关闭对话框并重置表单
            this.showAddTaskDialog = false;
            this.resetTaskForm();
          } catch (error) {
            console.error('Failed to save task:', error);
            this.createGlassToast('error', 'Failed to save task: ' + (error.response?.data?.message || error.message));
          }
        }
      });
    },
    resetTaskForm() {
      this.taskForm = {
        title: '',
        description: '',
        category: 'task',
        project_id: null,
        start_date: null,
        due_date: null,
        priority: 'none',
        has_recurrence: false,
        recurrence_category: 'weekly',
        recurrence_count: 1,
        selectedTags: []
      };
      this.isEditMode = false;
      if (this.$refs.taskFormRef) {
        this.$refs.taskFormRef.resetFields();
      }
    }

  }
}
</script>

<style>
@import '../../assets/styles/page/tasks.css';
</style>