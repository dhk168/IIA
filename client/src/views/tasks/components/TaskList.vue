<template>
  <el-card class="tasks-card">
    <template #header>
      <div class="card-header">
        <span>Task List</span>
      </div>
    </template>
    <light-table :data="filteredTasks" stripe style="width: 100%" default-expand-all>
      <el-table-column type="expand" width="40">
        <template #default="scope">
          <div class="task-detail">
            <div class="detail-item" v-if="scope.row.description">
              <strong>Description:</strong>
              <p>{{ scope.row.description }}</p>
            </div>
            <div class="detail-item">
              <strong>Created At:</strong>
              <span>{{ formatDate(scope.row.created_at) }}</span>
            </div>
            <div class="detail-item" v-if="scope.row.start_date">
              <strong>Start Date:</strong>
              <span>{{ formatDate(scope.row.start_date) }}</span>
            </div>
            <div class="detail-item" v-if="scope.row.completed_at">
              <strong>Completed At:</strong>
              <span>{{ formatDate(scope.row.completed_at) }}</span>
            </div>
            <div class="detail-item" v-if="scope.row.recurrence_info">
              <strong>Recurrence Rule:</strong>
              <span>{{ getRecurrenceText(scope.row.recurrence_info) }}</span>
            </div>
            <div class="detail-item" v-if="scope.row.tags && scope.row.tags.length > 0">
              <strong>Tags:</strong>
              <div class="task-tags">
                <el-tag 
                  v-for="tag in scope.row.tags" 
                  :key="tag.tagId" 
                  :type="getTagType(tag.color)" 
                  size="small" 
                  effect="plain"
                >
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="Task Name" min-width="300">
        <template #default="scope">
          <div class="task-title-container">
            <el-checkbox v-model="scope.row.status" :checked="scope.row.status === 'done'" @change="updateTaskStatus(scope.row)">
              <span :class="{ 'task-completed': scope.row.status === 'done' }" :style="{ paddingLeft: scope.row.level * 20 + 'px' }">{{ scope.row.title }}</span>
            </el-checkbox>
            <el-tag v-if="scope.row.category === 'note'" size="small" type="info" style="margin-left: 10px;">Note</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="project_name" label="Project" width="120" />
      <el-table-column prop="due_date" label="Due Date" width="120">
        <template #default="scope">
          <el-tag :type="getTaskDueType(scope.row)">
            {{ formatDate(scope.row.due_date) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="Priority" width="100">
        <template #default="scope">
          <el-tag :type="getPriorityType(scope.row.priority)">
            {{ getPriorityText(scope.row.priority) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="editTask(scope.row)">Edit</el-button>
          <el-button size="small" type="danger" @click="deleteTask(scope.row.task_id)">Delete</el-button>
        </template>
      </el-table-column>
    </light-table>
  </el-card>
</template>

<script>
import LightTable from '@/components/LightTable.vue'

export default {
  name: 'TaskList',
  components: {
    LightTable
  },
  props: {
    filteredTasks: {
      type: Array,
      required: true
    },
    tags: {
      type: Array,
      required: true
    },
    projects: {
      type: Array,
      required: true
    }
  },
  methods: {
    getTagType(color) {
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#e6a23c': 'warning',
        '#f56c6c': 'danger',
        '#909399': 'info'
      };
      return colorMap[color] || 'info';
    },
    
    formatDate(dateString) {
      if (!dateString) return 'None';
      const date = new Date(dateString);
      return date.toLocaleString('en-US');
    },
    
    getPriorityType(priority) {
      const typeMap = {
        'none': 'info',
        'low': 'success',
        'medium': 'warning',
        'high': 'danger'
      };
      return typeMap[priority] || 'info';
    },
    
    getPriorityText(priority) {
      const textMap = {
        'none': 'None',
        'low': 'Low',
        'medium': 'Medium',
        'high': 'High'
      };
      return textMap[priority] || 'None';
    },
    
    getTaskDueType(task) {
      if (!task.due_date) return 'info';
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const tomorrow = new Date(today);
      tomorrow.setDate(tomorrow.getDate() + 1);
      const dueDate = new Date(task.due_date);
      
      if (task.status === 'done') return 'success';
      if (dueDate < today) return 'danger';
      if (dueDate < tomorrow) return 'warning';
      return 'info';
    },
    
    getRecurrenceText(recurrenceInfo) {
      if (!recurrenceInfo) return '';
      
      const { category, schedule, count } = recurrenceInfo;
      
      switch (category) {
        case 'weekly':
          const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
          const dayTexts = schedule.map(day => days[day]).join(', ');
          return `Every ${dayTexts}, ${count} times`;
        case 'monthly':
          return `Monthly on ${schedule.join(', ')}, ${count} times`;
        case 'yearly':
          return `Yearly on ${schedule[0]}, ${count} times`;
        case 'days':
          return `Every ${schedule || 1} days, ${count} times`;
        case 'weeks':
          return `Every ${schedule || 1} weeks, ${count} times`;
        case 'ebinghaus':
          return `Ebbinghaus Memory Curve, ${count} times`;
        default:
          return 'Custom recurrence';
      }
    },
    
    editTask(task) {
      this.$emit('edit-task', task);
    },
    
    deleteTask(taskId) {
      this.$emit('delete-task', taskId);
    },
    
    updateTaskStatus(task) {
      this.$emit('update-task-status', task);
    }
  }
}
</script>