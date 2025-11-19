<template>
  <div class="reminder-projects-page">
    <!-- Add Project Button (Empty State) -->
    <EmptyProjects v-if="projects.length === 0" @create-project="showAddProjectModal" />

    <!-- Page Header (Non-Empty State) -->
    <div class="page-header" v-if="projects.length > 0">
      <LightButton type="primary" @click="showAddProjectModal">
        New Project
      </LightButton>
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
import { reminderProjectAPI } from '@/api/reminder';
import { OfficeBuilding, Document } from '@element-plus/icons-vue';
import EmptyProjects from './components/EmptyProjects.vue';
import { Vue3IconPicker } from 'vue3-icon-picker';
import ProjectCard from './components/ProjectCard.vue';
import ProjectFormModal from './components/ProjectFormModal.vue';
import LightButton from '@/components/LightButton.vue';


export default {
  name: 'ReminderProjects',
  components: {
    Vue3IconPicker,
    OfficeBuilding,
    ProjectCard,
    Document,
    EmptyProjects,
    ProjectFormModal,
    LightButton
  },
  inject: ['showToast'],
  data() {
    return {
      projects: [], // Empty array, not using mock data
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
    this.fetchProjects()
  },
  methods: {
    async fetchProjects() {
      try {
        const response = await reminderProjectAPI.getAllProjects();
        // Modify success response detection logic to match backend response format
        if (response && response.code === 200) {
          this.projects = response.data || [];
          // Remove success toast notification for query
        } else {
          this.projects = []; // Clear projects array
          // Show failure toast
          this.showToast('error', response?.msg || 'Failed to load projects');
        }
      } catch (error) {
        this.projects = []; // Clear projects array
        // Show failure toast
        this.showToast('error', 'Failed to load projects');
      }
    },
    
    selectProject(project) {
      // Display project detail modal
      this.selectedProject = project;
      // Temporarily use project form modal to show project details
      // A dedicated detail modal can be created later
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
      // Handle form submission success
      this.showToast('success', result.message);
      this.showProjectForm = false;
      this.fetchProjects(); // Refresh project list
    },
    
    handleFormError(error) {
      // Handle form submission error
      this.showToast('error', error.message);
    },
    
    async toggleArchive(project) {
      try {
        const updatedProject = { ...project };
        updatedProject.isArchived = !updatedProject.isArchived;
        
        const response = await reminderProjectAPI.updateProject(updatedProject);
        // Modify success response detection logic to match backend response format
        if (response?.code === 200) {
          // Keep archive/unarchive success toast
          this.showToast('success', response?.msg || (updatedProject.isArchived ? 'Project archived' : 'Project unarchived'));
          this.fetchProjects(); // Refresh project list
        } else {
          this.showToast('error', response?.msg || 'Operation failed');
        }
      } catch (error) {
        console.error('Error toggling archive status:', error);
        this.showToast('error', 'An error occurred while updating project');
      }
    },
  }
}
</script>

<style>
@import '@/assets/styles/page/projects.css';
</style>
