<template>
  <div class="back-div gradient-background" :class="className">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'BackDiv',
  props: {
    className: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
/* Ocean wave animation */
@keyframes oceanGradient {
  0% { background-position: 0% 50%; }
  25% { background-position: 50% 60%; }
  50% { background-position: 100% 50%; }
  75% { background-position: 50% 40%; }
  100% { background-position: 0% 50%; }
}

.back-div {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
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
  /* Default flexbox styles for content centering */
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  color: #ffffff;
}

/* 响应式样式 - 调整移动设备上的内边距 */
@media (max-width: 480px) {
  .back-div {
    padding: 15px;
  }
}

/* 非常小的屏幕 - 进一步减小内边距 */
@media (max-width: 360px) {
  .back-div {
    padding: 10px;
  }
}
</style>