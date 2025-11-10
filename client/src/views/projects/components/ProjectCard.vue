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
      <el-button size="mini" type="text" @click.stop="$emit('toggle-archive', project)">
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
    // 十六进制颜色转RGB的工具方法
    hexToRgb(hex) {
      // 移除#号
      hex = hex.replace('#', '');
      
      // 解析RGB值
      const r = parseInt(hex.substring(0, 2), 16);
      const g = parseInt(hex.substring(2, 4), 16);
      const b = parseInt(hex.substring(4, 6), 16);
      
      return `${r}, ${g}, ${b}`;
    }
  }
}
</script>