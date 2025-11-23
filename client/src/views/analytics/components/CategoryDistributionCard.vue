<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>Task Category Distribution</span>
      </div>
    </template>
    <div class="chart-content">
      <div class="category-item" v-for="(category, index) in processedCategories" :key="index">
        <div class="category-label">
          <div class="category-color" :style="{ backgroundColor: category.color }"></div>
          <span class="category-name">{{ category.name }}</span>
        </div>
        <el-progress 
          :percentage="category.percentage" 
          :color="category.color" 
          :format="() => ''" 
        />
        <div class="category-stats">
          <span class="category-count">{{ category.count }}</span>
          <span class="category-percentage">{{ category.percentage }}%</span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'CategoryDistributionCard',
  props: {
    taskCategories: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    // 计算每个类别的百分比
    processedCategories() {
      const total = this.taskCategories.reduce((sum, cat) => sum + cat.count, 0)
      return this.taskCategories.map(cat => ({
        ...cat,
        percentage: total > 0 ? Math.round((cat.count / total) * 100) : 0
      }))
    }
  }
}
</script>

<style scoped>
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

.category-item {
  margin-bottom: 20px;
}

.category-label {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.category-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  margin-right: 10px;
}

.category-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.category-stats {
  display: flex;
  justify-content: space-between;
  margin-top: 4px;
  font-size: 12px;
}

.category-count {
  color: #606266;
}

.category-percentage {
  color: #909399;
}
</style>