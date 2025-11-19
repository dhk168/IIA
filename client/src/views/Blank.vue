<template>
  <div class="blank-container">
    <div class="debug-section">
      <h2>调试弹窗测试</h2>
      <div class="debug-buttons">
        <button class="debug-button" @click="showDebugMessage('success')">成功提示</button>
        <button class="debug-button" @click="showDebugMessage('error')">错误提示</button>
        <button class="debug-button" @click="showDebugMessage('warning')">警告提示</button>
        <button class="debug-button" @click="showDebugMessage('info')">信息提示</button>
        <button class="debug-button" @click="showDebugMessage('email')">邮件发送提示</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';

export default {
  name: 'Blank',
  inject: ['showToast'],
  methods: {
    showDebugMessage(type) {
      if (type === 'success') {
        this.showToast('success', '这是一个成功提示示例');
      } else if (type === 'error') {
        this.showToast('error', '这是一个错误提示示例');
      } else if (type === 'warning') {
        this.showToast('warning', '这是一个警告提示示例');
      } else if (type === 'info') {
        this.showToast('info', '这是一个信息提示示例');
      } else if (type === 'email') {
        this.showToast('success', '已发送邮件到您的邮箱，请查收验证码');
      }
    }
  }
}
</script>

<style scoped>
.blank-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.debug-section {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.debug-section h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.debug-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
}

.debug-button {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f5f5f5;
  color: #333;
  margin: 5px;
}

.debug-button:hover {
  background: #e0e0e0;
  transform: translateY(-2px);
}

/* 响应式调整 */
@media (max-width: 600px) {
  .debug-buttons {
    flex-direction: column;
    width: 100%;
  }
  
  .debug-button {
    margin: 5px 0;
    width: 100%;
  }
}
</style>