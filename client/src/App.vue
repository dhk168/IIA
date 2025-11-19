<template>
  <div class="app-container">
    <!-- Background effect container - only shown on non-authentication pages -->
    <div v-if="!isAuthPage" class="app-background"></div>
    
    <!-- Route view -->
    <router-view />
    
    <!-- Global LightToast component -->
    <LightToast
      :visible="toastVisible"
      :message="toastMessage"
      :type="toastType"
      @close="toastVisible = false"
    />
  </div>
</template>

<script>
import { ref, watch, provide } from 'vue'
import { useRoute } from 'vue-router'
import LightToast from './components/LightToast.vue'

export default {
  name: 'App',
  components: {
    LightToast
  },
  setup() {
    const route = useRoute()
    const isAuthPage = ref(false)
    
    // Global toast state
    const toastVisible = ref(false)
    const toastMessage = ref('')
    const toastType = ref('info')
    
    // Method to show toast
    const showToast = (type, message) => {
      toastType.value = type
      toastMessage.value = message
      toastVisible.value = true
    }
    
    // Provide toast method to all child components
    provide('showToast', showToast)
    
    // Check if current page is authentication page (login, register, etc.)
    const checkIfAuthPage = () => {
      const authPaths = ['/login', '/register', '/forgot-password']
      isAuthPage.value = authPaths.includes(route.path)
    }
    
    // Watch for route changes
    watch(() => route.path, () => {
      checkIfAuthPage()
    }, { immediate: true })
    
    return {
      isAuthPage,
      toastVisible,
      toastMessage,
      toastType
    }
  }
}
</script>

<style>
.app-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}
</style>
