<template>
  <div class="custom-dropdown" @click="toggleDropdown">
    <slot name="trigger"></slot>
    <!-- 自定义下拉菜单内容 -->
    <transition name="dropdown">
      <div v-if="dropdownVisible" class="custom-dropdown-menu">
        <slot name="menu"></slot>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'GlassDropdown',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dropdownVisible: this.visible
    }
  },
  emits: ['update:visible', 'toggle'],
  methods: {
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
      this.$emit('toggle', this.dropdownVisible);
      this.$emit('update:visible', this.dropdownVisible);
    },
    // 点击外部关闭下拉菜单
    closeDropdownOnClickOutside(event) {
      const dropdown = this.$el.querySelector('.custom-dropdown');
      if (dropdown && !dropdown.contains(event.target)) {
        this.dropdownVisible = false;
        this.$emit('update:visible', false);
      }
    }
  },
  mounted() {
    // 添加点击外部关闭下拉菜单的事件监听
    document.addEventListener('click', this.closeDropdownOnClickOutside);
  },
  beforeUnmount() {
    // 移除事件监听
    document.removeEventListener('click', this.closeDropdownOnClickOutside);
  },
  watch: {
    visible(newVal) {
      this.dropdownVisible = newVal;
    }
  }
}
</script>

<style scoped>
.custom-dropdown {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 300;
  width: fit-content;
}

:deep(.custom-dropdown-menu) {
  background-color: rgba(80, 100, 145, 0.45);
  backdrop-filter: blur(20px);
  position: absolute;
  top: calc(100% + 12px);
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.3);
  z-index: 15000;
  text-align: left;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    width: 90px;
    top: calc(100% + 8px);
  }
  
  @media (max-width: 480px) {
    width: 80px;
    position: fixed;
    right: 20px;
    top: 80px;
    left: auto;
    transform: none;
  }
}

:deep(.custom-dropdown-item) {
  padding: 12px 20px;
  color: rgb(230, 230, 230);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: block;
  text-decoration: none;
}

:deep(.custom-dropdown-item):hover {
  /* background-color: rgba(0, 100, 145, 0.6); */
  transform: translateX(5px);
}

</style>
