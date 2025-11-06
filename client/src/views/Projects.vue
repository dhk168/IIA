<template>
  <div class="projects-container">
    <div class="page-header">
      <h1>项目计划</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showAddProjectDialog = true">
          <el-icon><Plus /></el-icon> 新建项目
        </el-button>
      </div>
    </div>
    
    <!-- 项目列表 -->
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
                      <el-dropdown-item @click.stop="editProject(project)">编辑</el-dropdown-item>
                      <el-dropdown-item @click.stop="deleteProject(project.id)" danger>删除</el-dropdown-item>
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
                <span>{{ project.taskCount }} 个任务</span>
              </div>
              <div class="meta-item">
                <el-icon class="meta-icon"><Calendar /></el-icon>
                <span>{{ formatDate(project.dueDate) }}</span>
              </div>
            </div>
            
            <div class="project-progress">
              <div class="progress-info">
                <span class="progress-text">进度</span>
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
        
        <!-- 空状态 -->
        <div v-if="projects.length === 0" class="empty-state">
          <el-empty description="暂无项目" />
          <el-button type="primary" style="margin-top: 20px" @click="showAddProjectDialog = true">
            <el-icon><Plus /></el-icon> 创建第一个项目
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 项目详情模态框 -->
    <el-dialog
      v-model="showProjectDetails"
      :title="selectedProject ? selectedProject.title : '项目详情'"
      width="800px"
    >
      <div v-if="selectedProject" class="project-details">
        <div class="details-section">
          <h3>项目信息</h3>
          <div class="detail-row">
            <span class="detail-label">描述：</span>
            <span class="detail-value">{{ selectedProject.description || '无描述' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">类别：</span>
            <el-tag type="info">{{ selectedProject.category }}</el-tag>
          </div>
          <div class="detail-row">
            <span class="detail-label">优先级：</span>
            <el-tag :type="selectedProject.priorityType">{{ selectedProject.priority }}</el-tag>
          </div>
          <div class="detail-row">
            <span class="detail-label">截止日期：</span>
            <span>{{ formatDate(selectedProject.dueDate) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">创建日期：</span>
            <span>{{ formatDate(selectedProject.createdAt) }}</span>
          </div>
        </div>
        
        <div class="details-section">
          <h3>任务列表</h3>
          <el-list :data="selectedProject.tasks" class="task-list">
            <template #header>
              <div class="list-header">
                <span>任务 ({{ selectedProject.tasks.length }})</span>
                <el-button size="small" type="primary" @click="showAddTaskDialog = true">
                  <el-icon><Plus /></el-icon> 添加任务
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
    
    <!-- 添加/编辑项目模态框 -->
    <el-dialog
      v-model="showAddProjectDialog"
      :title="isEditMode ? '编辑项目' : '新建项目'"
      width="600px"
    >
      <el-form :model="projectForm" :rules="projectRules" ref="projectFormRef">
        <el-form-item label="项目名称" prop="title">
          <el-input v-model="projectForm.title" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="projectForm.description" type="textarea" placeholder="请输入项目描述" :rows="3" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="projectForm.category" placeholder="请选择项目类别">
            <el-option label="工作" value="工作" />
            <el-option label="学习" value="学习" />
            <el-option label="生活" value="生活" />
            <el-option label="健康" value="健康" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="projectForm.priority" placeholder="请选择优先级">
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker
            v-model="projectForm.dueDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddProjectDialog = false">取消</el-button>
        <el-button type="primary" @click="submitProjectForm">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 添加任务模态框 -->
    <el-dialog
      v-model="showAddTaskDialog"
      title="添加任务"
      width="500px"
    >
      <el-form :model="taskForm" :rules="taskRules" ref="taskFormRef">
        <el-form-item label="任务名称" prop="title">
          <el-input v-model="taskForm.title" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker
            v-model="taskForm.dueDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTaskDialog = false">取消</el-button>
        <el-button type="primary" @click="submitTaskForm">确定</el-button>
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
          title: '网站开发',
          description: '创建公司官方网站，包含首页、产品展示、关于我们等页面',
          category: '工作',
          priority: '高',
          priorityType: 'danger',
          dueDate: '2023-11-30',
          createdAt: '2023-10-01',
          taskCount: 5,
          progress: 60,
          tasks: [
            { id: 101, title: '设计网站原型', completed: true, dueDate: '2023-10-10' },
            { id: 102, title: '前端页面开发', completed: true, dueDate: '2023-10-20' },
            { id: 103, title: '后端接口实现', completed: true, dueDate: '2023-10-25' },
            { id: 104, title: '数据库设计', completed: false, dueDate: '2023-11-05' },
            { id: 105, title: '上线部署', completed: false, dueDate: '2023-11-30' }
          ]
        },
        {
          id: 2,
          title: '学习计划',
          description: '学习Vue 3和TypeScript的基础知识和高级特性',
          category: '学习',
          priority: '中',
          priorityType: 'warning',
          dueDate: '2023-12-31',
          createdAt: '2023-10-10',
          taskCount: 4,
          progress: 25,
          tasks: [
            { id: 201, title: 'Vue 3基础语法', completed: true, dueDate: '2023-10-20' },
            { id: 202, title: '组合式API', completed: false, dueDate: '2023-11-10' },
            { id: 203, title: 'TypeScript基础', completed: false, dueDate: '2023-11-30' },
            { id: 204, title: '实战项目开发', completed: false, dueDate: '2023-12-31' }
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
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择项目类别', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        dueDate: [
          { required: true, message: '请选择截止日期', trigger: 'change' }
        ]
      },
      taskRules: {
        title: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    selectProject(project) {
      // 创建项目的深拷贝，避免直接修改原数据
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
      this.$confirm('确定要删除这个项目吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.projects.findIndex(p => p.id === projectId)
        if (index !== -1) {
          this.projects.splice(index, 1)
          this.$message.success('项目删除成功')
        }
      }).catch(() => {
        // 取消删除
      })
    },
    submitProjectForm() {
      this.$refs.projectFormRef.validate((valid) => {
        if (valid) {
          // 设置优先级类型
          const priorityTypeMap = {
            '高': 'danger',
            '中': 'warning',
            '低': 'success'
          }
          
          if (this.isEditMode) {
            // 编辑现有项目
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
              this.$message.success('项目更新成功')
            }
          } else {
            // 创建新项目
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
            this.$message.success('项目创建成功')
          }
          
          // 关闭对话框并重置表单
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
          
          this.$message.success('任务添加成功')
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
      if (!dateString) return '无'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
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