<template>
  <div class="projects-container">
    <div class="page-header">
      <h1>Project Plan</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showAddProjectDialog = true">
          <el-icon><Plus /></el-icon> New Project
        </el-button>
      </div>
    </div>
    
    <!-- Project List -->
    <div class="projects-list">
      <div class="project-cards">
        <el-card 
          v-for="project in projects" 
          :key="project.id" 
          class="project-card"
          @click="selectProject(project)"
        >
          <template #header>
            <div class="project-header">
              <h3 class="project-title">{{ project.title }}</h3>
              <div class="project-actions">
                <el-dropdown trigger="click">
                  <el-button circle plain size="small">
                    <el-icon><Setting /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click.stop="editProject(project)">Edit</el-dropdown-item>
                  <el-dropdown-item @click.stop="deleteProject(project.id)" danger>Delete</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </template>
          
          <div class="project-content">
            <div class="project-meta">
                <div class="meta-item">
                  <el-icon class="meta-icon"><Document /></el-icon>
                  <span>{{ project.taskCount }} Tasks</span>
                </div>
                <div class="meta-item">
                  <el-icon class="meta-icon"><Calendar /></el-icon>
                  <span>{{ formatDate(project.dueDate) }}</span>
                </div>
              </div>
            
            <div class="project-progress">
                <div class="progress-info">
                  <span class="progress-text">Progress</span>
                  <span class="progress-percentage">{{ project.progress }}%</span>
                </div>
                <el-progress 
                  :percentage="project.progress" 
                  :color="getProgressColor(project.progress)"
                  :show-text="false"
                />
              </div>
            
            <div class="project-tags">
              <el-tag size="small" :type="project.priorityType">{{ project.priority }}</el-tag>
              <el-tag size="small" type="info">{{ project.category }}</el-tag>
            </div>
          </div>
        </el-card>
        
        <!-- Empty State -->
            <div v-if="projects.length === 0" class="empty-state">
              <el-empty description="No projects" />
              <el-button type="primary" style="margin-top: 20px" @click="showAddProjectDialog = true">
                <el-icon><Plus /></el-icon> Create First Project
              </el-button>
            </div>
      </div>
    </div>
    
    <!-- Project Details Modal -->
    <el-dialog
      v-model="showProjectDetails"
      :title="selectedProject ? selectedProject.title : 'Project Details'"
      width="800px"
    >
      <div v-if="selectedProject" class="project-details">
        <div class="details-section">
          <h3>Project Information</h3>
          <div class="detail-row">
            <span class="detail-label">Description:</span>
            <span class="detail-value">{{ selectedProject.description || 'No description' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Category:</span>
            <el-tag type="info">{{ selectedProject.category }}</el-tag>
          </div>
          <div class="detail-row">
            <span class="detail-label">Priority:</span>
            <el-tag :type="selectedProject.priorityType">{{ selectedProject.priority }}</el-tag>
          </div>
          <div class="detail-row">
            <span class="detail-label">Due Date:</span>
            <span>{{ formatDate(selectedProject.dueDate) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Created Date:</span>
            <span>{{ formatDate(selectedProject.createdAt) }}</span>
          </div>
        </div>
        
        <div class="details-section">
          <h3>Task List</h3>
          <el-list :data="selectedProject.tasks" class="task-list">
            <template #header>
              <div class="list-header">
                <span>Tasks ({{ selectedProject.tasks.length }})</span>
                <el-button size="small" type="primary" @click="showAddTaskDialog = true">
                  <el-icon><Plus /></el-icon> Add Task
                </el-button>
              </div>
            </template>
            <el-list-item v-for="task in selectedProject.tasks" :key="task.id" class="task-item">
              <template #default>
                <div class="task-content">
                  <el-checkbox v-model="task.completed" @change="updateTaskStatus(task)">
                    <span :class="{ 'task-completed': task.completed }">{{ task.title }}</span>
                  </el-checkbox>
                </div>
              </template>
              <template #extra>
                <div class="task-extra">
                  <el-tag v-if="task.dueDate" size="small" :type="getTaskDueType(task)">
                    {{ formatDate(task.dueDate) }}
                  </el-tag>
                </div>
              </template>
            </el-list-item>
          </el-list>
        </div>
      </div>
    </el-dialog>
    
    <!-- Add/Edit Project Modal -->
    <el-dialog
      v-model="showAddProjectDialog"
      :title="isEditMode ? 'Edit Project' : 'New Project'"
      width="600px"
    >
      <el-form :model="projectForm" :rules="projectRules" ref="projectFormRef">
        <el-form-item label="Project Name" prop="title">
          <el-input v-model="projectForm.title" placeholder="Enter project name" />
        </el-form-item>
        <el-form-item label="Project Description" prop="description">
          <el-input v-model="projectForm.description" type="textarea" placeholder="Enter project description" :rows="3" />
        </el-form-item>
        <el-form-item label="Category" prop="category">
          <el-select v-model="projectForm.category" placeholder="Select project category">
            <el-option label="Work" value="Work" />
            <el-option label="Learning" value="Learning" />
            <el-option label="Life" value="Life" />
            <el-option label="Health" value="Health" />
          </el-select>
        </el-form-item>
        <el-form-item label="Priority" prop="priority">
          <el-select v-model="projectForm.priority" placeholder="Select priority">
            <el-option label="High" value="High" />
            <el-option label="Medium" value="Medium" />
            <el-option label="Low" value="Low" />
          </el-select>
        </el-form-item>
        <el-form-item label="Due Date" prop="dueDate">
          <el-date-picker
            v-model="projectForm.dueDate"
            type="date"
            placeholder="Select date"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddProjectDialog = false">Cancel</el-button>
        <el-button type="primary" @click="submitProjectForm">Confirm</el-button>
      </template>
    </el-dialog>
    
    <!-- Add Task Modal -->
    <el-dialog
      v-model="showAddTaskDialog"
      title="Add Task"
      width="500px"
    >
      <el-form :model="taskForm" :rules="taskRules" ref="taskFormRef">
        <el-form-item label="Task Name" prop="title">
          <el-input v-model="taskForm.title" placeholder="Enter task name" />
        </el-form-item>
        <el-form-item label="Due Date" prop="dueDate">
          <el-date-picker
            v-model="taskForm.dueDate"
            type="date"
            placeholder="Select date"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTaskDialog = false">Cancel</el-button>
        <el-button type="primary" @click="submitTaskForm">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Setting, Document, Calendar } from '@element-plus/icons-vue'

export default {
  name: 'Projects',
  components: {
    Plus,
    Setting,
    Document,
    Calendar
  },
  data() {
    return {
      projects: [
        {
          id: 1,
          title: 'Website Development',
          description: 'Create company official website, including homepage, product display, about us, etc.',
          category: 'Work',
          priority: 'High',
          priorityType: 'danger',
          dueDate: '2023-11-30',
          createdAt: '2023-10-01',
          taskCount: 5,
          progress: 60,
          tasks: [
            { id: 101, title: 'Design Website Prototype', completed: true, dueDate: '2023-10-10' },
            { id: 102, title: 'Frontend Page Development', completed: true, dueDate: '2023-10-20' },
            { id: 103, title: 'Backend API Implementation', completed: true, dueDate: '2023-10-25' },
            { id: 104, title: 'Database Design', completed: false, dueDate: '2023-11-05' },
            { id: 105, title: 'Deployment', completed: false, dueDate: '2023-11-30' }
          ]
        },
        {
          id: 2,
          title: 'Learning Plan',
          description: 'Learn Vue 3 and TypeScript fundamentals and advanced features',
          category: 'Study',
          priority: 'Medium',
          priorityType: 'warning',
          dueDate: '2023-12-31',
          createdAt: '2023-10-10',
          taskCount: 4,
          progress: 25,
          tasks: [
            { id: 201, title: 'Vue 3 Basic Syntax', completed: true, dueDate: '2023-10-20' },
            { id: 202, title: 'Composition API', completed: false, dueDate: '2023-11-10' },
            { id: 203, title: 'TypeScript Basics', completed: false, dueDate: '2023-11-30' },
            { id: 204, title: 'Practical Project Development', completed: false, dueDate: '2023-12-31' }
          ]
        }
      ],
      selectedProject: null,
      showProjectDetails: false,
      showAddProjectDialog: false,
      showAddTaskDialog: false,
      isEditMode: false,
      projectForm: {
        title: '',
        description: '',
        category: '',
        priority: '',
        dueDate: ''
      },
      taskForm: {
        title: '',
        dueDate: ''
      },
      projectRules: {
        title: [
          { required: true, message: 'Please enter project name', trigger: 'blur' }
        ],
        category: [
          { required: true, message: 'Please select project category', trigger: 'change' }
        ],
        priority: [
          { required: true, message: 'Please select priority', trigger: 'change' }
        ],
        dueDate: [
          { required: true, message: 'Please select due date', trigger: 'change' }
        ]
      },
      taskRules: {
        title: [
          { required: true, message: 'Please enter task name', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    selectProject(project) {
      // Create a deep copy of the project to avoid directly modifying the original data
      this.selectedProject = JSON.parse(JSON.stringify(project))
      this.showProjectDetails = true
    },
    editProject(project) {
      this.isEditMode = true
      this.projectForm = {
        id: project.id,
        title: project.title,
        description: project.description,
        category: project.category,
        priority: project.priority,
        dueDate: project.dueDate
      }
      this.showAddProjectDialog = true
    },
    deleteProject(projectId) {
      this.$confirm('Are you sure you want to delete this project?', 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const index = this.projects.findIndex(p => p.id === projectId)
        if (index !== -1) {
          this.projects.splice(index, 1)
          this.$message.success('Project deleted successfully')
        }
      }).catch(() => {
        // Cancel deletion
      })
    },
    submitProjectForm() {
      this.$refs.projectFormRef.validate((valid) => {
        if (valid) {
          // 设置优先级类型
          const priorityTypeMap = {
            'High': 'danger',
            'Medium': 'warning',
            'Low': 'success'
          }
          
          if (this.isEditMode) {
            // Edit existing project
            const index = this.projects.findIndex(p => p.id === this.projectForm.id)
            if (index !== -1) {
              this.projects[index] = {
                ...this.projects[index],
                title: this.projectForm.title,
                description: this.projectForm.description,
                category: this.projectForm.category,
                priority: this.projectForm.priority,
                priorityType: priorityTypeMap[this.projectForm.priority],
                dueDate: this.projectForm.dueDate
              }
              this.$message.success('Project updated successfully')
            }
          } else {
            // Create new project
            const newProject = {
              id: Date.now(),
              title: this.projectForm.title,
              description: this.projectForm.description,
              category: this.projectForm.category,
              priority: this.projectForm.priority,
              priorityType: priorityTypeMap[this.projectForm.priority],
              dueDate: this.projectForm.dueDate,
              createdAt: new Date().toISOString().split('T')[0],
              taskCount: 0,
              progress: 0,
              tasks: []
            }
            this.projects.push(newProject)
            this.$message.success('Project created successfully')
          }
          
          // Close dialog and reset form
          this.showAddProjectDialog = false
          this.resetProjectForm()
        }
      })
    },
    submitTaskForm() {
      this.$refs.taskFormRef.validate((valid) => {
        if (valid && this.selectedProject) {
          const newTask = {
            id: Date.now(),
            title: this.taskForm.title,
            completed: false,
            dueDate: this.taskForm.dueDate || null
          }
          
          // 添加任务到选中的项目
          const projectIndex = this.projects.findIndex(p => p.id === this.selectedProject.id)
          if (projectIndex !== -1) {
            this.projects[projectIndex].tasks.push(newTask)
            this.projects[projectIndex].taskCount = this.projects[projectIndex].tasks.length
            this.updateProjectProgress(this.projects[projectIndex])
            
            // 同时更新选中的项目
            this.selectedProject.tasks.push(newTask)
            this.selectedProject.taskCount = this.selectedProject.tasks.length
            this.updateProjectProgress(this.selectedProject)
          }
          
          this.$message.success('Task added successfully')
          this.showAddTaskDialog = false
          this.resetTaskForm()
        }
      })
    },
    updateTaskStatus(task) {
      if (this.selectedProject) {
        // 更新原始项目数据
        const projectIndex = this.projects.findIndex(p => p.id === this.selectedProject.id)
        if (projectIndex !== -1) {
          const taskIndex = this.projects[projectIndex].tasks.findIndex(t => t.id === task.id)
          if (taskIndex !== -1) {
            this.projects[projectIndex].tasks[taskIndex].completed = task.completed
            this.updateProjectProgress(this.projects[projectIndex])
            
            // 同时更新选中的项目
            this.updateProjectProgress(this.selectedProject)
          }
        }
      }
    },
    updateProjectProgress(project) {
      if (project.tasks.length === 0) {
        project.progress = 0
      } else {
        const completedCount = project.tasks.filter(t => t.completed).length
        project.progress = Math.round((completedCount / project.tasks.length) * 100)
      }
    },
    resetProjectForm() {
      this.projectForm = {
        title: '',
        description: '',
        category: '',
        priority: '',
        dueDate: ''
      }
      this.isEditMode = false
      if (this.$refs.projectFormRef) {
        this.$refs.projectFormRef.resetFields()
      }
    },
    resetTaskForm() {
      this.taskForm = {
        title: '',
        dueDate: ''
      }
      if (this.$refs.taskFormRef) {
        this.$refs.taskFormRef.resetFields()
      }
    },
    formatDate(dateString) {
      if (!dateString) return 'None'
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US')
    },
    getProgressColor(progress) {
      if (progress === 100) return '#67c23a'
      if (progress >= 70) return '#409eff'
      if (progress >= 40) return '#e6a23c'
      return '#f56c6c'
    },
    getTaskDueType(task) {
      if (!task.dueDate) return 'info'
      const today = new Date().setHours(0, 0, 0, 0)
      const dueDate = new Date(task.dueDate).setHours(0, 0, 0, 0)
      if (dueDate < today) return 'danger'
      if (dueDate === today) return 'warning'
      return 'info'
    }
  }
}
</script>

<style scoped>
.projects-container {
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

.header-actions {
  display: flex;
  gap: 10px;
}

.projects-list {
  margin-top: 20px;
}

.project-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.project-card {
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.project-title {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  margin-right: 10px;
}

.project-actions {
  display: flex;
  gap: 5px;
}

.project-content {
  padding: 10px 0;
}

.project-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
}

.meta-icon {
  margin-right: 5px;
}

.project-progress {
  margin-bottom: 15px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
  font-size: 14px;
}

.progress-text {
  color: #606266;
}

.progress-percentage {
  font-weight: 500;
  color: #303133;
}

.project-tags {
  display: flex;
  gap: 10px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background-color: #fafafa;
  border-radius: 8px;
  border: 1px dashed #dcdfe6;
}

.project-details {
  max-height: 60vh;
  overflow-y: auto;
}

.details-section {
  margin-bottom: 30px;
}

.details-section h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.detail-label {
  width: 80px;
  color: #606266;
  font-size: 14px;
}

.detail-value {
  flex: 1;
  color: #303133;
  font-size: 14px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.task-list {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.task-item {
  border-bottom: 1px solid #ebeef5;
  transition: background-color 0.3s ease;
}

.task-item:hover {
  background-color: #f5f7fa;
}

.task-item:last-child {
  border-bottom: none;
}

.task-content {
  padding: 10px 0;
}

.task-completed {
  text-decoration: line-through;
  color: #909399;
}

.task-extra {
  display: flex;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .project-cards {
    grid-template-columns: 1fr;
  }
}
</style>