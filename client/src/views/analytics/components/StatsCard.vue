<template>
  <div class="stats-cards">
    <LightCard 
      class="stat-card-item"
      :is-stat-card="true"
      :number="totalTasks"
      label="Total Tasks"
      icon-type="primary"
    >
      <template #icon><Document /></template>
    </LightCard>
    
    <LightCard 
      class="stat-card-item"
      :is-stat-card="true"
      :number="completedTasks"
      label="Completed"
      icon-type="success"
    >
      <template #icon><Check /></template>
    </LightCard>
    
    <LightCard 
      class="stat-card-item"
      :is-stat-card="true"
      :number="pendingTasks"
      label="Pending"
      icon-type="warning"
    >
      <template #icon><Clock /></template>
    </LightCard>
    
    <LightCard 
      class="stat-card-item"
      :is-stat-card="true"
      :number="overdueTasks"
      label="Overdue"
      icon-type="danger"
    >
      <template #icon><Warning /></template>
    </LightCard>
  </div>
</template>

<script>
import { Document, Check, Clock, Warning } from '@element-plus/icons-vue'
import { reminderTaskAPI } from '@/api/reminder.js'
import LightCard from '@/components/LightCard.vue'

export default {
  name: 'StatsCard',
  components: {
    Document,
    Check,
    Clock,
    Warning,
    LightCard
  },
  data() {
    return {
      totalTasks: 128, // 默认值，确保页面加载时有内容显示
      completedTasks: 94,
      pendingTasks: 25,
      overdueTasks: 9,
      tasks: [] // 存储所有任务数据
    }
  },
  mounted() {
    this.loadTasksData()
  },
  methods: {
    async loadTasksData() {
      try {
        console.log('正在加载任务统计数据...')
        
        // 调用后端API获取任务数据
        const response = await reminderTaskAPI.getTasks()
        
        // 检查响应是否成功
        if (response && response.code === 200 && Array.isArray(response.data)) {
          this.tasks = response.data
          // 统计任务数据
          this.calculateTaskStats()
          console.log('任务统计数据加载完成:', this.tasks.length, '个任务')
        } else {
          console.warn('API返回非预期响应:', response)
          this.resetStats()
        }
      } catch (error) {
        console.error('加载任务数据失败:', error)
        // 设置默认值以防止显示错误
        this.resetStats()
      }
    },
    
    // 计算任务统计数据
    calculateTaskStats() {
      const now = new Date()
      
      // 总任务数
      this.totalTasks = this.tasks.length
      
      // 已完成任务数（status为'done'或completed_at不为空）
      this.completedTasks = this.tasks.filter(task => 
        task.status === 'done' || task.completed_at
      ).length
      
      // 待处理任务数（状态不是done且没有过期）
      this.pendingTasks = this.tasks.filter(task => {
        const isCompleted = task.status === 'done' || task.completed_at
        const isOverdue = task.due_date && new Date(task.due_date) < now
        return !isCompleted && !isOverdue
      }).length
      
      // 逾期任务数（状态不是done但已过期）
      this.overdueTasks = this.tasks.filter(task => {
        const isCompleted = task.status === 'done' || task.completed_at
        const isOverdue = task.due_date && new Date(task.due_date) < now
        return !isCompleted && isOverdue
      }).length
    },
    
    // 重置统计数据为默认值
    resetStats() {
      this.totalTasks = 0
      this.completedTasks = 0
      this.pendingTasks = 0
      this.overdueTasks = 0
    }
  }
}
</script>

<style scoped>
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card-item {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 480px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
}
</style>