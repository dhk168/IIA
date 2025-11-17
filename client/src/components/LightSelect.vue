<template>
  <el-select
    :model-value="modelValue"
    :placeholder="placeholder"
    :clearable="clearable"
    :disabled="disabled"
    :multiple="multiple"
    :size="size"
    @update:model-value="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
    class="glass-filter"
    :class="className"
    popper-class="glass-popper"
  >
    <el-option
      v-for="option in options"
      :key="option.value"
      :label="option.label"
      :value="option.value"
    ></el-option>
  </el-select>
</template>

<script>
export default {
  name: 'LightSelect',
  props: {
    modelValue: {
      type: [String, Number, Array],
      default: () => (this.multiple ? [] : '')
    },
    placeholder: {
      type: String,
      default: ''
    },
    clearable: {
      type: Boolean,
      default: true
    },
    options: {
      type: Array,
      default: () => []
    },
    disabled: {
      type: Boolean,
      default: false
    },
    multiple: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: ''
    },
    className: {
      type: String,
      default: ''
    }
  },
  emits: ['update:model-value', 'change', 'focus', 'blur'],
  methods: {
    handleChange(value) {
      this.$emit('update:model-value', value)
      this.$emit('change', value)
    },
    handleFocus(event) {
      this.$emit('focus', event)
    },
    handleBlur(event) {
      this.$emit('blur', event)
    }
  }
}
</script>

<style>
.glass-filter {
  width: 100%;
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(25px) !important;
  -webkit-backdrop-filter: blur(25px) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 14px !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2) !important;
  padding: 4px;
  margin-bottom: 16px;
  transition: all 0.3s ease !important;
}

/* 重置所有可能的背景相关CSS变量 */
.glass-filter {
  --el-bg-color: transparent !important;
  --el-bg-color-page: transparent !important;
  --el-bg-color-overlay: transparent !important;
  --el-fill-color-blank: transparent !important;
  --el-fill-color-light: transparent !important;
  --el-input-bg-color: transparent !important;
}

/* 移除Element UI内部组件的白色边框 */
.glass-filter .el-input__wrapper,
.glass-filter .el-select__wrapper,
/* 处理鼠标悬停状态 */
.glass-filter .el-input__wrapper:hover,
.glass-filter .el-select__wrapper:hover {
  border: none !important;
  --el-border-color: transparent !important;
  --el-border-color-light: transparent !important;
  box-shadow: none !important;
  --el-box-shadow: none !important;
  background-color: transparent !important;
}

/* 移除输入框的边框 */
.glass-filter .el-input__inner,
.glass-filter .el-select__input {
  border: none !important;
  outline: none !important;
}

/* 确保输入框内部完全透明 */
.glass-filter .el-input__wrapper {
  background-color: transparent !important;
  backdrop-filter: none !important;
  -webkit-backdrop-filter: none !important;
  border: none !important;
  box-shadow: none !important;
  border-radius: 8px !important;
}

.glass-filter .el-input__inner {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

/* 确保下拉箭头颜色正确 */
.glass-filter .el-input__suffix-inner {
  color: rgba(255, 255, 255, 0.7) !important;
}

/* 移除Element UI选择器内部白色背景 */
.glass-filter .el-input {
  background: transparent !important;
}

/* 更具体地覆盖Element UI输入包装器样式 */
.glass-filter .el-input__wrapper,
.glass-filter .el-select__wrapper {
  background-color: transparent !important;
  --el-input-bg-color: transparent !important;
  --el-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
  background-image: none !important;
  background-clip: padding-box !important;
}

/* 确保输入框内部完全透明 */
.glass-filter .el-input__inner,
.glass-filter .el-select__input {
  background: transparent !important;
  --el-input-bg-color: transparent !important;
  background-image: none !important;
}

/* 覆盖选择器的所有可能背景 */
.glass-filter .el-select,
.glass-filter .el-select--default,
.glass-filter .el-select--single {
  background: transparent !important;
  --el-bg-color: transparent !important;
}

/* 处理选择器聚焦状态 */
.glass-filter:focus-within {
  background: rgba(255, 255, 255, 0.25) !important;
  border-color: rgba(255, 255, 255, 0.45) !important;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.15) !important;
  transform: translateY(-2px) !important;
  box-shadow: 0 16px 50px rgba(0, 0, 0, 0.25) !important;
}

/* 确保日期选择器的输入框也正确显示 */
.glass-filter .el-date-editor .el-input__inner {
  color: rgba(255, 255, 255, 0.2) !important;
}

/* 为选择器下拉面板添加毛玻璃效果 */
.glass-popper {
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(25px) !important;
  -webkit-backdrop-filter: blur(25px) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 14px !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.25) !important;
  z-index: 15000 !important;
}

/* 确保下拉选项为纯白色 */
/* .glass-popper .el-option {
  background: #FFAA00 !important;
  backdrop-filter: none !important;
  -webkit-backdrop-filter: none !important;
  color: #333333 !important;
  border: 1px solid #e0e0e0 !important;
  border-radius: 10px !important;
  margin-bottom: 5px !important;
  padding: 12px 14px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  cursor: pointer !important;
} */

/* 处理下拉选项的悬停状态 */
/* .glass-popper .el-option:hover {
  background: #2e7d32 !important;
  color: #8e1111 !important;
  transform: translateX(6px) !important;
  border-color: #bdbdbd !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
} */

/* 处理下拉选项的选中状态 */
/* .glass-popper .el-option.selected {
  background: #e8f5e9 !important;
  color: #2e7d32 !important;
  border-color: #c8e6c9 !important;
} */

/* 移除下拉面板的内边距以保持样式统一 */
/* .glass-popper .el-select__options {
  padding: 8px !important;
} */

/* 为选中的选项添加样式 */
/* .glass-popper .el-option.is-selected {
  background: #e8f5e9 !important;
  color: #2e7d32 !important;
  border: 1px solid #c8e6c9 !important;
  border-radius: 10px !important;
  font-weight: 500 !important;
} */
</style>