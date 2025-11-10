<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEditMode ? 'Edit Project' : 'New Project'"
    width="500px"
    @close="handleDialogClose"
  >
    <el-form :model="formData" :rules="rules" ref="projectForm" label-width="100px">
      <el-form-item label="Project Name" prop="name">
        <el-input v-model="formData.name" placeholder="Enter project name" />
      </el-form-item>
      
      <el-form-item label="Description">
        <el-input 
          v-model="formData.description" 
          type="textarea" 
          placeholder="Enter project description"
          :rows="3"
        />
      </el-form-item>
      
      <el-form-item label="Color">
        <el-color-picker v-model="formData.color" show-alpha />
      </el-form-item>
      
      <el-form-item label="Icon">
        <Vue3IconPicker 
          v-model="formData.icon" 
          placeholder="Select an icon" 
          :valueType="'name'"
          :iconLibrary="'fa'"
          :displaySearch="true"
          :clearable="true"
        />
      </el-form-item>
      
      <!-- Sort Order field removed as it will be handled by the system later -->
    </el-form>
    
    <template #footer>
      <el-button @click="handleCancel">Cancel</el-button>
      <el-button type="primary" @click="handleSubmit">Submit</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { Vue3IconPicker } from 'vue3-icon-picker';
import { reminderProjectAPI } from '../../../api/reminder';

export default {
  name: 'ProjectFormModal',
  components: {
    Vue3IconPicker
  },
  emits: ['close', 'success', 'error'],
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Object,
      default: () => ({
        name: '',
        description: '',
        color: '#1890ff',
        icon: '',
        isArchived: false
      })
    },
    isEditMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      formData: {
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
  watch: {
    projectData: {
      handler(newData) {
        if (newData) {
          this.formData = { ...newData };
        }
      },
      deep: true,
      immediate: true
    },
    visible: {
      handler(newVal) {
        this.dialogVisible = newVal;
      },
      immediate: true
    }
  },
  methods: {
    handleDialogClose() {
      this.$emit('close');
    },
    handleCancel() {
      this.$emit('close');
    },
    
    async handleSubmit() {
      try {
        await this.$refs.projectForm.validate();
        
        console.log('Submitting form data:', this.formData);
        
        let response;
        if (this.isEditMode) {
          // 更新项目
          response = await reminderProjectAPI.updateProject(this.formData);
        } else {
          // 创建新项目
          response = await reminderProjectAPI.createProject(this.formData);
        }
        
        console.log('API response:', response);
        
        // 成功响应的检测逻辑，与后端响应格式匹配
        const isSuccess = response?.code === 200;
        
        if (isSuccess) {
          this.$emit('success', {
            message: response?.msg || (this.isEditMode ? 'Project updated' : 'Project created'),
            data: response?.data
          });
        } else {
          console.error('API returned non-success response:', response);
          this.$emit('error', {
            message: response?.msg || 'Operation failed'
          });
        }
      } catch (error) {
        console.error('Error submitting form:', error);
        let errorMessage = 'An error occurred while processing your request';
        if (error.response?.data?.message) {
          errorMessage = error.response.data.message;
        } else if (error.message) {
          errorMessage = error.message;
        }
        this.$emit('error', { message: errorMessage });
      }
    }
  }
}
</script>