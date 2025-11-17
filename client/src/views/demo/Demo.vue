<template>
    <!-- Light Button Demo -->
    <div class="demo-section">
      <h2 class="demo-section-title">Light Button</h2>
      <div class="demo-button-group">
        <LightButton>Primary Button</LightButton>
        <LightButton disabled>Disabled Button</LightButton>
      </div>
    </div>
    
    <!-- Glass Dialog -->
    <el-dialog
      v-model="showDialog"
      title="Glass Dialog"
      class="glass-dialog"
    >
      <p>This is a glass-style dialog with Element UI overrides.</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDialog = false">Cancel</el-button>
          <el-button type="primary" @click="showDialog = false">Confirm</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Light Toast Demo -->
    <div class="demo-section">
      <h2 class="demo-section-title">Light Toast</h2>
      <div class="demo-button-group">
        <LightButton @click="showSuccessToast">Success Toast</LightButton>
        <LightButton @click="showErrorToast">Error Toast</LightButton>
        <LightButton @click="showWarningToast">Warning Toast</LightButton>
        <LightButton @click="showInfoToast">Info Toast</LightButton>
      </div>
    </div>

    <!-- Light Toast Component -->
    <LightToast
      :visible="toastVisible"
      :message="toastMessage"
      :type="toastType"
      @close="toastVisible = false"
    />

</template>

<script>
import LightButton from '@/components/LightButton.vue'
import LightToast from '@/components/LightToast.vue'
import { ElButton, ElInput, ElDialog } from 'element-plus'
import 'element-plus/dist/index.css'

// Import custom component styles
import '@/assets/styles/components/el-dialog-glass.css'
import '@/assets/styles/components/light-toast.css'
import '@/assets/styles/components/glass-toast.css'

// Import page styles
import '@/assets/styles/page/auth.css'

export default {
  name: 'Demo',
  components: {
    LightButton,
    LightToast,
    ElButton,
    ElInput,
    ElDialog
  },
  data() {
    return {
      showDialog: false,
      toastVisible: false,
      toastMessage: '',
      toastType: ''
    }
  },
  methods: {
    handleButtonClick() {
      alert('Button clicked!')
    },
    showSuccessToast() {
      this.toastMessage = 'Operation completed successfully!'
      this.toastType = 'success'
      this.toastVisible = true
    },
    showErrorToast() {
      this.toastMessage = 'An error occurred. Please try again.'
      this.toastType = 'error'
      this.toastVisible = true
    },
    showWarningToast() {
      this.toastMessage = 'Warning! Proceed with caution.'
      this.toastType = 'warning'
      this.toastVisible = true
    },
    showInfoToast() {
      this.toastMessage = 'This is an informational message.'
      this.toastType = 'info'
      this.toastVisible = true
    }
  }
}
</script>

<style scoped>
.demo-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.demo-title {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
  font-size: 2rem;
  font-weight: 700;
}

.demo-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: transparent;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.demo-section-title {
  color: #fff;
  margin-bottom: 1rem;
  font-size: 1.2rem;
  font-weight: 600;
}

.demo-button-group {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

.demo-input {
  margin-top: 1rem;
  width: 300px;
}
</style>