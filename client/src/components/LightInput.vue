<template>
  <el-input
    :model-value="modelValue"
    :type="type"
    :placeholder="placeholder"
    :disabled="disabled"
    :readonly="readonly"
    :autocomplete="autocomplete"
    @update:model-value="handleInput"
    @focus="handleFocus"
    @blur="handleBlur"
    :class="['light-input', className]"
  ></el-input>
</template>

<script>
export default {
  name: 'LightInput',
  props: {
    modelValue: {
      type: [String, Number],
      default: ''
    },
    type: {
      type: String,
      default: 'text'
    },
    placeholder: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    autocomplete: {
      type: String,
      default: 'off'
    },
    className: {
      type: String,
      default: ''
    }
  },
  emits: ['update:model-value', 'focus', 'blur'],
  methods: {
    handleInput(value) {
      this.$emit('update:model-value', value)
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

<style scoped>
.light-input-container {
  width: 100% !important;
  background-color: transparent;
  /* background-color: rgb(255,0,0) !important; */
  margin: 0 !important;
  padding: 0 !important;
}

/* Target all layers of Element Plus input component */
.light-input {
  width: 100% !important;
  background-color: transparent !important;
  display: block !important;
}

.light-input :deep(*) {
  background-color: transparent !important;
  border-color: transparent !important;
  box-shadow: none !important;
}

.light-input :deep(.el-input__wrapper) {
  width: 100% !important;
  padding: 0 !important;
}

.light-input :deep(.el-input__inner) {
  width: 100%;
  height: 48px;
  padding: 0 16px;
  font-size: 16px !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  border-radius: 8px !important;
  background-color: rgba(255, 255, 255, 0.1) !important;
  color: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(10px) !important;
  transition: all 0.3s ease !important;
}

.light-input :deep(.el-input__inner:hover) {
  border-color: rgba(255 255, 255, 0.4);
  background-color: rgba(255, 255, 255, 0.15);
}

.light-input :deep(.el-input__inner:focus) {
  outline: none;
  border-color: rgba(255, 255, 255, 0.6);
  background-color: rgba(255, 255, 255, 0.2);
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.1);
}

.light-input :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.5);
}

.light-input :deep(.el-input__inner:disabled),
.light-input :deep(.el-input__inner[readonly]) {
  background-color: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.6);
  cursor: not-allowed;
}
</style>