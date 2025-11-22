<template>
  <div class="charts-container">
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>Task Progress</span>
        </div>
      </template>
      <div class="chart-content">
        <div class="progress-chart">
          <el-progress :percentage="completionRate" :format="formatRate" />
        </div>
      </div>
    </el-card>
    
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>Task Category Distribution</span>
        </div>
      </template>
      <div class="chart-content">
        <div class="category-chart">
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item, index) in taskCategories" :key="index">
              <div class="category-item">
                <div class="category-color" :style="{ backgroundColor: item.color }"></div>
                <div class="category-info">
                  <div class="category-name">{{ item.name }}</div>
                  <div class="category-count">{{ item.count }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ChartsArea',
  props: {
    completionRate: {
      type: Number,
      default: 0
    },
    taskCategories: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    formatRate(percentage) {
      return `${percentage}%`
    }
  }
}
</script>

<style scoped>
.charts-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 30px;
}

.chart-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  color: #303133;
}

.chart-content {
  padding: 20px 0;
}

.progress-chart {
  width: 80%;
  margin: 0 auto;
  padding: 20px 0;
}

.category-chart {
  padding: 10px 0;
}

.category-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.category-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  margin-right: 10px;
}

.category-info {
  flex: 1;
}

.category-name {
  font-size: 14px;
  color: #606266;
}

.category-count {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-top: 5px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>