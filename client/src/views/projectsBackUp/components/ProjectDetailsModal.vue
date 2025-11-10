<template>
  <el-dialog
    v-bind="{ modelValue: show }"
    @update:modelValue="handleClose"
    :title="project ? project.title : 'Project Details'"
    width="800px"
  >
    <div v-if="project" class="project-details">
      <div class="details-section">
        <h3>Project Information</h3>
        <div class="detail-row">
          <span class="detail-label">Description:</span>
          <span class="detail-value">{{ project.description || 'No description' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Category:</span>
          <el-tag type="info">{{ project.category }}</el-tag>
        </div>
        <div class="detail-row">
          <span class="detail-label">Priority:</span>
          <el-tag :type="project.priorityType">{{ project.priority }}</el-tag>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button type="primary" @click="handleEdit">Edit</el-button>
      <el-button type="danger" @click="handleDelete">Delete</el-button>
      <el-button @click="handleClose">Close</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'ProjectDetailsModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    project: {
      type: Object,
      default: null
    }
  },
  emits: ['close', 'update:show', 'edit', 'delete'],
  methods: {
    handleClose() {
      this.$emit('update:show', false)
    },
    handleEdit() {
      this.$emit('edit')
    },
    handleDelete() {
      this.$emit('delete')
    }
  }
}
</script>

<style scoped>
.project-details {
  padding: 10px;
}

.details-section {
  margin-bottom: 20px;
}

.details-section h3 {
  margin-bottom: 15px;
  color: #333;
}

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.detail-label {
  width: 120px;
  font-weight: 500;
  color: #606266;
}

.detail-value {
  flex: 1;
  color: #303133;
}
</style>