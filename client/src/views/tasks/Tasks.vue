<template>
  <div class="tasks-container">
    <div class="page-header">
      <div class="header-actions">
        <el-button type="primary" @click="showAddTaskDialog = true">
          <el-icon><Plus /></el-icon> New Task
        </el-button>
        <el-button @click="showTagsDialog = true">
          <el-icon><Management /></el-icon> Tags Management
        </el-button>
      </div>
    </div>
    
    <!-- 任务筛选 -->
    <div class="filter-section">
      <el-row :gutter="20">
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.status" placeholder="Task Status" clearable>
            <el-option label="All" value="" />
            <el-option label="To Do" value="todo" />
            <el-option label="Completed" value="done" />
            <el-option label="Abandoned" value="abandoned" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.project" placeholder="Project" clearable>
            <el-option label="All" value="" />
            <el-option v-for="project in projects" :key="project.project_id" :label="project.name" :value="project.project_id" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.priority" placeholder="Priority" clearable>
            <el-option label="All" value="" />
            <el-option label="None" value="none" />
            <el-option label="Low" value="low" />
            <el-option label="Medium" value="medium" />
            <el-option label="High" value="high" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-date-picker v-model="filterOptions.dateRange" type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date" />
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.tag" placeholder="Tags Filter" clearable>
            <el-option label="All" value="" />
            <el-option v-for="tag in tags" :key="tag.tag_id" :label="tag.name" :value="tag.tag_id" />
          </el-select>
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
                <el-tag v-for="tag in scope.row.tags" :key="tag.tag_id" :type="getTagType(tag.color)">
                  {{ tag.name }}
                </el-tag>
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
          <el-select v-model="taskForm.project_id" placeholder="Select project" clearable>
            <el-option v-for="project in projects" :key="project.project_id" :label="project.name" :value="project.project_id" />
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
          <el-option v-for="tag in tags" :key="tag.tag_id" :label="tag.name" :value="tag.tag_id" />
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
      <div class="tags-management">
        <el-card class="tags-card">
          <template #header>
            <div class="tags-card-header">
              <span>Tags List</span>
              <el-button type="primary" size="small" @click="showAddTagDialog = true">
                <el-icon><Plus /></el-icon> New Tag
              </el-button>
            </div>
          </template>
          <el-table :data="tags" stripe style="width: 100%">
            <el-table-column prop="name" label="Tag Name" />
            <el-table-column prop="color" label="Color" width="120">
              <template #default="scope">
                <el-tag :type="getTagType(scope.row.color)">
                  {{ scope.row.color }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="scope">
                <el-button size="small" @click="editTag(scope.row)">Edit</el-button>
                <el-button size="small" type="danger" @click="deleteTag(scope.row.tag_id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </el-dialog>
    
    <!-- Add/Edit Tag Dialog -->
    <el-dialog
      v-model="showAddTagDialog"
      :title="isEditingTag ? 'Edit Tag' : 'New Tag'"
      width="400px"
    >
      <el-form :model="tagForm" :rules="tagRules" ref="tagFormRef">
        <el-form-item label="Tag Name" prop="name">
          <el-input v-model="tagForm.name" placeholder="Enter tag name" />
        </el-form-item>
        <el-form-item label="Tag Color" prop="color">
          <el-color-picker v-model="tagForm.color" show-alpha show-panel-color />
        </el-form-item>
      </el-form>
      <template #footer>
          <el-button @click="showAddTagDialog = false">Cancel</el-button>
          <el-button type="primary" @click="submitTagForm">Confirm</el-button>
        </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Management } from '@element-plus/icons-vue'

export default {
  name: 'Tasks',
  components: {
    Plus,
    Management
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
      showAddTagDialog: false,
      isEditingTag: false,
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
      },
      tagForm: {
        name: '',
        color: '#409eff'
      },
      tagRules: {
        name: [
          { required: true, message: 'Please enter tag name', trigger: 'blur' },
          { min: 1, max: 10, message: 'Tag name length should be between 1 and 10 characters', trigger: 'blur' }
        ],
        color: [
          { required: true, message: 'Please select tag color', trigger: 'change' }
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
        filtered = filtered.filter(task => task.project_id === this.filterOptions.project)
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
          task.tags && task.tags.some(tag => tag.tag_id === this.filterOptions.tag)
        )
      }
      
      return filtered
    }
  },
  methods: {
    async init() {
      await this.loadTasks();
    },
    async loadTasks() {
      try {
        // TODO: 实现真实的任务数据加载API调用
        console.log('Loading tasks data from API');
        // 这里将在API实现后替换为实际调用
      } catch (error) {
        console.error('Failed to load tasks data:', error);
      }
    },
    formatDate(dateString) {
        if (!dateString) return 'None'
        const date = new Date(dateString)
        return date.toLocaleString('en-US')
      },
    getPriorityType(priority) {
      const typeMap = {
        'none': 'info',
        'low': 'success',
        'medium': 'warning',
        'high': 'danger'
      }
      return typeMap[priority] || 'info'
    },
    getPriorityText(priority) {
        const textMap = {
          'none': 'None',
          'low': 'Low',
          'medium': 'Medium',
          'high': 'High'
        }
        return textMap[priority] || 'None'
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
      if (!recurrenceInfo) return ''
      
      const { category, schedule, count } = recurrenceInfo
      
      switch (category) {
        case 'weekly':
          const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
          const dayTexts = schedule.map(day => days[day]).join(', ')
          return `Every ${dayTexts}, ${count} times`
        case 'monthly':
          return `Monthly on ${schedule.join(', ')}, ${count} times`
        case 'yearly':
          return `Yearly on ${schedule[0]}, ${count} times`
        case 'days':
          return `Every ${schedule || 1} days, ${count} times`
        case 'weeks':
          return `Every ${schedule || 1} weeks, ${count} times`
        case 'ebinghaus':
          return `Ebbinghaus Memory Curve, ${count} times`
        default:
          return 'Custom recurrence'
      }
    },
    getTagType(color) {
      // 根据颜色返回对应的Element Plus标签类型
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#e6a23c': 'warning',
        '#f56c6c': 'danger',
        '#909399': 'info'
      }
      return colorMap[color] || 'info'
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
        this.$message.success('Task status updated successfully')
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
        selectedTags: task.tags ? task.tags.map(tag => tag.tag_id) : []
      }
      this.showAddTaskDialog = true
    },
    deleteTask(taskId) {
        this.$confirm('Are you sure you want to delete this task?', 'Confirmation', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const index = this.tasks.findIndex(t => t.task_id === taskId)
        if (index !== -1) {
          this.tasks.splice(index, 1)
          this.$message.success('Task deleted successfully')
        }
      }).catch(() => {
        // 取消删除
      })
    },
    submitTaskForm() {
      this.$refs.taskFormRef.validate((valid) => {
        if (valid) {
          if (this.isEditMode) {
            // 编辑现有任务
            const index = this.tasks.findIndex(t => t.task_id === this.taskForm.task_id)
            if (index !== -1) {
              const updatedTask = {
                ...this.tasks[index],
                title: this.taskForm.title,
                description: this.taskForm.description,
                category: this.taskForm.category,
                project_id: this.taskForm.project_id,
                project_name: this.taskForm.project_id ? this.projects.find(p => p.project_id === this.taskForm.project_id)?.name : 'No Project',
                start_date: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
                due_date: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
                priority: this.taskForm.priority,
                tags: this.taskForm.selectedTags.map(tagId => 
                  this.tags.find(tag => tag.tag_id === tagId)
                ).filter(Boolean)
              }
              
              if (this.taskForm.has_recurrence) {
                updatedTask.recurrence_info = {
                  category: this.taskForm.recurrence_category,
                  count: this.taskForm.recurrence_count
                  // schedule会根据category在后端生成
                }
              } else {
                updatedTask.recurrence_info = null
              }
              
              this.tasks[index] = updatedTask
              this.$message.success('Task updated successfully')
            }
          } else {
            // 创建新任务
            const newTask = {
              task_id: Date.now(),
              user_id: 1, // 模拟用户ID
              project_id: this.taskForm.project_id,
              project_name: this.taskForm.project_id ? this.projects.find(p => p.project_id === this.taskForm.project_id)?.name : 'No Project',
              title: this.taskForm.title,
              description: this.taskForm.description,
              category: this.taskForm.category,
              status: 'todo',
              is_archived: false,
              parent_task_id: null,
              level: 0,
              due_date: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
              start_date: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
              completed_at: null,
              created_at: new Date().toISOString(),
              priority: this.taskForm.priority,
              tags: this.taskForm.selectedTags.map(tagId => 
                this.tags.find(tag => tag.tag_id === tagId)
              ).filter(Boolean),
              recurrence_info: null
            }
            
            if (this.taskForm.has_recurrence) {
              newTask.recurrence_info = {
                category: this.taskForm.recurrence_category,
                count: this.taskForm.recurrence_count
              }
            }
            
            this.tasks.push(newTask)
            this.$message.success('Task created successfully')
          }
          
          // 关闭对话框并重置表单
          this.showAddTaskDialog = false
          this.resetTaskForm()
        }
      })
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
      }
      this.isEditMode = false
      if (this.$refs.taskFormRef) {
        this.$refs.taskFormRef.resetFields()
      }
    },
    resetTagForm() {
      this.tagForm = {
        name: '',
        color: '#409eff'
      }
      this.isEditingTag = false
      if (this.$refs.tagFormRef) {
        this.$refs.tagFormRef.resetFields()
      }
    },
    editTag(tag) {
      this.isEditingTag = true
      this.tagForm = {
        tag_id: tag.tag_id,
        name: tag.name,
        color: tag.color
      }
      this.showAddTagDialog = true
    },
    deleteTag(tagId) {
        // Check if any tasks use this tag
        const hasTasksWithTag = this.tasks.some(task => 
          task.tags && task.tags.some(tag => tag.tag_id === tagId)
        )
        
        if (hasTasksWithTag) {
          this.$message.warning('This tag is being used by tasks, cannot delete')
          return
        }
        
        this.$confirm('Are you sure you want to delete this tag?', 'Confirmation', {
        confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const index = this.tags.findIndex(t => t.tag_id === tagId)
        if (index !== -1) {
          this.tags.splice(index, 1)
          this.$message.success('Tag deleted successfully')
        }
      }).catch(() => {
        // 取消删除
      })
    },
    getTagType(color) {
      // 根据颜色值判断对应的el-tag类型
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#f56c6c': 'danger',
        '#e6a23c': 'warning',
        '#909399': 'info'
      }
      return colorMap[color] || 'info'
    },
    submitTagForm() {
      this.$refs.tagFormRef.validate((valid) => {
        if (valid) {
          if (this.isEditingTag) {
            // 编辑现有标签
            const index = this.tags.findIndex(t => t.tag_id === this.tagForm.tag_id)
            if (index !== -1) {
              this.tags[index] = {
                ...this.tags[index],
                name: this.tagForm.name,
                color: this.tagForm.color
              }
              // 更新使用此标签的任务
              this.tasks.forEach(task => {
                if (task.tags && task.tags.length > 0) {
                  task.tags.forEach(tag => {
                    if (tag.tag_id === this.tagForm.tag_id) {
                      tag.name = this.tagForm.name
                      tag.color = this.tagForm.color
                    }
                  })
                }
              })
              this.$message.success('Tag updated successfully')
            }
          } else {
            // 检查标签名称是否已存在
            const existingTag = this.tags.find(t => t.name === this.tagForm.name)
            if (existingTag) {
              this.$message.warning('Tag name already exists')
              return
            }
            
            // 创建新标签
            const newTag = {
              tag_id: Date.now(),
              name: this.tagForm.name,
              color: this.tagForm.color
            }
            
            this.tags.push(newTag)
            this.$message.success('Tag created successfully')
          }
          
          // 关闭对话框并重置表单
          this.showAddTagDialog = false
          this.resetTagForm()
        }
      })
    }
  }
}
</script>

<style scoped>
.tasks-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.filter-section {
  margin-bottom: 20px;
}

.tasks-card {
  border-radius: 8px;
}

.tags-card {
  margin-bottom: 20px;
}

.tags-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tags-management {
  padding: 10px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  color: #303133;
}

.task-title-container {
  display: flex;
  align-items: center;
}

.task-completed {
  text-decoration: line-through;
  color: #909399;
}

.task-detail {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.detail-item {
  margin-bottom: 8px;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-item strong {
  color: #606266;
  margin-right: 8px;
}

.detail-item p {
  margin: 5px 0 0 0;
  color: #303133;
}
</style>