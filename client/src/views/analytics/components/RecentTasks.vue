<template>
  <el-card class="recent-tasks-card">
    <template #header>
      <div class="card-header">
        <span>Recently Completed Tasks</span>
      </div>
    </template>
    <el-table :data="recentCompletedTasks" stripe style="width: 100%">
      <el-table-column prop="title" label="Task Name" width="300" />
      <el-table-column prop="category" label="Category">
        <template #default="scope">
          <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="completedTime" label="Completion Time">
        <template #default="scope">
          {{ formatDate(scope.row.completedTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="Priority">
        <template #default="scope">
          <el-tag :type="getPriorityType(scope.row.priority)">{{ scope.row.priority }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  name: 'RecentTasks',
  props: {
    recentCompletedTasks: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    formatDate(dateString) {
      return new Date(dateString).toLocaleString('en-US')
    },
    getCategoryType(category) {
      const typeMap = {
        'Work': 'primary',
        'Study': 'success',
        'Life': 'warning',
        'Health': 'danger'
      }
      return typeMap[category] || 'info'
    },
    getPriorityType(priority) {
      const typeMap = {
        'High': 'danger',
        'Medium': 'warning',
        'Low': 'success'
      }
      return typeMap[priority] || 'info'
    }
  }
}
</script>

<style scoped>
.recent-tasks-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  color: #303133;
}
</style>