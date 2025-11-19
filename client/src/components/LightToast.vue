<template>
  <div v-if="visible" :class="['glass-toast', type]" @click="close">
    {{ message }}
  </div>
</template>

<script>
export default {
  name: 'LightToast',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    message: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'info', // success, error, warning, info
      validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
    },
    duration: {
      type: Number,
      default: 3000
    }
  },
  data() {
    return {
      timer: null
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.startTimer()
      } else {
        this.clearTimer()
      }
    }
  },
  methods: {
    startTimer() {
      this.clearTimer()
      if (this.duration > 0) {
        this.timer = setTimeout(() => {
          this.$emit('close')
        }, this.duration)
      }
    },
    clearTimer() {
      if (this.timer) {
        clearTimeout(this.timer)
        this.timer = null
      }
    },
    close() {
      this.$emit('close')
    }
  },
  beforeUnmount() {
    this.clearTimer()
  }
}
</script>

<style scoped>
/* Frosted glass effect toast style */

/* Basic frosted glass style */
.glass-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 16px 24px;
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 9999;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.5s ease;
  min-width: 300px;
  opacity: 1;
  transform: translateX(0);
  cursor: pointer;
}

/* Success toast style */
.glass-toast.success {
  background: rgba(72, 187, 120, 0.6);
  color: #1e5c3e;
  border-color: rgba(72, 187, 120, 0.4);
}

.glass-toast.success:hover {
  background: rgba(72, 187, 120, 0.5);
  transform: translateY(-2px);
}

/* Error toast style */
.glass-toast.error {
  background: rgba(245, 108, 108, 0.4);
  color: #a51e1e;
  border-color: rgba(245, 108, 108, 0.4);
}

.glass-toast.error:hover {
  background: rgba(245, 108, 108, 0.5);
  transform: translateY(-2px);
}

/* Warning toast style */
.glass-toast.warning {
  background: rgba(247, 183, 49, 0.4);
  color: #8a4c13;
  border-color: rgba(247, 183, 49, 0.4);
}

.glass-toast.warning:hover {
  background: rgba(247, 183, 49, 0.5);
  transform: translateY(-2px);
}

/* Info toast style */
.glass-toast.info {
  background: rgba(64, 158, 255, 0.4);
  color: #104c83;
  border-color: rgba(64, 158, 255, 0.4);
}

.glass-toast.info:hover {
  background: rgba(64, 158, 255, 0.5);
  transform: translateY(-2px);
}

/* Responsive design */
@media (max-width: 768px) {
  .glass-toast {
    top: 10px;
    right: 10px;
    left: 10px;
    min-width: auto;
    font-size: 14px;
  }
}
</style>