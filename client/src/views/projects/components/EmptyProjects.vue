<template>
  <div class="empty-state">
    <!-- 更精美的项目图标 -->
    <svg width="100" height="100" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <!-- 文件夹底座 -->
      <path d="M21 15V8.6H13.6L12 7V4.5C12 3.837 11.463 3.3 10.8 3.3H3.6C2.937 3.3 2.4 3.837 2.4 4.5V19.5C2.4 20.163 2.937 20.7 3.6 20.7H20.4C21.063 20.7 21.6 20.163 21.6 19.5V15H21Z" fill="url(#folderGradient)" stroke="#6a5acd" stroke-width="0.5"/>
      <!-- 文件夹标签 -->
      <path d="M3.6 10.5H21.6" stroke="#836fff" stroke-width="0.8" stroke-linecap="round"/>
      <!-- 装饰点 -->
      <circle cx="6" cy="6" r="0.4" fill="#9370db"/>
      <circle cx="9" cy="6" r="0.4" fill="#9370db"/>
      <circle cx="6" cy="13.5" r="0.4" fill="#9370db"/>
      <circle cx="9" cy="13.5" r="0.4" fill="#9370db"/>
      <circle cx="12" cy="13.5" r="0.4" fill="#9370db"/>
      <!-- 加号 -->
      <circle cx="19" cy="18" r="1.5" fill="white" stroke="#6a5acd" stroke-width="0.5"/>
      <path d="M19 17V19" stroke="#6a5acd" stroke-width="0.8" stroke-linecap="round"/>
      <path d="M18 18H20" stroke="#6a5acd" stroke-width="0.8" stroke-linecap="round"/>
      <!-- 渐变定义 -->
      <defs>
        <linearGradient id="folderGradient" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stop-color="#e6e6fa"/>
          <stop offset="100%" stop-color="#c4b9ff"/>
        </linearGradient>
      </defs>
    </svg>
    
    <h3 class="empty-title">No Projects Yet</h3>
    <p class="empty-description">Get started by creating your first project</p>
    <LightButton type="primary" @click="$emit('create-project')">
        <i class="el-icon-plus"></i>
        Create New Project
      </LightButton>
  </div>
</template>

<script>
import LightButton from '@/components/LightButton.vue';

export default {
  name: 'EmptyProjects',
  components: {
    LightButton
  },
  emits: ['create-project']
}
</script>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  max-width: 400px;
  margin: 2rem auto;
  text-align: center;
}

.empty-title {
  margin: 1.5rem 0 0.5rem 0;
  font-size: 1.5rem;
  color: #ffffff;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.empty-description {
  margin: 0 0 2rem 0;
  color: #e6e6fa;
  text-align: center;
  line-height: 1.5;
  font-size: 1.1rem;
}
</style>