<template>
  <div class="analytics-container">
    <!-- Stats Cards Component -->
    <StatsCard />
    
    <!-- Charts Components -->
    <div class="charts-container">
      <!-- Task Progress Component -->
      <ProgressCard 
        :completion-rate="completionRate" 
        :totalTasks="totalTasks"
        :completedTasks="completedTasks"
        :projectProgress="projectProgress"
      />
      
      <!-- Task Category Distribution Component -->
      <CategoryDistributionCard :task-categories="taskCategories" />
    </div>
    
    <!-- Recently Completed Tasks Component -->
    <RecentTasks :recent-completed-tasks="recentCompletedTasks" />
  </div>
</template>

<script>
import StatsCard from './components/StatsCard.vue'
import ProgressCard from './components/ProgressCard.vue'
import CategoryDistributionCard from './components/CategoryDistributionCard.vue'
import RecentTasks from './components/RecentTasks.vue'
import { reminderTaskAPI, reminderProjectAPI, tagAPI } from '@/api/reminder.js'

export default {
  name: 'Analytics',
  components: {
    StatsCard,
    ProgressCard,
    CategoryDistributionCard,
    RecentTasks
  },
  data() {
    return {
      totalTasks: 0,
      completedTasks: 0,
      pendingTasks: 0,
      overdueTasks: 0,
      taskCategories: [],
      recentCompletedTasks: [],
      projectProgress: [],
      tasks: [],
      projects: [],
      tags: [],
      projectMap: {} // 新增：{projectId: name} 格式的数据结构
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
    async loadAnalyticsData() {
      try {
        // 获取所有任务数据
        const tasksResponse = await reminderTaskAPI.getTasks()
        
        if (tasksResponse && tasksResponse.code === 200 && Array.isArray(tasksResponse.data)) {
          this.tasks = tasksResponse.data
          this.calculateTaskStats()
          this.generateTaskCategories()
          this.generateRecentCompletedTasks()
        }
        // 任务数据处理完成
        
        // 获取所有项目数据
        const projectsResponse = await reminderProjectAPI.getAllProjects()
        
        if (projectsResponse && projectsResponse.code === 200 && Array.isArray(projectsResponse.data)) {
          this.projects = projectsResponse.data
          // 构建 {projectId: name} 格式的数据结构
          this.projectMap = {}
          this.projects.forEach(project => {
            // 使用数字类型的projectId作为键，项目名称作为值
            const projectId = Number(project.projectId) || Number(project.id) || project.id
            this.projectMap[projectId] = project.name || 'Unnamed Project'
          })
          console.log('项目映射数据结构:', JSON.stringify(this.projectMap))
          this.generateProjectProgress()
        }
        
      } catch (error) {
        console.error('加载分析数据失败:', error)
        this.resetAnalyticsData()
      }
    },
    
    // 计算任务统计信息
    calculateTaskStats() {
      const now = new Date()
      
      // 总任务数
      this.totalTasks = this.tasks.length
      
      // 已完成任务数
      this.completedTasks = this.tasks.filter(task => 
        task.status === 'done' || task.completedAt
      ).length
      
      // 待处理任务数
      this.pendingTasks = this.tasks.filter(task => {
        const isCompletedOrAbandoned = task.status === 'done' || task.status === 'abandoned' || task.completedAt
        const isOverdue = task.dueDate && new Date(task.dueDate) < now
        return !isCompletedOrAbandoned && !isOverdue
      }).length
      
      // 逾期任务数
      this.overdueTasks = this.tasks.filter(task => {
        const isCompletedOrAbandoned = task.status === 'done' || task.status === 'abandoned' || task.completed_at
        const isOverdue = task.dueDate && new Date(task.dueDate) < now
        return !isCompletedOrAbandoned && isOverdue
      }).length
    },
    
    // 生成任务类别分布
    generateTaskCategories() {
      // 颜色映射
      const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']
      
      // 按标签或类别分组统计
      const categoryMap = new Map()
      
      this.tasks.forEach(task => {
        // 优先使用标签名，如果没有标签则使用默认类别
        const categoryName = task.tags && task.tags.length > 0 
          ? task.tags[0].name 
          : task.category || 'Others'
        
        categoryMap.set(categoryName, (categoryMap.get(categoryName) || 0) + 1)
      })
      
      // 转换为所需格式
      let colorIndex = 0
      this.taskCategories = Array.from(categoryMap.entries()).map(([name, count]) => ({
        name,
        count,
        color: colors[colorIndex++ % colors.length]
      }))
    },
    
    // 生成最近完成的任务
    generateRecentCompletedTasks() {
      // 过滤出已完成的任务并按完成时间排序
      const completedTasks = this.tasks
        .filter(task => task.status === 'done' || task.completedAt)
        .sort((a, b) => new Date(b.completedAt || b.updated_at) - new Date(a.completedAt || a.updated_at))
        .slice(0, 5) // 取最近5个
      
      this.recentCompletedTasks = completedTasks.map(task => ({
        title: task.title || 'Untitled Task',
        category: task.tags && task.tags.length > 0 ? task.tags[0].name : task.category || 'Others',
        completedTime: task.completedAt || task.updated_at,
        priority: this.getPriorityLabel(task.priority)
      }))
    },
    
    // 生成项目进度数据
    generateProjectProgress() {
      console.log('生成项目进度数据 - 开始')
      console.log('当前项目数量:', this.projects.length)
      console.log('当前任务数量:', this.tasks.length)
      
      // 颜色映射
      const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']
      
      // 按项目分组统计任务
      const projectTaskMap = new Map()
      
      // 首先初始化所有项目，确保每个项目都有记录
      if (Object.keys(this.projectMap).length > 0) {
        console.log('项目映射数据结构:', JSON.stringify(this.projectMap))
        
        Object.entries(this.projectMap).forEach(([projectId, projectName]) => {
          // 确保key是数字类型
          const numProjectId = Number(projectId)
          
          console.log('项目信息:', {name: projectName, key: numProjectId})
          
          projectTaskMap.set(numProjectId, {
            projectId: numProjectId,
            projectName: projectName,
            totalTasks: 0,
            completedTasks: 0
          }) // 初始化项目任务统计
        })
        
        console.log('初始化后的项目映射:', Array.from(projectTaskMap.entries()).map(([key, proj]) => `${key}:${proj.projectName}`))
      } else {
        console.log('未找到项目数据，使用默认演示项目')
        const defaultProjects = [
          { id: 1, name: 'Website Redesign' },
          { id: 2, name: 'Mobile App Development' },
          { id: 3, name: 'Marketing Campaign' },
          { id: 4, name: 'Research & Development' }
        ]
        defaultProjects.forEach(project => {
          // 保持ID为数字类型
          projectTaskMap.set(project.id, {
            projectId: project.id,
            projectName: project.name,
            totalTasks: 0,
            completedTasks: 0
          })
        })
      }
      
      // 处理任务与项目的关联
      console.log('开始处理任务关联')
      console.log('任务数据结构:', JSON.stringify(this.tasks[0]))
      const taskWithProject = this.tasks.filter(t => t.projectId)
      console.log(`有${taskWithProject.length}个任务关联了项目`) 
      
      // 打印关联项目的任务详情
      if (taskWithProject.length > 0) {
        console.log('关联项目的任务详情:', taskWithProject.map(task => ({
          taskId: task.taskId,
          title: task.title,
          projectId: task.projectId,
          status: task.status,
          completedAt: task.completedAt
          })))
      }
      
      // 不再需要单独的项目名称映射，直接使用projectMap
      
      this.tasks.forEach(task => {
          // 打印每个任务的完整信息
          console.log('当前处理任务详情:', {
            id: task.id,
            title: task.title,
            projectId: task.projectId,
            status: task.status,
            completed_at: task.completed_at,
            tags: task.tags,
            category: task.category
          })
          
        if (task.projectId !== undefined && task.projectId !== null) {
          const projectId = Number(task.projectId) || task.projectId // 保持数字类型
          const projectIdStr = String(projectId)
          
          console.log(`处理任务: ${task.title}, 项目ID: ${projectId} (原始类型: ${typeof projectId})`)
          
          // 查找匹配的项目 - 使用多种匹配策略
          let matchedProject = null
          
          // 策略1: 直接匹配项目键，首先尝试精确匹配数字ID
          const possibleKeys = [
            projectId, // 数字类型ID
            projectIdStr // 字符串类型ID（备用）
          ]
          
          // 首先尝试直接精确匹配数字ID
          if (projectTaskMap.has(projectId)) {
            matchedProject = projectTaskMap.get(projectId)
          } else {
            // 如果精确匹配失败，尝试其他可能的键
            for (const key of possibleKeys) {
              if (projectTaskMap.has(key)) {
                matchedProject = projectTaskMap.get(key)
                break
              }
            }
          }
          
          // 策略2: 通过projectMap查找项目名称
          if (!matchedProject && this.projectMap[projectId]) {
            // 如果有项目ID但没匹配到，可能是因为ID是数字但map中是字符串，重新尝试
            const numProjectId = Number(projectId)
            if (projectTaskMap.has(numProjectId)) {
              matchedProject = projectTaskMap.get(numProjectId)
            }
          }
          
          // 策略3: 尝试反向匹配 - 检查任务的其他字段是否包含项目信息
          if (!matchedProject && task.title) {
            const taskTitleLower = task.title.toLowerCase()
            matchedProject = Array.from(projectTaskMap.values()).find(proj => 
              taskTitleLower.includes(proj.projectName.toLowerCase())
            )
          }
          
          if (matchedProject) {
            console.log(`找到匹配的项目: ${matchedProject.projectName}`)
            console.log(`任务 ${task.title} (ID:${task.taskId}) 成功匹配到项目: ${matchedProject.projectName}`)
            const wasCompleted = task.status === 'done' || task.completedAt
            console.log(`  - 匹配前项目统计: 完成=${matchedProject.completedTasks}, 总计=${matchedProject.totalTasks}`)
            matchedProject.totalTasks++
            
            if (wasCompleted) {
              matchedProject.completedTasks++
            }
            console.log(`  - 匹配后项目统计: 完成=${matchedProject.completedTasks}, 总计=${matchedProject.totalTasks}`)
          } else {
            // 如果任务有projectId但找不到对应的项目，创建一个未分类项目
            console.log(`找到未分类项目的任务，projectId: ${task.projectId}`)
            console.log(`任务 ${task.title} (ID:${task.taskId}) 未匹配到任何项目，创建新的项目记录`)
            console.log(`  - 尝试匹配的键值:`, possibleKeys)
            // 为未匹配的项目使用数字ID作为键，优先从projectMap获取项目名称
            const numProjectId = Number(projectId)
            const projectName = this.projectMap[numProjectId] || this.projectMap[projectId] || `proj${projectIdStr}`
            projectTaskMap.set(numProjectId, {
              projectId: numProjectId,
              projectName: projectName,
              totalTasks: 1,
              completedTasks: task.status === 'done' || task.completedAt ? 1 : 0
            })
          }
        } else {
            // 处理没有projectId的任务，添加到"其他"项目
            const otherProjectId = 'other'
          if (!projectTaskMap.has(otherProjectId)) {
            projectTaskMap.set(otherProjectId, {
              projectId: 'other',
              projectName: 'Other Tasks',
              totalTasks: 0,
              completedTasks: 0
            })
          }
          const otherProject = projectTaskMap.get(otherProjectId)
          console.log(`任务 ${task.title} (ID:${task.taskId}) 未匹配到任何项目，添加到其他任务`)
          console.log(`  - 匹配前其他任务统计: 完成=${otherProject.completedTasks}, 总计=${otherProject.totalTasks}`)
          otherProject.totalTasks++
          if (task.status === 'done' || task.completedAt) {
            otherProject.completedTasks++
          }
          console.log(`  - 匹配后其他任务统计: 完成=${otherProject.completedTasks}, 总计=${otherProject.totalTasks}`)
        }
      })
      
      // 转换为所需格式并排序
      let colorIndex = 0
      this.projectProgress = Array.from(projectTaskMap.values())
        .map(project => ({
          ...project,
          completionRate: project.totalTasks > 0 
            ? Math.round((project.completedTasks / project.totalTasks) * 100) 
            : 0,
          color: colors[colorIndex++ % colors.length]
        }))
        // 修改排序逻辑，确保有任务的项目排在前面，但所有项目都显示
        .sort((a, b) => {
          // 有任务的项目优先
          if (a.totalTasks > 0 && b.totalTasks === 0) return -1
          if (a.totalTasks === 0 && b.totalTasks > 0) return 1
          // 然后按完成任务数排序
          return b.completedTasks - a.completedTasks
        })
      
      console.log('生成的项目进度数据:', this.projectProgress)
    },
    
    // 获取优先级标签
    getPriorityLabel(priority) {
      const priorityMap = {
        'high': 'High',
        'medium': 'Medium',
        'low': 'Low'
      }
      return priorityMap[priority] || 'Medium'
    },
    
    // 重置分析数据为默认值
    resetAnalyticsData() {
      this.totalTasks = 0
      this.completedTasks = 0
      this.pendingTasks = 0
      this.overdueTasks = 0
      this.taskCategories = []
      this.recentCompletedTasks = []
      this.projectProgress = []
    }
  }
}
</script>

<style scoped>
.analytics-container {
  padding: 20px;
}

.charts-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}

h1 {
  margin-bottom: 30px;
  color: #303133;
  font-weight: 500;
}
</style>