<template>
  <button type="button" :class="['auth-button', className, submit && 'submit-button']" :disabled="disabled" @click="handleClick">
    <slot></slot>
  </button>
</template>

<script>
export default {
  name: 'AuthButton',
  props: {
    disabled: {
      type: Boolean,
      default: false
    },
    className: {
      type: String,
      default: ''
    },
    submit: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleClick(event) {
      if (!this.disabled) {
        this.$emit('click', event);
      }
    }
  },
  emits: ['click']
}
</script>

<style scoped>
/* 通用认证按钮样式 */
.auth-button {
  height: 48px;
  padding: 0 16px;
  min-width: 120px;
  background-color: #3b82f6;
  border: none;
  border-radius: 8px;
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  outline: none;
}

/* 悬停状态 */
.auth-button:hover:not(:disabled) {
  background-color: #2563eb;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
  transform: translateY(-1px);
}

/* 点击状态 */
.auth-button:active:not(:disabled) {
  transform: translateY(0);
  background-color: #1d4ed8;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 禁用状态 */
.auth-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* 提交按钮样式（用于表单提交） */
.auth-button.submit-button {
  width: 100%;
  margin-top: 8px;
  margin-bottom: 24px;
}
</style>