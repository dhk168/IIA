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
        totalTasks: 0, // Default value to ensure content is displayed when page loads
        completedTasks: 0,
        pendingTasks: 0,
        overdueTasks: 0,
        tasks: [] // Store all task data
    }
  },
  mounted() {
    this.loadTasksData()
  },
  methods: {
    async loadTasksData() {
      try {
        const response = await reminderTaskAPI.getTasks()
        
        if (response && response.code === 200 && Array.isArray(response.data)) {
          this.tasks = response.data
          this.calculateTaskStats() // Calculate task statistics
          // console.log('任务统计数据加载完成:', this.tasks.length, '个任务')
        } else {
          console.warn('API返回非预期响应:', response)
          this.resetStats()
        }
      } catch (error) {
        console.error('加载任务数据失败:', error)
        this.resetStats() // Set default values to prevent display errors
      }
    },
    
    // Calculate task statistics
    calculateTaskStats() {
      const now = new Date()
      
      // Total tasks
      this.totalTasks = this.tasks.length
      
      // Completed tasks (status is 'done' or completed_at is not empty)
      this.completedTasks = this.tasks.filter(task => 
        task.status === 'done' || task.completedAt
      ).length
      
      // Pending tasks (status is not done/abandoned and not overdue)
      this.pendingTasks = this.tasks.filter(task => {
        const isCompletedOrAbandoned = task.status === 'done' || task.status === 'abandoned' || task.completedAt
        const isOverdue = task.dueDate && new Date(task.dueDate) < now
        return !isCompletedOrAbandoned && !isOverdue
      }).length
      
      // Overdue tasks (status is not done/abandoned but expired)
      this.overdueTasks = this.tasks.filter(task => {
        const isCompletedOrAbandoned = task.status === 'done' || task.status === 'abandoned' || task.completed_at
        const isOverdue = task.dueDate && new Date(task.dueDate) < now
        return !isCompletedOrAbandoned && isOverdue
      }).length
    },
    
    // Reset statistics to default values
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