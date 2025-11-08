<template>
  <div class="app-container">
    <!-- 背景效果容器 - 仅在非认证页面显示 -->
    <div v-if="!isAuthPage" class="app-background"></div>
    
    <!-- 路由视图 -->
    <router-view />
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

export default {
  name: 'App',
  setup() {
    const route = useRoute()
    const isAuthPage = ref(false)
    
    // 检测当前是否为认证页面（登录、注册等）
    const checkIfAuthPage = () => {
      const authPaths = ['/login', '/register', '/forgot-password']
      isAuthPage.value = authPaths.includes(route.path)
    }
    
    // 监听路由变化
    watch(() => route.path, () => {
      checkIfAuthPage()
    }, { immediate: true })
    
    return {
      isAuthPage
    }
  }
}
</script>

<style>
/* 导入可复用的背景样式 */
@import './assets/styles/background.css';

.app-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}
</style>
