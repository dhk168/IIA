<template>
  <LightDialog
    v-model="dialogVisible"
    :title="isEditMode ? 'Edit Task' : 'New Task'"
    :width="width"
    @close="handleClose"
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
      <el-form-item label="Project" prop="project_id">
        <el-select v-model="taskForm.project_id" placeholder="Select project">
          <el-option label="No Project" value="" />
          <el-option v-for="project in projects" :key="project.projectId" :label="project.name" :value="project.projectId" />
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
      <el-button @click="handleCancel">Cancel</el-button>
      <el-button type="primary" @click="handleSave">Confirm</el-button>
    </template>
  </LightDialog>
</template>

<script>
import LightDialog from './LightDialog.vue'
export default {
  name: 'TaskDialog',
  components: {
    LightDialog
  },
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    width: {
      type: String,
      default: '600px'
    },
    isEditMode: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object,
      default: () => ({})
    },
    projects: {
      type: Array,
      default: () => []
    },
    tags: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: this.modelValue,
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
  watch: {
    modelValue: {
      handler(newVal) {
        this.dialogVisible = newVal;
        if (newVal) {
          this.initForm();
        }
      },
      immediate: true
    },
    dialogVisible: {
      handler(newVal) {
        this.$emit('update:model-value', newVal);
      },
      immediate: true
    }
  },
  methods: {
    initForm() {
      if (this.isEditMode && this.task) {
        // 如果是编辑模式，初始化表单数据
        this.taskForm = {
          title: this.task.title || '',
          description: this.task.description || '',
          category: this.task.category || 'task',
          project_id: this.task.project_id || null,
          start_date: this.task.start_date ? new Date(this.task.start_date) : null,
          due_date: this.task.due_date ? new Date(this.task.due_date) : null,
          priority: this.task.priority || 'none',
          has_recurrence: this.task.has_recurrence || false,
          recurrence_category: this.task.recurrence_category || 'weekly',
          recurrence_count: this.task.recurrence_count || 1,
          selectedTags: this.task.tags ? this.task.tags.map(tag => tag.tagId) : []
        };
      } else {
        // 否则重置表单
        this.resetForm();
      }
    },
    handleClose() {
      this.$emit('close');
      this.resetForm();
    },
    handleCancel() {
      this.dialogVisible = false;
      this.$emit('close');
      this.resetForm();
    },
    handleSave() {
      this.$refs.taskFormRef.validate((valid) => {
        if (valid) {
          const taskData = {
            title: this.taskForm.title,
            description: this.taskForm.description,
            category: this.taskForm.category,
            projectId: this.taskForm.project_id || null,
            parentTaskId: null,
            startDate: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
            dueDate: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
            reminderSentAt: null,
            priority: this.taskForm.priority
          };
          
          // 处理重复设置
          if (this.taskForm.has_recurrence) {
            taskData.has_recurrence = true;
            taskData.recurrence_category = this.taskForm.recurrence_category;
            taskData.recurrence_count = this.taskForm.recurrence_count;
          }
          
          // 如果是编辑模式，添加taskId
          if (this.isEditMode) {
            taskData.taskId = this.task.task_id;
          }
          
          // 处理标签数据
          taskData.tags = this.taskForm.selectedTags;
          
          this.$emit('save', taskData);
          this.dialogVisible = false;
          this.resetForm();
        }
      });
    },
    resetForm() {
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
      if (this.$refs.taskFormRef) {
        this.$refs.taskFormRef.resetFields();
      }
    }
  }
}
</script>

<style scoped>
/* 可以在这里添加组件特定的样式 */
</style>