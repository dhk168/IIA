<template>
  <LightDiv class="header">
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
  </LightDiv>
</template>

<script>
import { UserFilled } from '@element-plus/icons-vue'
import { authAPI } from '@/api/auth'
import LightDiv from '@/components/LightDiv.vue'

export default {
  name: 'Header',
  components: {
    UserFilled,
    LightDiv
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
      dropdownVisible: false
    }
  },
  methods: {
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

/* 自定义下拉菜单样式 */
.custom-dropdown {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  cursor: pointer;
  /* 确保父容器不会限制下拉菜单的层级 */
  z-index: 300;
}

.custom-dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  right: -20px;
  min-width: 180px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  /* 增加z-index确保显示在最顶层 - 使用更高的值避免被任何元素覆盖 */
  z-index: 2147483647;
  /* 最大安全z-index值 */
  /* 为箭头留出空间 */
  padding-top: 8px;
}

/* 毛玻璃背景 - 进一步增加不透明度使其更加清晰可见 */
.custom-dropdown-menu::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  z-index: -1;
}

/* 添加箭头指示器 - 与菜单背景不透明度保持一致 */
.custom-dropdown-menu::after {
  content: '';
  position: absolute;
  top: -8px;
  right: 40px;
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 8px solid rgba(255, 255, 255, 0.7);
  /* 箭头边框 */
  filter: drop-shadow(0 -1px 0 rgba(255, 255, 255, 0.2));
}

.custom-dropdown-item {
  padding: 12px 20px;
  color: #333333;
  /* 将文字颜色改为黑色，提高在浅色背景上的可读性 */
  font-weight: bold;
  /* 文字加粗 */
  text-align: center;
  /* 文字居中 */
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  /* 边框也改为深色，与深色文字协调 */
}

.custom-dropdown-item:last-child {
  border-bottom: none;
}

.custom-dropdown-item:hover {
  background: rgba(0, 0, 0, 0.08);
  /* 悬停背景改为深色半透明，与黑色文字协调 */
  transform: translateX(4px);
}

/* 下拉菜单动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
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