<template>
  <el-date-picker
    :model-value="modelValue"
    :type="type"
    :placeholder="placeholder"
    :clearable="clearable"
    :disabled="disabled"
    :size="size"
    :start-placeholder="startPlaceholder"
    :end-placeholder="endPlaceholder"
    :range-separator="rangeSeparator"
    :popper-class="popperClass"
    @update:model-value="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
    @change="handleChange"
    class="glass-date"
    :class="className"
  >
    <template v-if="showTime" #append>
      <el-input
        ref="timeRef"
        v-model="timeValue"
        type="time"
        size="small"
        format="HH:mm"
        placeholder="HH:mm"
      ></el-input>
    </template>
  </el-date-picker>
</template>

<script>
export default {
  name: 'LightDate',
  props: {
    modelValue: {
      type: [String, Number, Array, Date, null],
      default: null
    },
    type: {
      type: String,
      default: 'date'
    },
    placeholder: {
      type: String,
      default: ''
    },
    clearable: {
      type: Boolean,
      default: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: ''
    },
    startPlaceholder: {
      type: String,
      default: ''
    },
    endPlaceholder: {
      type: String,
      default: ''
    },
    rangeSeparator: {
      type: String,
      default: '-'
    },
    showTime: {
      type: Boolean,
      default: false
    },
    className: {
      type: String,
      default: ''
    },
    popperClass: {
      type: String,
      default: 'glass-popper'
    }
  },
  data() {
    return {
      timeValue: ''
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

.glass-date {
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
  height: 40px !important;
}

.glass-date .el-icon {
  color: rgb(230, 230, 230) !important;
}

.glass-date .el-range-separator {
  color: rgb(230, 230, 230) !important;
}

.glass-date .el-range-input {
  color: rgb(255, 255, 255) !important;
}

.glass-date .el-range-input::placeholder {
  color: rgb(210, 210, 210) !important;
}
</style>