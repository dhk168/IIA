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

/* 应用背景 */
.app-background {
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
</style>
