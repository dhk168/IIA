<template>
  <el-card class="chart-card">
    <template #header>
        <div class="card-header">
          <span>Task Progress by Tag</span>
        </div>
      </template>
    <div class="chart-content">
      <div class="category-item" v-for="(category, index) in processedCategories" :key="index">
        <div class="category-label">
          <div class="category-color" :style="{ backgroundColor: category.color }"></div>
          <span class="category-name">{{ category.name }}</span>
        </div>
        <el-progress 
          :percentage="category.completionRate || 0" 
          :color="category.color" 
          :format="() => ''" 
        />
        <div class="category-stats">
          <span class="completion-rate">{{ category.completionRate || 0 }}%</span>
          <span class="task-status">{{ category.completedCount || 0 }} / {{ category.count }} tasks completed</span>
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
      // 添加无标签任务的处理
      const categoriesWithNoTag = [...this.taskCategories]
      
      // 检查是否已有无标签类别
      const hasNoTagCategory = categoriesWithNoTag.some(cat => cat.id === 'no_tags')
      
      // 如果没有无标签类别，添加一个
      if (!hasNoTagCategory) {
        const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#C0C4CC']
        categoriesWithNoTag.push({
          id: 'no_tags',
          name: '没有标签',
          count: 0,
          completedCount: 0,
          completionRate: 0,
          color: colors[categoriesWithNoTag.length % colors.length]
        })
      }
      
      const total = categoriesWithNoTag.reduce((sum, cat) => sum + cat.count, 0)
      return categoriesWithNoTag.map(cat => ({
        ...cat,
        percentage: total > 0 ? Math.round((cat.count / total) * 100) : 0,
        // 确保completionRate存在
        completionRate: cat.completionRate !== undefined ? cat.completionRate : 0,
        completedCount: cat.completedCount || 0
      }))
    }
  }
}
</script>

<style scoped>
.chart-card {
  border-radius: 8px;
  /* 毛玻璃效果 */
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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
  /* 为单个分类项也添加轻微的毛玻璃效果 */
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border-radius: 6px;
  padding: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.category-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
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
  color: #606266;
}

.completion-rate {
  font-weight: 500;
  color: #303133;
}

.task-status {
  font-size: 12px;
  color: #606266;
}

.category-percentage {
  color: #909399;
}
</style>