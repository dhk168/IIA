<template>
  <el-dialog
    v-model="dialogVisible"
    :title="title"
    :width="width"
    :close-on-click-modal="closeOnClickModal"
    :close-on-press-escape="closeOnPressEscape"
    :show-close="showClose"
    class="light-dialog"
    @close="handleClose"
  >
    <slot></slot>
    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'LightDialog',
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '500px'
    },
    closeOnClickModal: {
      type: Boolean,
      default: true
    },
    closeOnPressEscape: {
      type: Boolean,
      default: true
    },
    showClose: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      dialogVisible: this.modelValue
    }
  },
  watch: {
    modelValue: {
      handler(newVal) {
        this.dialogVisible = newVal
      },
      immediate: true
    },
    dialogVisible: {
      handler(newVal) {
        this.$emit('update:model-value', newVal)
      },
      immediate: true
    }
  },
  emits: ['update:model-value', 'close'],
  methods: {
    handleClose() {
      this.$emit('close')
    }
  }
}
</script>

<style>
.el-dialog.light-dialog {
  box-sizing: border-box; /* 确保padding和border包含在width内 */
  padding: 20px; /* 调整内边距 */

  /* Apply project color border */
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37), 0 0 0 1px rgba(255, 255, 255, 0.2) !important;
  /* Set light white background */
  background: rgba(50, 50, 95, 0.8) !important;
  /* 玻璃效果 - 增强模糊度 */
  backdrop-filter: blur(30px) saturate(1.2) !important;
  -webkit-backdrop-filter: blur(30px) saturate(1.2) !important;
  /* 确保弹窗容器是相对定位 */
  position: relative;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 14px !important;
  
  .el-dialog__header {
    border-bottom: none !important;
  }
  .el-dialog__title {
    color: rgba(255, 255, 255, 0.9) !important;
  }
  .el-dialog__body {
    color: rgba(255, 255, 255, 0.8) !important;
  }
  .el-form-item__label {
    color: rgba(255, 255, 255, 0.9) !important;
  }
  .el-form-item__content {
    background-color: transparent !important;
  }
  .el-input{
    .el-input__wrapper {
      background: rgb(0, 0, 0, 0.3) !important;
      .el-input__inner {
        color: rgba(255, 255, 255, 0.9) !important;
      }
    }
  }
  .el-textarea{
    .el-textarea__inner {
      background: rgb(0, 0, 0, 0.3) !important;
      color: rgba(255, 255, 255, 0.9) !important;
    }
  }
  .el-select .el-select__wrapper{
    background: rgb(0, 0, 0, 0.3) !important;
  }
  .v3ip__custom-select .v3ip__selected,
  .v3ip__custom-select div,
  .v3ip__custom-select input {
    background: rgb(0, 0, 0, 0.3) !important;
    color: rgba(255, 255, 255, 0.9) !important;
  }
  .el-dialog__footer {
    border-top: none !important;
  }
  .el-dialog__close {
    color: rgba(255, 255, 255, 0.7) !important;
  }
  /* Footer button styles */
  .el-dialog__footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  .el-dialog__footer .el-button {
    background-color: rgba(255, 255, 255, 0.2) !important;
    border-color: rgba(255, 255, 255, 0.3) !important;
    color: rgba(255, 255, 255, 0.9) !important;
    backdrop-filter: blur(10px) !important;
    -webkit-backdrop-filter: blur(10px) !important;
  }
  .el-dialog__footer .el-button:hover {
    background-color: rgba(255, 255, 255, 0.3) !important;
    border-color: rgba(255, 255, 255, 0.4) !important;
  }
  .el-dialog__footer .el-button:active {
    background-color: rgba(255, 255, 255, 0.4) !important;
  }
  .el-dialog__footer .el-button--primary {
    background-color: #1890ff !important;
    border-color: #1890ff !important;
    color: #fff !important;
  }
  .el-dialog__footer .el-button--primary:hover {
    background-color: #40a9ff !important;
    border-color: #40a9ff !important;
  }
  .el-dialog__footer .el-button--primary:active {
    background-color: #096dd9 !important;
  }
}

/* 渐变动画定义 */
@keyframes oceanGradient {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}
</style>