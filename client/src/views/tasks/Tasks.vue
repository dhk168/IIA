<template>
  <div class="tasks-container">
    <div class="page-header">
      <div class="header-actions">
        <LightButton type="primary" @click="showAddTaskDialog = true">
          New Task
        </LightButton>
        <LightButton @click="showTagsDialog = true">
          Tags Management
        </LightButton>
      </div>
    </div>
    
    <!-- Task Filter -->
    <TaskFilter v-model:filter="filterOptions" :tags="tags" :projects="projects" />
    
    <!-- Task List -->
    <TaskList
      :filtered-tasks="filteredTasks"
      :tags="tags"
      :projects="projects"
      @edit-task="editTask"
      @delete-task="deleteTask"
      @update-task-status="updateTaskStatus"
    />
    
    <!-- Add/Edit Task Dialog -->
    <TaskDialog
      v-model="showAddTaskDialog"
      :is-edit-mode="isEditMode"
      :task="taskToEdit"
      :projects="sortedProjects"
      :tags="tags"
      @save="handleTaskSave"
    />

    <!-- Tags Management Dialog -->
    <tag-management 
      v-model="showTagsDialog"
      :tags="tags" 
      :tasks="tasks" 
      @tags-updated="handleTagsUpdated"
      @close="showTagsDialog = false"
    />
  </div>
</template>

<script>
import TagManagement from './components/TagManagement.vue'
import LightButton from '@/components/LightButton.vue'
import LightSelect from '@/components/LightSelect.vue'
import TaskDialog from './components/TaskDialog.vue'
import TaskFilter from './components/TaskFilter.vue'
import TaskList from './components/TaskList.vue'
import { reminderTaskAPI, tagAPI, taskTagAPI, reminderProjectAPI } from '@/api/reminder';
export default {
  name: 'Tasks',
  inject: ['showToast'],
  components: {
    TagManagement,
    LightButton,
    LightSelect,
    TaskDialog,
    TaskFilter,
    TaskList
  },
  created() {
    this.init()
  },
  data() {
    return {
      tasks: [],
      tags: [],
      projects: [],
      filterOptions: {
        status: '',
        priority: '',
        dateRange: null,
        tag: '',
        project: ''
      },
      showAddTaskDialog: false,
      isEditMode: false,
      showTagsDialog: false,
      taskToEdit: {},
      taskForm: {
        title: '',
        description: '',
        category: 'task',
        start_date: null,
        due_date: null,
        priority: 'none',
        has_recurrence: false,
        recurrence_category: 'weekly',
        recurrence_count: 1,
        selectedTags: [],
        project_id: null
      },
      taskRules: {
        title: [
          { required: true, message: 'Please enter task title', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 在组件创建时初始化数据
    this.init().then(() => {
    });
  },
  computed: {

    filteredTasks() {
      let filtered = [...this.tasks]
      
      // 状态筛选
      if (this.filterOptions.status) {
        filtered = filtered.filter(task => task.status === this.filterOptions.status)
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
      
      // 项目筛选
      if (this.filterOptions.project) {
        filtered = filtered.filter(task => task && task.project_id === this.filterOptions.project)
      }
      
      return filtered
    },
    sortedProjects() {
      return [...this.projects].sort((a, b) => {
        const nameA = (a?.name || '').toLowerCase();
        const nameB = (b?.name || '').toLowerCase();
        return nameA.localeCompare(nameB);
      });
    }
  },
  methods: {
    async loadProjects() {
      try {
        const response = await reminderProjectAPI.getAllProjects();
        if (response && response.code === 200 && Array.isArray(response.data)) {
          this.projects = response.data.filter(project => !project.isArchived);
        } else {
          this.projects = [];
        }
      } catch (error) {
        console.error('Failed to load projects:', error);
        this.projects = [];
      }
    },
    getProjectName(projectId) {
      if (!projectId) return 'No Project';
      const project = this.projects.find(p => p.projectId === projectId);
      return project ? project.name : 'Unknown Project';
    },
    // 初始化数据加载
    async init() {
      // 加载标签、任务和项目数据
      await Promise.all([
        this.loadTags(),
        this.loadTasks(),
        this.loadProjects()
      ]);
    },
    

    
    
    async loadTags() {
      try {
        const response = await tagAPI.getAllTags();
        // 响应拦截器已经返回了response.data，直接检查code
        if (response && response.code === 200 && Array.isArray(response.data)) {
          this.tags = response.data;
        } else {
          console.warn('API returned non-success response or invalid data:', response);
          this.tags = [];
          this.showToast('error', 'Failed to load tags');
        }
      } catch (error) {
        console.error('Failed to load tags:', error);
        this.tags = [];
        this.showToast('error', 'Failed to load tags');
      }
    },
    
    async loadTasks() {
      try {
        const response = await reminderTaskAPI.getTasks();
        if (response && response.code === 200) {
          this.tasks = (response.data || []).map(task => ({
            task_id: task.taskId,
            user_id: task.userId,
            project_id: task.projectId,
            project_name: task.projectName || 'No Project',
            title: task.title,
            description: task.description,
            category: task.category,
            status: task.status || 'todo',
            is_archived: task.isArchived || false,
            parent_task_id: task.parentTaskId,
            level: 0,
            due_date: task.dueDate,
            start_date: task.startDate,
            completed_at: task.completedAt,
            created_at: task.createdAt,
            priority: task.priority || 'none',
            tags: task.tags || [],
            recurrence_info: task.recurrenceInfo,
            // 保留原始字段
            ...task
          }));
          console.log('Tasks loaded successfully:', this.tasks);
          // this.showToast('success', `Successfully loaded ${this.tasks.length} tasks`);
        } else {
          console.warn('API returned non-success response:', response);
          this.tasks = [];
          this.showToast('error', 'Failed to load tasks');
        }
      } catch (error) {
        console.error('Failed to load tasks data:', error);
        this.showToast('error', 'Failed to load tasks');
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
        this.showToast('success', 'Task status updated successfully')
      }
    },
    editTask(task) {
      this.isEditMode = true;
      this.taskToEdit = task;
      this.showAddTaskDialog = true;
    },
    async handleTaskSave(taskData) {
      try {
        let savedTask;
        // 创建/编辑任务请求
        if (this.isEditMode) {
          // 编辑任务
          taskData.taskId = this.taskToEdit.task_id;
          const response = await reminderTaskAPI.createTask(taskData);
          if (response?.code === 200) {
            savedTask = response.data || {};
            await taskTagAPI.deleteTaskTagsByTaskId(this.taskToEdit.task_id);
            if (taskData.tags?.length > 0) {
              await taskTagAPI.createTaskTagBatch({ taskId: savedTask.taskId, tagIds: taskData.tags });
            }
            // 更新本地列表
            const index = this.tasks.findIndex(t => t.task_id === savedTask.task_id);
            if (index !== -1) {
              this.tasks[index] = { ...this.tasks[index], ...savedTask, tags: taskData.tags.map(tagId => this.tags.find(tag => tag.tagId === tagId)).filter(Boolean) };
            }
            this.showToast('success', 'Task updated successfully');
          } else {
            throw new Error('Failed to update task');
          }
        } else {
          // 创建任务
          const response = await reminderTaskAPI.createTask(taskData);
          if (response?.code === 200) {
            savedTask = response.data || {};
            if (taskData.tags?.length > 0) {
              await taskTagAPI.createTaskTagBatch({ taskId: savedTask.taskId, tagIds: taskData.tags });
            }
            // 添加到本地列表
            this.tasks.push({ ...savedTask, task_id: savedTask.taskId, status: 'todo', created_at: new Date().toISOString(), tags: taskData.tags.map(tagId => this.tags.find(tag => tag.tagId === tagId)).filter(Boolean) });
            this.showToast('success', 'Task created successfully');
          } else {
            throw new Error('Failed to create task');
          }
        }
        // 关闭弹窗并重置状态
        this.showAddTaskDialog = false;
        this.isEditMode = false;
        this.taskToEdit = {};
      } catch (error) {
        console.error('Task save error:', error);
        this.showToast('error', error.message || 'Failed to save task');
      }
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
          this.showToast('success', 'Task deleted successfully');
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
                this.showToast('error', 'Failed to update task');
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
              
              this.showToast('success', 'Task updated successfully')
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
                this.showToast('error', 'Failed to create task');
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
              
              this.showToast('success', 'Task created successfully')
            }
            
            // 关闭对话框并重置表单
            this.showAddTaskDialog = false;
            this.resetTaskForm();
          } catch (error) {
            console.error('Failed to save task:', error);
            this.showToast('error', 'Failed to save task: ' + (error.response?.data?.message || error.message));
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
@import '@/assets/styles/page/tasks.css';
</style>