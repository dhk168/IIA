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
      class="custom-option"
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

.el-select__input {
  color: rgb(0, 240, 240) !important;
}

.el-select__selected-item.el-select__placeholder.is-transparent {
  color: rgb(210, 210, 210) !important;
}

.el-select__selected-item.el-select__placeholder {
  color: rgb(255, 255, 255) !important;
}

/* 移除Element UI内部组件的白色边框 */
.glass-filter .el-input__wrapper,
.glass-filter .el-select__wrapper,
.glass-filter .el-input__wrapper:hover,
.glass-filter .el-select__wrapper:hover {
  border: none !important;
  --el-border-color: transparent !important;
  --el-border-color-light: transparent !important;
  box-shadow: none !important;
  --el-box-shadow: none !important;
  background-color: transparent !important;
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

/* 处理下拉选项的悬停状态 */
.el-select-dropdown__item.is-hovering.custom-option {
  background-color: rgba(255, 255, 255, 0.4) !important;
  color: rgb(255, 255, 255) !important;
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.3) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-select-dropdown__item.custom-option {
  color: rgb(230, 230, 230) !important;
}

.el-select-dropdown__item.is-selected.custom-option {
  color: rgb(100, 100, 245) !important;
  background-color: rgba(255, 255, 255, 0.6) !important;
}

</style>