<template>
  <div class="debug-container">
    <div class="debug-buttons">
      <el-button class="light-button" @click="clearAllData">清除全部数据</el-button>
      <el-button class="light-button" @click="clearReminderData">清除备忘录数据</el-button>
    </div>
  </div>
</template>

<script>
import '../../assets/styles/components/glass-toast.css';
import debugAPI from '../../api/debug';

export default {
  name: 'Debug',
  data() {
    return {
      // 可以添加debug相关的数据
    }
  },
  methods: {
    // 清除所有数据库表内容
    async clearAllData() {
      try {
        const response = await debugAPI.clearAllData();
        if (response.code === 200) {
          this.createGlassToast('success', '所有数据库表内容已成功清除');
        } else {
          this.createGlassToast('error', response.msg || '清除全部数据失败');
        }
      } catch (error) {
        console.error('清除全部数据失败:', error);
        this.createGlassToast('error', '清除全部数据失败');
      }
    },

    // 清除所有提醒模块数据表内容
    async clearReminderData() {
      try {
        const response = await debugAPI.clearReminderData();
        if (response.code === 200) {
          this.createGlassToast('success', '所有提醒模块数据表内容已成功清除');
        } else {
          this.createGlassToast('error', response.msg || '清除备忘录数据失败');
        }
      } catch (error) {
        console.error('清除备忘录数据失败:', error);
        this.createGlassToast('error', '清除备忘录数据失败');
      }
    },

    // 毛玻璃提示方法
    createGlassToast(type, message) {
      const toast = document.createElement('div');
      toast.className = `glass-toast glass-toast-${type}`;
      toast.textContent = message;
      document.body.appendChild(toast);

      // 自动移除
      setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
      }, 4000);

      setTimeout(() => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      }, 4500);

      toast.addEventListener('click', () => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      });
    }
  }
}
</script>

<style scoped>
.debug-container {
  width: 100%;
  height: 100%;
  padding: 20px;
}
</style>