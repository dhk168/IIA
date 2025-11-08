<template>
  <div class="projects-page">
    <div class="page-header">
      <el-button type="primary" @click="showAddProjectModal" class="light-button">
        <el-icon><Plus /></el-icon> New Project
      </el-button>
    </div>
    
    <!-- Project List -->
    <div class="project-cards">
      <ProjectCard
        v-for="project in projects"
        :key="project.id"
        :project="project"
        @click="selectProject(project)"
      />
      
      <!-- Empty State -->
      <div v-if="projects.length === 0" class="empty-state">
        <el-empty description="No projects" />
        <el-button type="primary" style="margin-top: 20px" class="light-button" @click="showAddProjectModal">
          <el-icon><Plus /></el-icon> Create First Project
        </el-button>
      </div>
    </div>
    
    <!-- Project Details Modal -->
    <ProjectDetailsModal
      :show="showProjectDetails"
      :project="selectedProject"
      @update:show="showProjectDetails = $event"
      @edit="editProject(selectedProject)"
      @delete="deleteProject(selectedProject.id)"
    />
    
    <!-- Project Form Modal -->
    <ProjectFormModal
      :show="showProjectForm"
      :is-edit-mode="isEditMode"
      :project="projectFormData"
      @update:show="showProjectForm = $event"
      @submit="handleFormSubmit"
    />
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import ProjectCard from './components/ProjectCard.vue'
import ProjectDetailsModal from './components/ProjectDetailsModal.vue'
import ProjectFormModal from './components/ProjectFormModal.vue'

export default {
  name: 'Projects',
  components: {
    Plus,
    ProjectCard,
    ProjectDetailsModal,
    ProjectFormModal
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
          tasks: [],
          taskCount: 0,
          progress: 10
        },
        {
          id: 2,
          title: 'Learning Plan',
          description: 'Study Vue 3 and Element Plus for frontend development',
          category: 'Study',
          priority: 'Medium',
          priorityType: 'warning',
          tasks: [],
          taskCount: 0,
          progress: 30
        },
        {
          id: 3,
          title: 'Fitness Program',
          description: 'Daily exercise routine and nutrition plan',
          category: 'Personal',
          priority: 'Low',
          priorityType: 'info',
          tasks: [],
          taskCount: 0,
          progress: 50
        }
      ],
      selectedProject: null,
      showProjectDetails: false,
      showProjectForm: false,
      isEditMode: false,
      projectFormData: {}
    }
  },
  methods: {
    selectProject(project) {
      this.selectedProject = { ...project }
      this.showProjectDetails = true
    },
    showAddProjectModal() {
      this.isEditMode = false
      this.projectFormData = {}
      this.showProjectForm = true
    },
    editProject(project) {
      this.isEditMode = true
      this.projectFormData = { ...project }
      this.showProjectForm = true
    },
    deleteProject(projectId) {
      this.$confirm('Are you sure you want to delete this project?', 'Confirm', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const index = this.projects.findIndex(p => p.id === projectId)
        if (index !== -1) {
          this.projects.splice(index, 1)
          this.$message.success('Project deleted successfully')
          
          // 如果删除的是当前选中的项目，关闭详情页
          if (this.selectedProject && this.selectedProject.id === projectId) {
            this.selectedProject = null
            this.showProjectDetails = false
          }
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    handleFormSubmit(formData) {
      if (this.isEditMode) {
        // 更新项目
        const index = this.projects.findIndex(p => p.id === this.projectFormData.id)
        if (index !== -1) {
          // 保留原始的tasks和计算字段
          const updatedProject = {
            ...formData,
            id: this.projectFormData.id,
            tasks: this.projectFormData.tasks || [],
            taskCount: this.projectFormData.taskCount || 0,
            progress: this.projectFormData.progress || 0,
            priorityType: this.getPriorityType(formData.priority)
          }
          this.projects[index] = updatedProject
          
          // 如果更新的是当前选中的项目，同步更新
          if (this.selectedProject && this.selectedProject.id === updatedProject.id) {
            this.selectedProject = { ...updatedProject }
          }
          
          this.$message.success('Project updated successfully')
        }
      } else {
        // 创建新项目
        const newProject = {
          ...formData,
          id: Date.now(),
          tasks: [],
          taskCount: 0,
          progress: 0,
          priorityType: this.getPriorityType(formData.priority)
        }
        this.projects.push(newProject)
        this.$message.success('Project created successfully')
      }
      
      this.showProjectForm = false
    },
    getPriorityType(priority) {
      switch (priority) {
        case 'High':
          return 'danger'
        case 'Medium':
          return 'warning'
        case 'Low':
          return 'info'
        default:
          return 'info'
      }
    },
    updateProjectProgress(project) {
      if (project.tasks.length === 0) {
        project.progress = 0
        return
      }
      
      const completedTasks = project.tasks.filter(task => task.completed).length
      project.progress = Math.round((completedTasks / project.tasks.length) * 100)
    }
  }
}
</script>

<style scoped>
.projects-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-start;
}

/* 使用全局定义的light-button样式 */
/* 无需在此处重复定义样式，已在light-button.css中统一管理 */

.project-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.empty-state {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
}
</style>