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
  /* 侧边栏整体样式调整为底部横向导航 */
  .sidebar {
    width: 100vw; /* 使用视口宽度确保全宽 */
    height: 70px;
    margin: 0;
    border-radius: 0;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
    border-right: none;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    position: fixed; /* 固定在底部 */
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1000; /* 确保在顶部 */
  }
  
  /* 移除Element UI的容器限制 */
  .el-aside {
    width: 100% !important;
    height: 100%;
  }
  
  /* 菜单容器调整为横向布局 */
  .el-menu-vertical {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    height: 100%;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
  }
  
  /* 菜单项调整为横向排列的圆圈按钮 */
  .el-menu-vertical .el-menu-item {
    width: 50px;
    height: 50px;
    line-height: 1;
    margin: 10px 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }
  
  /* 调整图标大小 */
  .menu-icon {
    font-size: 20px;
  }
}
</style>