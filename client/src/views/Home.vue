<template>
  <div class="main-container">
    <!-- 波浪呼吸效果背景层 -->
    <div class="home-background"></div>
    
    <div class="layout-wrapper">
      <!-- 侧边栏 -->
      <el-aside width="80px" class="sidebar">
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
            <el-icon class="menu-icon"><Document /></el-icon>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 右侧内容区域 -->
      <div class="right-content">
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <h1 class="page-title">{{ currentPageTitle }}</h1>
          </div>
          <div class="header-right">
            <el-dropdown>
              <el-avatar :size="40" :icon="UserFilled" class="user-avatar" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>Profile</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">Logout</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 页面内容 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </div>
    </div>
  </div>
</template>

<script>
import { Document, UserFilled } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    Document,
    UserFilled
  },
  data() {
    return {
      activeMenu: '/home/projects',
      username: 'User'
    }
  },
  computed: {
    currentPageTitle() {
      const path = this.$route.path
      if (path.includes('/blank')) return 'Blank Page'
      if (path.includes('/projects')) return 'Project Plan'
      // if (path.includes('/tasks')) return 'Tasks'
      // if (path.includes('/analytics')) return 'Analytics'
      return 'Blank Page'
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
    setActiveMenu() {
      const currentPath = this.$route.path
      if (currentPath.includes('/home/')) {
        this.activeMenu = currentPath
      }
    },
    handleLogout() {
      // 清除localStorage中的用户信息
      localStorage.removeItem('username')
      localStorage.removeItem('isLoggedIn')
      // 重定向到登录页面
      this.$router.push('/login')
    }
  },
  watch: {
    $route() {
      this.setActiveMenu()
    }
  }
}
</script>

<style>
/* 导入可复用的背景样式 */
@import '../assets/styles/background.css';
</style>

<style scoped>
/* 主容器样式 */
.main-container {
  height: 100vh;
  overflow: hidden;
  position: relative;
  width: 100%;
}

/* 呼吸效果背景容器 */
.home-background {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
  overflow: hidden;
  background: linear-gradient(
    135deg, 
    #1e1b4b 0%, 
    #312e81 25%, 
    #4338ca 50%, 
    #3b82f6 75%, 
    #60a5fa 100%
  );
  background-size: 400% 400%;
  animation: oceanGradient 15s ease-in-out infinite;
}

.layout-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
}

.right-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

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
  width: 56px;
  height: 56px;
  line-height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  transition: all 0.3s ease;
  border-radius: 50%;
  margin: 12px auto;
  color: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.el-menu-vertical .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.2);
  color: #ffffff;
  backdrop-filter: blur(12px);
  border-color: rgba(255, 255, 255, 0.25);
}

.el-menu-vertical .el-menu-item.is-active {
  background: rgba(91, 33, 182, 0.25);
  color: #ffffff;
  backdrop-filter: blur(12px);
  border-radius: 50%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.menu-icon {
  font-size: 24px;
}

/* 顶部导航样式 */
.header {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88px;
  border-radius: 16px;
  margin: 16px 16px 8px 16px;
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.header-left .page-title {
  margin: 0;
  color: #ffffff;
  font-size: 28px;
  font-weight: 700;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.user-avatar {
  cursor: pointer;
  border: 3px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
}

.user-avatar:hover {
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

/* 主内容区域 */
.main-content {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  padding: 32px;
  overflow-y: auto;
  flex: 1;
  border-radius: 16px;
  margin: 8px 16px 16px 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
}

/* 自定义滚动条 */
.main-content::-webkit-scrollbar {
  width: 8px;
}

.main-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 4px;
  transition: background 0.3s ease;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}

/* Element Plus 组件样式覆盖 */
.el-dropdown-menu {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.el-dropdown-menu__item {
  color: #333;
  transition: all 0.3s ease;
}

.el-dropdown-menu__item:hover {
  background: rgba(91, 33, 182, 0.1);
  color: #5b21b6;
}

.el-dropdown-menu__item--divided {}

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
  
  .header {
    padding: 0 16px;
    height: 72px;
    margin: 12px 12px 6px 12px;
  }
  
  .header-left .page-title {
    font-size: 24px;
  }
  
  .main-content {
    padding: 16px;
    height: calc(100vh - 90px);
    margin: 6px 12px 12px 12px;
  }
}
</style>