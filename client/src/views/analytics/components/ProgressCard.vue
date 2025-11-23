<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>Task Progress by Project</span>
      </div>
    </template>
    <div class="chart-content">
      <div v-for="project in projectProgress" :key="project.projectId" class="progress-item">
        <div class="progress-label">{{ project.projectName || 'Uncategorized' }}</div>
        <el-progress :percentage="project.completionRate" :format="formatRate" :color="project.color" />
        <div class="progress-stats">{{ project.completedTasks }} / {{ project.totalTasks }} tasks completed</div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'ProgressCard',
  props: {
    projectProgress: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    formatRate(percentage) {
      return `${percentage}%`
    }
  }
}
</script>

<style scoped>
.chart-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  color: #303133;
}

.chart-content {
  padding: 20px 0;
}

.progress-item {
  margin-bottom: 20px;
}

.progress-label {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  font-weight: 500;
}

.progress-stats {
  font-size: 12px;
  color: #909399;
  text-align: right;
  margin-top: 4px;
}
</style>