<template>
  <el-dialog
    v-bind="{ modelValue: show }"
    @update:modelValue="handleClose"
    :title="isEditMode ? 'Edit Project' : 'Add Project'"
    width="600px"
  >
    <el-form :model="projectForm" :rules="rules" ref="formRef">
      <el-form-item label="Project Name" prop="name">
        <el-input v-model="projectForm.name" placeholder="Enter project name" />
      </el-form-item>
      <el-form-item label="Description" prop="description">
        <el-input v-model="projectForm.description" type="textarea" rows="4" placeholder="Enter project description" />
      </el-form-item>
      <el-form-item label="Category" prop="category">
        <el-select v-model="projectForm.category" placeholder="Select category">
          <el-option label="Work" value="Work" />
          <el-option label="Study" value="Study" />
          <el-option label="Personal" value="Personal" />
          <el-option label="Other" value="Other" />
        </el-select>
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="projectForm.priority" placeholder="Select priority">
          <el-option label="High" value="High" />
          <el-option label="Medium" value="Medium" />
          <el-option label="Low" value="Low" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">Cancel</el-button>
      <el-button type="primary" @click="handleSubmit">Confirm</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, watch } from 'vue'

export default {
  name: 'ProjectFormModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    isEditMode: {
      type: Boolean,
      default: false
    },
    project: {
      type: Object,
      default: () => ({})
    }
  },
  setup(props, { emit }) {
    const projectForm = reactive({
      name: '',
      description: '',
      category: '',
      priority: 'Medium'
    });
    
    const rules = {
      name: [
        { required: true, message: 'Please enter project name', trigger: 'blur' }
      ],
      category: [
        { required: true, message: 'Please select project category', trigger: 'change' }
      ],
      priority: [
        { required: true, message: 'Please select priority', trigger: 'change' }
      ]
    };

    // 监听show变化，当弹窗打开时重置表单
    watch(() => props.show, (newShow) => {
      if (newShow) {
        if (props.isEditMode && props.project) {
          // 编辑模式：填充现有项目数据
          Object.assign(projectForm, {
            name: props.project.name || '',
            description: props.project.description || '',
            category: props.project.category || '',
            priority: props.project.priority || 'Medium'
          });
        } else {
          // 添加模式：重置表单
          Object.assign(projectForm, {
            name: '',
            description: '',
            category: '',
            priority: 'Medium'
          });
        }
      }
    });

    const handleClose = () => {
      emit('update:show', false);
    };

    const handleSubmit = () => {
      emit('submit', { ...projectForm });
    };

    return {
      projectForm,
      rules,
      handleSubmit,
      handleClose
    };
  }
}
</script>