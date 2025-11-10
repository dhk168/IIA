<template>
  <div class="reminder-projects-page">
    <!-- Add Project Button (Empty State) -->
    <EmptyProjects v-if="projects.length === 0" @create-project="showAddProjectModal" />

    <!-- Page Header (Non-Empty State) -->
    <div class="page-header" v-if="projects.length > 0">
      <el-button type="primary" @click="showAddProjectModal" class="light-button">
        New Project
      </el-button>
    </div>
    
    <!-- Project List (Non-Empty State) -->
    <div class="project-cards">
      <ProjectCard 
        v-for="project in projects" 
        :key="project.projectId"
        :project="project"
        @select="selectProject"
        @toggle-archive="toggleArchive"
      />
    </div>
    
    <!-- Project Form Modal -->
    <ProjectFormModal
      :visible="showProjectForm"
      :project-data="projectFormData"
      :is-edit-mode="isEditMode"
      @close="showProjectForm = false"
      @success="handleFormSuccess"
      @error="handleFormError"
    />
    
  </div>
</template>

<script>
import { reminderProjectAPI } from '../../api/reminder';
import { OfficeBuilding, Document } from '@element-plus/icons-vue';
import EmptyProjects from './components/EmptyProjects.vue';
import { Vue3IconPicker } from 'vue3-icon-picker';
import ProjectCard from './components/ProjectCard.vue';
import ProjectFormModal from './components/ProjectFormModal.vue';



export default {
  name: 'ReminderProjects',
  components: {
    Vue3IconPicker,
    OfficeBuilding,
    ProjectCard,
    Document,
    EmptyProjects,
    ProjectFormModal
  },
  data() {
    return {
      projects: [], // 空数组，不使用mock数据
      selectedProject: null,
      showProjectForm: false,
      isEditMode: false,
      projectFormData: {
        name: '',
        description: '',
        color: '#1890ff',
        icon: '',
        isArchived: false
      },
      rules: {
        name: [
          { required: true, message: 'Please enter project name', trigger: 'blur' },
          { min: 1, max: 255, message: 'Length should be 1 to 255 characters', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 打印localStorage中存储的token信息
    const storedToken = localStorage.getItem('token')
    console.log('localStorage中的token:', storedToken)
    console.log('isLoggedIn状态:', localStorage.getItem('isLoggedIn'))
    
    if (!storedToken) {
      console.log('请先登录获取真实token')
    }
    
    this.fetchProjects()
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
    
    async fetchProjects() {
      try {
        console.log('准备调用API，检查请求头中的token')
        const response = await reminderProjectAPI.getAllProjects();
        // 修改成功响应的检测逻辑，与后端响应格式匹配
        if (response && response.code === 200) {
          this.projects = response.data || [];
          // 移除查询成功的弹窗提示
        } else {
          console.warn('API returned non-success response:', response);
          this.projects = []; // 清空projects数组
          // 显示失败弹窗
          this.createGlassToast('error', response?.msg || 'Failed to load projects');
        }
      } catch (error) {
        console.error('API call failed:', error);
        this.projects = []; // 清空projects数组
        // 显示失败弹窗
        this.createGlassToast('error', 'Failed to load projects');
      }
    },
    
    selectProject(project) {
      // 显示项目详情弹窗
      this.selectedProject = project;
      // 这里暂时使用项目表单弹窗来展示项目详情
      // 后续可以创建专门的详情弹窗
      this.isEditMode = true;
      this.projectFormData = { ...project };
      this.showProjectForm = true;
    },
    
    showAddProjectModal() {
      this.isEditMode = false;
      this.projectFormData = {
        name: '',
        description: '',
        color: '#1890ff',
        icon: '',
        isArchived: false
      };
      this.showProjectForm = true;
    },
    
    handleFormSuccess(result) {
      // 处理表单提交成功
      this.createGlassToast('success', result.message);
      this.showProjectForm = false;
      this.fetchProjects(); // 重新获取项目列表
    },
    
    handleFormError(error) {
      // 处理表单提交错误
      this.createGlassToast('error', error.message);
    },
    
    async toggleArchive(project) {
      try {
        const updatedProject = { ...project };
        updatedProject.isArchived = !updatedProject.isArchived;
        
        const response = await reminderProjectAPI.updateProject(updatedProject);
        // 修改成功响应的检测逻辑，与后端响应格式匹配
        if (response?.code === 200) {
          // 保留归档/取消归档成功的弹窗
          this.createGlassToast('success', response?.msg || (updatedProject.isArchived ? 'Project archived' : 'Project unarchived'));
          this.fetchProjects(); // 重新获取项目列表
        } else {
          this.createGlassToast('error', response?.msg || 'Operation failed');
        }
      } catch (error) {
        console.error('Error toggling archive status:', error);
        this.createGlassToast('error', 'An error occurred while updating project');
      }
    },
    

  }
}
</script>

<style>
/* 导入共享的认证页面样式 */
@import '../../assets/styles/page/projects.css';
</style>
