<template>
  <div class="analytics-container">
    <div class="page-header">
      <h1>Analytics</h1>
      <p>View your to-do list statistics</p>
    </div>
    
    <!-- Stats Cards -->
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-icon primary"><Document /></div>
        <div class="stat-content">
          <div class="stat-number">{{ totalTasks }}</div>
          <div class="stat-label">Total Tasks</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon success"><Check /></div>
        <div class="stat-content">
          <div class="stat-number">{{ completedTasks }}</div>
          <div class="stat-label">Completed</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon warning"><Clock /></div>
        <div class="stat-content">
          <div class="stat-number">{{ pendingTasks }}</div>
          <div class="stat-label">Pending</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon danger"><Warning /></div>
        <div class="stat-content">
          <div class="stat-number">{{ overdueTasks }}</div>
          <div class="stat-label">Overdue</div>
        </div>
      </el-card>
    </div>
    
    <!-- Charts Area -->
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
    
    <!-- Recently Completed Tasks -->
    <el-card class="recent-tasks-card">
      <template #header>
        <div class="card-header">
          <span>Recently Completed Tasks</span>
        </div>
      </template>
      <el-table :data="recentCompletedTasks" stripe style="width: 100%">
        <el-table-column prop="title" label="Task Name" width="300" />
        <el-table-column prop="category" label="Category">
          <template #default="scope">
            <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completedTime" label="Completion Time">
          <template #default="scope">
            {{ formatDate(scope.row.completedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="Priority">
          <template #default="scope">
            <el-tag :type="getPriorityType(scope.row.priority)">{{ scope.row.priority }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { Document, Check, Clock, Warning } from '@element-plus/icons-vue'

export default {
  name: 'Analytics',
  components: {
    Document,
    Check,
    Clock,
    Warning
  },
  data() {
    return {
      totalTasks: 0,
      completedTasks: 0,
      pendingTasks: 0,
      overdueTasks: 0,
      taskCategories: [],
      recentCompletedTasks: []
    }
  },
  created() {
    this.loadAnalyticsData();
  },
  computed: {
    completionRate() {
      return Math.round((this.completedTasks / this.totalTasks) * 100)
    }
  },
  methods: {
    async loadAnalyticsData() {
      try {
        // TODO: 实现真实的数据分析API调用
        console.log('Loading analytics data from API');
        // 这里将在API实现后替换为实际调用
      } catch (error) {
        console.error('Failed to load analytics data:', error);
      }
    },
    formatRate(percentage) {
      return `${percentage}%`
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleString('en-US')
    },
    getCategoryType(category) {
      const typeMap = {
        'Work': 'primary',
        'Study': 'success',
        'Life': 'warning',
        'Health': 'danger'
      }
      return typeMap[category] || 'info'
    },
    getPriorityType(priority) {
      const typeMap = {
        'High': 'danger',
        'Medium': 'warning',
        'Low': 'success'
      }
      return typeMap[priority] || 'info'
    }
  }
}
</script>

<style scoped>
.analytics-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.stat-icon {
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

.stat-icon.primary {
  background-color: #409eff;
}

.stat-icon.success {
  background-color: #67c23a;
}

.stat-icon.warning {
  background-color: #e6a23c;
}

.stat-icon.danger {
  background-color: #f56c6c;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

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

.recent-tasks-card {
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .stats-cards {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 480px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 15px;
  }
  
  .stat-number {
    font-size: 24px;
  }
}
</style>