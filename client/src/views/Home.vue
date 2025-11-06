<template>
  <div class="main-container">
    <el-container class="container">
      <!-- 侧边栏 -->
      <el-aside width="240px" class="sidebar">
        <div class="sidebar-header">
          <h2>个人信息助手</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          router
          @select="handleMenuSelect"
        >
          <el-menu-item index="/home/projects">
          <el-icon><Document /></el-icon>
          <span>项目计划</span>
        </el-menu-item>
        <el-menu-item index="/home/tasks">
          <el-icon><List /></el-icon>
          <span>任务管理</span>
        </el-menu-item>
        <el-menu-item index="/home/analytics">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据分析</span>
        </el-menu-item>
          <el-menu-item index="" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区域 -->
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-info">
            <span class="welcome-text">欢迎回来，{{ username }}</span>
            <el-avatar :size="36" :icon="UserFilled" />
          </div>
        </el-header>
        
        <!-- 页面内容 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { DataAnalysis, Document, SwitchButton, UserFilled, List } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    DataAnalysis,
    Document,
    SwitchButton,
    UserFilled,
    List
  },
  data() {
    return {
      activeMenu: '/home/analytics',
      username: '用户'
    }
  },
  created() {
    // 从localStorage获取用户名（如果有）
    const savedUsername = localStorage.getItem('username')
    if (savedUsername) {
      this.username = savedUsername
    }
    
    // 设置当前激活的菜单项
    this.setActiveMenu()
  },
  methods: {
    handleMenuSelect(key) {
      this.activeMenu = key
    },
    handleLogout() {
      // 清除登录状态
      localStorage.removeItem('isLoggedIn')
      localStorage.removeItem('username')
      // 跳转到登录页
      this.$router.push('/login')
    },
    setActiveMenu() {
      const currentPath = this.$route.path
      if (currentPath.includes('/home/')) {
        this.activeMenu = currentPath
      }
    }
  },
  watch: {
    $route() {
      this.setActiveMenu()
    }
  }
}
</script>

<style scoped>
.main-container {
  height: 100vh;
  overflow: hidden;
}

.container {
  height: 100vh;
}

.sidebar {
  background-color: #2c3e50;
  color: #ecf0f1;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  position: relative;
}

.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #34495e;
  background-color: #34495e;
}

.sidebar-header h2 {
  color: #ffffff;
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.el-menu-vertical {
  border-right: none;
  background-color: transparent;
}

.el-menu-vertical .el-menu-item {
  color: #bdc3c7;
  height: 60px;
  line-height: 60px;
  font-size: 16px;
  background-color: transparent;
  transition: all 0.3s ease;
}

.el-menu-vertical .el-menu-item:hover {
  background-color: #34495e;
  color: #ffffff;
}

.el-menu-vertical .el-menu-item.is-active {
  background-color: #409eff;
  color: #ffffff;
}

.el-menu-vertical .el-menu-item i {
  margin-right: 10px;
}

.header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  color: #606266;
  font-size: 14px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
  height: calc(100vh - 60px);
}
</style>