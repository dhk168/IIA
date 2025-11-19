<template>
  <div class="debug-container">
    <div class="debug-buttons">
      <light-button @click="clearAllData">Clear All Data</light-button>
      <light-button @click="clearReminderData">Clear Reminder Data</light-button>
    </div>
  </div>
</template>

<script>
import debugAPI from '../../api/debug';
import LightButton from '../../components/LightButton.vue';

export default {
  name: 'Debug',
  inject: ['showToast'],
  components: {
    LightButton
  },
  data() {
    return {
      // ...
    }
  },
  methods: {
    // Clear all database table contents
    async clearAllData() {
      try {
        const response = await debugAPI.clearAllData();
        if (response.code === 200) {
          this.showToast('success', 'All database table contents have been successfully cleared');
        } else {
          this.showToast('error', response.msg || 'Failed to clear all data');
        }
      } catch (error) {
        console.error('Failed to clear all data:', error);
        this.showToast('error', 'Failed to clear all data');
      }
    },

    // Clear all reminder module data table contents
    async clearReminderData() {
      try {
        const response = await debugAPI.clearReminderData();
        if (response.code === 200) {
          this.showToast('success', 'All reminder module data table contents have been successfully cleared');
        } else {
          this.showToast('error', response.msg || 'Failed to clear reminder data');
        }
      } catch (error) {
        console.error('Failed to clear reminder data:', error);
        this.showToast('error', 'Failed to clear reminder data');
      }
    },


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