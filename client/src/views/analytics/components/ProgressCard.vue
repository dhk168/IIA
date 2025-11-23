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
  /* 毛玻璃效果 */
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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
  /* 为单个进度项也添加轻微的毛玻璃效果 */
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border-radius: 6px;
  padding: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.progress-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.progress-label {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  font-weight: 500;
}

.progress-stats {
  font-size: 12px;
  color: #606266;
  text-align: right;
  margin-top: 4px;
  font-weight: 400;
}
</style>