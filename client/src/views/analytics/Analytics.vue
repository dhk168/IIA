<template>
  <div class="analytics-container">
    <!-- Stats Cards Component -->
    <StatsCard />
    
    <!-- Charts Area Component -->
    <ChartsArea 
      :completion-rate="completionRate"
      :task-categories="taskCategories"
    />
    
    <!-- Recently Completed Tasks Component -->
    <RecentTasks :recent-completed-tasks="recentCompletedTasks" />
  </div>
</template>

<script>
import StatsCard from './components/StatsCard.vue'
import ChartsArea from './components/ChartsArea.vue'
import RecentTasks from './components/RecentTasks.vue'

export default {
  name: 'Analytics',
  components: {
    StatsCard,
    ChartsArea,
    RecentTasks
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
    this.loadAnalyticsData()
  },
  computed: {
    completionRate() {
      return Math.round((this.completedTasks / this.totalTasks) * 100)
    }
  },
  methods: {
    loadAnalyticsData() {
      // In a real application, this would be an API call
      // For now, we'll use mock data
      setTimeout(() => {
        this.totalTasks = 128
        this.completedTasks = 87
        this.pendingTasks = 32
        this.overdueTasks = 9
        
        this.taskCategories = [
          { name: 'Work', count: 45, color: '#409eff' },
          { name: 'Study', count: 32, color: '#67c23a' },
          { name: 'Life', count: 31, color: '#e6a23c' },
          { name: 'Health', count: 20, color: '#f56c6c' }
        ]
        
        this.recentCompletedTasks = [
          {
            title: 'Complete project proposal',
            category: 'Work',
            completedTime: '2023-10-15T14:30:00',
            priority: 'High'
          },
          {
            title: 'Read new book chapter',
            category: 'Study',
            completedTime: '2023-10-14T09:15:00',
            priority: 'Medium'
          },
          {
            title: 'Weekly grocery shopping',
            category: 'Life',
            completedTime: '2023-10-13T18:45:00',
            priority: 'Low'
          },
          {
            title: 'Morning exercise routine',
            category: 'Health',
            completedTime: '2023-10-13T06:30:00',
            priority: 'Medium'
          },
          {
            title: 'Prepare presentation slides',
            category: 'Work',
            completedTime: '2023-10-12T16:20:00',
            priority: 'High'
          }
        ]
      }, 500)
    }
  }
}
</script>

<style scoped>
.analytics-container {
  padding: 20px;
}

h1 {
  margin-bottom: 30px;
  color: #303133;
  font-weight: 500;
}
</style>