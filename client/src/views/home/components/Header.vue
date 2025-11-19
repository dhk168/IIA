<template>
  <LightDiv class="header">
    <div class="header-left">
      <h1 class="page-title">{{ currentPageTitle }}</h1>
    </div>
    <div class="header-right">
      <div class="welcome-text">
        Welcome, {{ username }}
      </div>
      <!-- 使用新的玻璃态下拉菜单组件 -->
      <GlassDropdown>
        <template #trigger>
          <el-avatar :size="50" :icon="UserFilled" class="user-avatar" />
        </template>
        <template #menu>
          <div class="custom-dropdown-item">Profile</div>
          <div class="custom-dropdown-item" @click="handleLogout">Logout</div>
        </template>
      </GlassDropdown>
    </div>
  </LightDiv>
</template>

<script>
import { UserFilled } from '@element-plus/icons-vue'
import { authAPI } from '@/api/auth'
import LightDiv from '@/components/LightDiv.vue'
import GlassDropdown from './GlassDropdown.vue'

export default {
  name: 'Header',
  components: {
    UserFilled,
    LightDiv,
    GlassDropdown
  },
  props: {
    username: {
      type: String,
      default: 'User'
    },
    currentPageTitle: {
      type: String,
      default: 'Blank Page'
    }
  },
  data() {
    return {
    };
  },
  methods: {
    handleLogout() {
      // 清除localStorage中的用户信息
      localStorage.removeItem('username')
      localStorage.removeItem('isLoggedIn')
      // 重定向到登录页面
      this.$router.push('/login')
    },
    
  },
  mounted() {
  },
  beforeUnmount() {
  }
}
</script>

<style scoped>
/* 顶部导航样式 */
.header {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 16px;
  height: 88px;
  border-radius: 16px;
  margin: 16px 16px 8px 16px;
  border: 1px solid rgba(255, 255, 255, 0.15);
  position: relative;
  z-index: 100;
}

.header-left {
  flex: 0 0 auto;
}

.header-left .page-title {
  margin: 0;
  color: #ffffff;
  font-size: 28px;
  font-weight: 700;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  white-space: nowrap;
}

.header-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 24px;
  position: relative;
  z-index: 200;
}

.welcome-text {
  color: #ffffff;
  font-size: 16px;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  white-space: nowrap;
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





/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 0 16px;
    height: 72px;
    margin: 12px 12px 6px 12px;
  }
  
  .header-left .page-title {
    font-size: 24px;
  }
}
</style>