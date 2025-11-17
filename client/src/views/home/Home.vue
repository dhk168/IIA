<template>
  <BackDiv class="main-container home-container">
    <div class="layout-wrapper">
      <!-- 侧边栏 -->
      <SideBar :active-menu="activeMenu" @menu-select="handleMenuSelect" />
      <!-- 右侧内容区域 -->
      <div class="right-content">
        <!-- 顶部导航 -->
        <Header :username="username" :current-page-title="currentPageTitle" />
        <!-- 页面内容 -->
        <LightDiv class="main-content">
          <el-main>
            <router-view />
          </el-main>
        </LightDiv>
      </div>
    </div>
  </BackDiv>
</template>

<script>
import { authAPI } from '@/api/auth'
import LightDiv from '@/components/LightDiv.vue'
import BackDiv from '@/components/BackDiv.vue'
import SideBar from './components/SideBar.vue'
import Header from './components/Header.vue'

export default {
  name: 'Home',
  components: {
    LightDiv,
    BackDiv,
    SideBar,
    Header
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
      if (path.includes('/projects')) return 'Projects'
      if (path.includes('/tasks')) return 'Tasks'
      if (path.includes('/analytics')) return 'Analytics'
      if (path.includes('/debug')) return 'Debug'
      if (path.includes('/demo')) return 'Demo'
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

  },
  watch: {
    $route() {
      this.setActiveMenu()
    }
  },

}
</script>

<style scoped>
@import '@/assets/styles/page/home.css';
</style>