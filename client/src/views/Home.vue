<template>
  <div class="main-container home-container">
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
            <div class="welcome-text">
              Welcome, {{ username }}
            </div>
            <!-- 自定义下拉菜单 -->
              <div class="custom-dropdown" @click="toggleDropdown">
                <el-avatar :size="50" :icon="UserFilled" class="user-avatar" />
                <!-- 自定义下拉菜单内容 -->
                <transition name="dropdown">
                  <div v-if="dropdownVisible" class="custom-dropdown-menu">
                    <div class="custom-dropdown-item" @click="dropdownVisible = false">Profile</div>
                    <div class="custom-dropdown-item" @click="handleLogout">Logout</div>
                  </div>
                </transition>
              </div>
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
import { authAPI } from '../api/auth'

export default {
  name: 'Home',
  components: {
    Document, UserFilled
  },
  data() {
    return {
      activeMenu: '/home/projects',
      username: 'User',
      dropdownVisible: false
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
    // 尝试从localStorage获取用户名（如果有）
    const savedUsername = localStorage.getItem('username')
    if (savedUsername) {
      this.username = savedUsername
    }
    
    // 调用API获取真实用户信息
    this.fetchUserProfile()
    
    // 设置当前激活的菜单项
    this.setActiveMenu()
  },
  methods: {
    // 获取用户个人信息
    async fetchUserProfile() {
      try {
        const response = await authAPI.getCurrentUser()
        if (response.data && response.data.userName) {
          this.username = response.data.userName
          // 存储到localStorage以便下次使用
          localStorage.setItem('username', response.data.userName)
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 失败时尝试使用localStorage中的usernameOrEmail（可能是邮箱）
        const usernameOrEmail = localStorage.getItem('usernameOrEmail')
        if (usernameOrEmail) {
          // 如果是邮箱，只显示@前面的部分
          if (usernameOrEmail.includes('@')) {
            this.username = usernameOrEmail.split('@')[0]
          } else {
            this.username = usernameOrEmail
          }
        }
      }
    },
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
    },
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible
    },
    // 点击外部关闭下拉菜单
    closeDropdownOnClickOutside(event) {
      const dropdown = this.$el.querySelector('.custom-dropdown')
      if (dropdown && !dropdown.contains(event.target)) {
        this.dropdownVisible = false
      }
    }
  },
  watch: {
    $route() {
      this.setActiveMenu()
    }
  },
  mounted() {
    // 添加点击外部关闭下拉菜单的事件监听
    document.addEventListener('click', this.closeDropdownOnClickOutside)
  },
  beforeUnmount() {
    // 移除事件监听
    document.removeEventListener('click', this.closeDropdownOnClickOutside)
  }
}
</script>

<style scoped>
/* 导入页面级样式文件 */
@import '../assets/styles/page/home.css';
</style>