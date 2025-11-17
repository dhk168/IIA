<template>
  <div :class="['light-div', className]" :style="computedStyles">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'LightDiv',
  props: {
    className: {
      type: String,
      default: ''
    },
    dark: {
      type: Boolean,
      default: false
    },
    backgroundOpacity: {
      type: Number,
      default: 0.12
    },
    blurAmount: {
      type: Number,
      default: 12
    },
    borderRadius: {
      type: String,
      default: '16px'
    },
    borderOpacity: {
      type: Number,
      default: 0.15
    },
    boxShadowOpacity: {
      type: Number,
      default: 0.1
    },
    customStyles: {
      type: Object,
      default: () => {}
    }
  },
  computed: {
    computedStyles() {
      const bgColor = this.dark ? `rgba(0, 0, 0, ${this.backgroundOpacity})` : `rgba(255, 255, 255, ${this.backgroundOpacity})`
      const borderColor = this.dark ? `rgba(255, 255, 255, ${this.borderOpacity})` : `rgba(255, 255, 255, ${this.borderOpacity})`
      const boxShadow = this.dark ? `0 6px 24px rgba(255, 255, 255, ${this.boxShadowOpacity})` : `0 6px 24px rgba(0, 0, 0, ${this.boxShadowOpacity})`
      return {
        background: bgColor,
        backdropFilter: `blur(${this.blurAmount}px)`,
        WebkitBackdropFilter: `blur(${this.blurAmount}px)`,
        boxShadow: boxShadow,
        border: `1px solid ${borderColor}`,
        borderRadius: this.borderRadius,
        ...this.customStyles
      }
    }
  }
}
</script>

<style scoped>
.light-div {
  /* 基础样式，可通过props覆盖 */
}
</style>