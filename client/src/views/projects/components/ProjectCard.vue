<template>
  <el-card 
    class="project-card"
    :style="{
      backgroundColor: `rgba(${hexToRgb(project.color || '#1890ff')}, 0.2)`,
      border: `1px solid rgba(${hexToRgb(project.color || '#1890ff')}, 0.3)`,
      '--project-color': project.color || '#1890ff'
    }"
    @click="$emit('select', project)"
  >
    <div class="project-header">
      <div class="project-title" :style="{ color: project.color || '#1890ff' }">
        <i v-if="project.icon" class="fa" :class="`fa-${project.icon}`"></i>
        {{ project.name }}
      </div>
      <el-button link @click.stop="$emit('toggle-archive', project)">
        {{ project.isArchived ? 'Unarchive' : 'Archive' }}
      </el-button>
    </div>
    <div class="project-description" v-if="project.description">
      {{ project.description }}
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'ProjectCard',
  props: {
    project: {
      type: Object,
      required: true,
      default: () => ({})
    }
  },
  emits: ['select', 'toggle-archive'],
  methods: {
    // Hex to RGB conversion utility method
    hexToRgb(hex) {
      // Remove the # sign
      hex = hex.replace('#', '');
      
      // Parse RGB values
      const r = parseInt(hex.substring(0, 2), 16);
      const g = parseInt(hex.substring(2, 4), 16);
      const b = parseInt(hex.substring(4, 6), 16);
      
      return `${r}, ${g}, ${b}`;
    }
  }
}
</script>

<style scoped>
.project-card {
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
  backdrop-filter: blur(10px);
  border-radius: 12px;
  width: 100%;
  max-width: 100%;
}

.project-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  background-color: rgba(255, 255, 255, 0.6);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.project-title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.project-description {
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 12px;
  line-height: 1.5;
}

/* 响应式设计：小屏幕优化 */
@media (max-width: 480px) {
  .project-card {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
  }
  
  .project-title {
    font-size: 16px;
  }
}
</style>