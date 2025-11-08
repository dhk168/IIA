<template>
  <el-card class="project-card" @click="handleCardClick">
    <div class="project-header">
      <h3 class="project-title">{{ project.title }}</h3>
    </div>
    
    <div class="project-content">
      <div class="project-meta">
        <div class="meta-item">
          <el-icon class="meta-icon"><Document /></el-icon>
          <span>{{ project.taskCount }} Tasks</span>
        </div>
      </div>
      
      <div class="project-progress">
        <div class="progress-info">
          <span class="progress-text">Progress</span>
          <span class="progress-percentage">{{ project.progress }}%</span>
        </div>
        <el-progress 
          :percentage="project.progress" 
          :color="getProgressColor(project.progress)"
          :show-text="false"
        />
      </div>
      

      
      <div class="project-tags">
        <el-tag size="small" :type="project.priorityType">{{ project.priority }}</el-tag>
        <el-tag size="small" type="info">{{ project.category }}</el-tag>
      </div>
    </div>
  </el-card>
</template>

<script>
import { Setting, Document } from '@element-plus/icons-vue'

export default {
  name: 'ProjectCard',
  components: {
    Setting,
    Document
  },
  props: {
    project: {
      type: Object,
      required: true
    }
  },
  emits: ['click'],
  methods: {
    handleCardClick() {
      this.$emit('click')
    },
    getProgressColor(progress) {
      if (progress === 100) return '#67c23a'
      if (progress >= 70) return '#409eff'
      if (progress >= 40) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.project-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.project-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.6);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.project-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}



.project-content {
  margin-top: 16px;
}

.project-meta {
  margin-bottom: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  color: #606266;
}

.meta-icon {
  margin-right: 8px;
}

.project-progress {
  margin-bottom: 16px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}

.progress-text {
  font-weight: 500;
}

.progress-percentage {
  font-weight: 600;
}

.project-tags {
  display: flex;
  gap: 8px;
  margin-top: 16px;
}
</style>