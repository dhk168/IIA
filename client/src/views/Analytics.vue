<template>
  <div class="analytics-container">
    <div class="page-header">
      <h1>数据分析</h1>
      <p>查看您的待办事项统计情况</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-icon primary"><Document /></div>
        <div class="stat-content">
          <div class="stat-number">{{ totalTasks }}</div>
          <div class="stat-label">总待办事项</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon success"><Check /></div>
        <div class="stat-content">
          <div class="stat-number">{{ completedTasks }}</div>
          <div class="stat-label">已完成</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon warning"><Clock /></div>
        <div class="stat-content">
          <div class="stat-number">{{ pendingTasks }}</div>
          <div class="stat-label">待处理</div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-icon danger"><Warning /></div>
        <div class="stat-content">
          <div class="stat-number">{{ overdueTasks }}</div>
          <div class="stat-label">已逾期</div>
        </div>
      </el-card>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-container">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>待办事项进度</span>
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
            <span>待办事项类型分布</span>
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
    
    <!-- 最近完成的任务 -->
    <el-card class="recent-tasks-card">
      <template #header>
        <div class="card-header">
          <span>最近完成的任务</span>
        </div>
      </template>
      <el-table :data="recentCompletedTasks" stripe style="width: 100%">
        <el-table-column prop="title" label="任务名称" width="300" />
        <el-table-column prop="category" label="类别">
          <template #default="scope">
            <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completedTime" label="完成时间">
          <template #default="scope">
            {{ formatDate(scope.row.completedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级">
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
      // 模拟数据
      totalTasks: 32,
      completedTasks: 18,
      pendingTasks: 14,
      overdueTasks: 2,
      taskCategories: [
        { name: '工作', count: 12, color: '#409eff' },
        { name: '学习', count: 8, color: '#67c23a' },
        { name: '生活', count: 6, color: '#e6a23c' },
        { name: '健康', count: 4, color: '#f56c6c' }
      ],
      recentCompletedTasks: [
        { title: '完成项目提案', category: '工作', completedTime: '2023-10-15 14:30', priority: '高' },
        { title: '阅读技术文档', category: '学习', completedTime: '2023-10-14 09:15', priority: '中' },
        { title: '购买生活用品', category: '生活', completedTime: '2023-10-13 18:45', priority: '低' },
        { title: '晨跑30分钟', category: '健康', completedTime: '2023-10-13 07:00', priority: '中' }
      ]
    }
  },
  computed: {
    completionRate() {
      return Math.round((this.completedTasks / this.totalTasks) * 100)
    }
  },
  methods: {
    formatRate(percentage) {
      return `${percentage}%`
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleString('zh-CN')
    },
    getCategoryType(category) {
      const typeMap = {
        '工作': 'primary',
        '学习': 'success',
        '生活': 'warning',
        '健康': 'danger'
      }
      return typeMap[category] || 'info'
    },
    getPriorityType(priority) {
      const typeMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
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