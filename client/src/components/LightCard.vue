<template>
  <div class="light-card" :class="{ 
    'hover-effect': hoverable,
    'stat-card': isStatCard
  }">
    <div class="card-content">
      <!-- 如果是统计卡片，可以直接使用传入的属性 -->
      <template v-if="isStatCard">
        <div class="stat-icon" :class="iconType"><slot name="icon"></slot></div>
        <div class="stat-content">
          <div class="stat-number">{{ number }}</div>
          <div class="stat-label">{{ label }}</div>
        </div>
      </template>
      <!-- 否则使用默认插槽 -->
      <template v-else>
        <slot></slot>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LightCard',
  props: {
    hoverable: {
      type: Boolean,
      default: true
    },
    // 统计卡片相关属性
    isStatCard: {
      type: Boolean,
      default: false
    },
    number: {
      type: [Number, String],
      default: 0
    },
    label: {
      type: String,
      default: ''
    },
    iconType: {
      type: String,
      default: 'primary'
    }
  }
}
</script>

<style scoped>
.light-card {
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.15), rgba(255, 255, 255, 0.1));
  backdrop-filter: blur(15px);
  -webkit-backdrop-filter: blur(15px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 20px;
  transition: all 0.3s ease;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
}

.light-card.hover-effect:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px 0 rgba(31, 38, 135, 0.1);
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.25), rgba(255, 255, 255, 0.15));
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 统计卡片特定样式 */
.light-card.stat-card .card-content {
  flex-direction: row;
  align-items: center;
}

.light-card.stat-card .stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: #fff;
  font-size: 24px;
}

.light-card.stat-card .stat-icon.primary {
  background-color: rgba(64, 158, 255, 0.8);
}

.light-card.stat-card .stat-icon.success {
  background-color: rgba(103, 194, 58, 0.8);
}

.light-card.stat-card .stat-icon.warning {
  background-color: rgba(230, 162, 60, 0.8);
}

.light-card.stat-card .stat-icon.danger {
  background-color: rgba(245, 108, 108, 0.8);
}

.light-card.stat-card .stat-content {
  flex: 1;
}

.light-card.stat-card .stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  margin-bottom: 5px;
}

.light-card.stat-card .stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}
</style>