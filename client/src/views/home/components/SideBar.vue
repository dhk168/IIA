<template>
  <LightDiv class="sidebar">
    <el-aside width="80px">
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        router
        @select="handleMenuSelect"
        background-color="transparent"
        text-color="#fff"
        active-text-color="#fff"
      >
        <el-menu-item index="/home/projects">
          <el-icon class="menu-icon"><Box /></el-icon>
        </el-menu-item>
        <el-menu-item index="/home/tasks">
          <el-icon class="menu-icon"><List /></el-icon>
        </el-menu-item>
        <el-menu-item index="/home/analytics">
          <el-icon class="menu-icon"><DataLine /></el-icon>
        </el-menu-item>
        <el-menu-item index="/home/debug">
          <el-icon class="menu-icon"><Tools /></el-icon>
        </el-menu-item>
        <el-menu-item index="/home/demo">
          <el-icon class="menu-icon"><Tools /></el-icon>
        </el-menu-item>
      </el-menu>
    </el-aside>
  </LightDiv>
</template>

<script>
import { Box, List, DataLine, Tools } from '@element-plus/icons-vue'
import LightDiv from '@/components/LightDiv.vue'

export default {
  name: 'SideBar',
  components: {
    Box,
    List,
    DataLine,
    Tools,
    LightDiv
  },
  props: {
    activeMenu: {
      type: String,
      default: '/home/projects'
    }
  },
  methods: {
    handleMenuSelect(key) {
      this.$emit('menu-select', key)
    }
  }
}
</script>

<style scoped>
/* 侧边栏样式 */
.sidebar {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.15);
  position: relative;
  transition: all 0.3s ease;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  margin: 16px 0 16px 16px;
  height: calc(100vh - 32px);
}

.el-menu-vertical {
  border-right: none;
  background-color: transparent;
}

.el-menu-vertical .el-menu-item {
  width: 60px;
  height: 60px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: none;
  transition: all 0.3s ease;
  border-radius: 50%;
  margin: 12px auto;
  color: rgba(255, 255, 255, 0.9);
  border: none;
  position: relative;
  padding: 0 !important;
  text-indent: 0;
}

.el-menu-vertical .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.2);
  color: #ffffff;
  backdrop-filter: none;
  border-color: transparent;
}

.el-menu-vertical .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.3) !important;
  color: #ffffff !important;
  backdrop-filter: none;
  border-radius: 50%;
  box-shadow: none;
  border: none;
}

/* 菜单图标样式 */
.menu-icon {
  font-size: 23px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 0 !important;
  margin: 0 auto;
  left: 0;
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 60px;
    margin: 12px 0 12px 12px;
    height: calc(100vh - 24px);
  }
  
  .el-menu-vertical .el-menu-item {
    width: 48px;
    height: 48px;
    line-height: 48px;
    margin: 8px auto;
  }
  
  .menu-icon {
    font-size: 20px;
  }
}
</style>