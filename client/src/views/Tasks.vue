<template>
  <div class="tasks-container">
    <div class="page-header">
      <h1>任务管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showAddTaskDialog = true">
          <el-icon><Plus /></el-icon> 新建任务
        </el-button>
        <el-button @click="showTagsDialog = true">
          <el-icon><Management /></el-icon> 标签管理
        </el-button>
      </div>
    </div>
    
    <!-- 任务筛选 -->
    <div class="filter-section">
      <el-row :gutter="20">
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.status" placeholder="任务状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="待处理" value="todo" />
            <el-option label="已完成" value="done" />
            <el-option label="已放弃" value="abandoned" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.project" placeholder="所属项目" clearable>
            <el-option label="全部" value="" />
            <el-option v-for="project in projects" :key="project.project_id" :label="project.name" :value="project.project_id" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.priority" placeholder="优先级" clearable>
            <el-option label="全部" value="" />
            <el-option label="无" value="none" />
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-col>
        <el-col :md="6" :sm="24">
          <el-date-picker v-model="filterOptions.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" />
        </el-col>
        <el-col :md="6" :sm="24">
          <el-select v-model="filterOptions.tag" placeholder="标签筛选" clearable>
            <el-option label="全部" value="" />
            <el-option v-for="tag in tags" :key="tag.tag_id" :label="tag.name" :value="tag.tag_id" />
          </el-select>
        </el-col>
      </el-row>
    </div>
    
    <!-- 任务列表 -->
    <el-card class="tasks-card">
      <template #header>
        <div class="card-header">
          <span>任务列表</span>
        </div>
      </template>
      <el-table :data="filteredTasks" stripe style="width: 100%" default-expand-all>
        <el-table-column type="expand" width="40">
          <template #default="scope">
            <div class="task-detail">
              <div class="detail-item" v-if="scope.row.description">
                <strong>描述：</strong>
                <p>{{ scope.row.description }}</p>
              </div>
              <div class="detail-item">
                <strong>创建时间：</strong>
                <span>{{ formatDate(scope.row.created_at) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.start_date">
                <strong>开始日期：</strong>
                <span>{{ formatDate(scope.row.start_date) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.completed_at">
                <strong>完成时间：</strong>
                <span>{{ formatDate(scope.row.completed_at) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.recurrence_info">
                <strong>重复规则：</strong>
                <span>{{ getRecurrenceText(scope.row.recurrence_info) }}</span>
              </div>
              <div class="detail-item" v-if="scope.row.tags && scope.row.tags.length > 0">
                <strong>标签：</strong>
                <el-tag v-for="tag in scope.row.tags" :key="tag.tag_id" :type="getTagType(tag.color)">
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="任务名称" min-width="300">
          <template #default="scope">
            <div class="task-title-container">
              <el-checkbox v-model="scope.row.status" :checked="scope.row.status === 'done'" @change="updateTaskStatus(scope.row)">
                <span :class="{ 'task-completed': scope.row.status === 'done' }" :style="{ paddingLeft: scope.row.level * 20 + 'px' }">{{ scope.row.title }}</span>
              </el-checkbox>
              <el-tag v-if="scope.row.category === 'note'" size="small" type="info" style="margin-left: 10px;">笔记</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="project_name" label="项目" width="120" />
        <el-table-column prop="due_date" label="截止日期" width="120">
          <template #default="scope">
            <el-tag :type="getTaskDueType(scope.row)">
              {{ formatDate(scope.row.due_date) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="scope">
            <el-tag :type="getPriorityType(scope.row.priority)">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editTask(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteTask(scope.row.task_id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加/编辑任务对话框 -->
    <el-dialog
      v-model="showAddTaskDialog"
      :title="isEditMode ? '编辑任务' : '新建任务'"
      width="600px"
    >
      <el-form :model="taskForm" :rules="taskRules" ref="taskFormRef">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="taskForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="任务类型" prop="category">
          <el-radio-group v-model="taskForm.category">
            <el-radio value="task">任务</el-radio>
            <el-radio value="note">笔记</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="taskForm.description" type="textarea" placeholder="请输入任务描述" :rows="3" />
        </el-form-item>
        <el-form-item label="所属项目" prop="project_id">
          <el-select v-model="taskForm.project_id" placeholder="选择项目" clearable>
            <el-option v-for="project in projects" :key="project.project_id" :label="project.name" :value="project.project_id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="start_date">
          <el-date-picker
            v-model="taskForm.start_date"
            type="datetime"
            placeholder="选择开始日期和时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="截止日期" prop="due_date">
          <el-date-picker
            v-model="taskForm.due_date"
            type="datetime"
            placeholder="选择截止日期和时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="taskForm.priority" placeholder="选择优先级">
            <el-option label="无" value="none" />
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item label="重复设置" prop="has_recurrence">
          <el-switch v-model="taskForm.has_recurrence" />
        </el-form-item>
        <template v-if="taskForm.has_recurrence">
          <el-form-item label="重复类型" prop="recurrence_category">
            <el-select v-model="taskForm.recurrence_category" placeholder="选择重复类型">
              <el-option label="每周" value="weekly" />
              <el-option label="每月" value="monthly" />
              <el-option label="每年" value="yearly" />
              <el-option label="隔N天" value="days" />
              <el-option label="隔N周" value="weeks" />
              <el-option label="艾宾浩斯记忆" value="ebinghaus" />
            </el-select>
          </el-form-item>
          <el-form-item label="重复次数" prop="recurrence_count">
          <el-input v-model.number="taskForm.recurrence_count" type="number" min="1" placeholder="设置重复次数" />
        </el-form-item>
      </template>
      <el-form-item label="标签">
        <el-select v-model="taskForm.selectedTags" multiple placeholder="选择标签">
          <el-option v-for="tag in tags" :key="tag.tag_id" :label="tag.name" :value="tag.tag_id" />
        </el-select>
      </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTaskDialog = false">取消</el-button>
        <el-button type="primary" @click="submitTaskForm">确定</el-button>
      </template>
    </el-dialog>
    <!-- 标签管理对话框 -->
    <el-dialog
      v-model="showTagsDialog"
      title="标签管理"
      width="500px"
    >
      <div class="tags-management">
        <el-card class="tags-card">
          <template #header>
            <div class="tags-card-header">
              <span>标签列表</span>
              <el-button type="primary" size="small" @click="showAddTagDialog = true">
                <el-icon><Plus /></el-icon> 新建标签
              </el-button>
            </div>
          </template>
          <el-table :data="tags" stripe style="width: 100%">
            <el-table-column prop="name" label="标签名称" />
            <el-table-column prop="color" label="颜色" width="120">
              <template #default="scope">
                <el-tag :type="getTagType(scope.row.color)">
                  {{ scope.row.color }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="scope">
                <el-button size="small" @click="editTag(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteTag(scope.row.tag_id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </el-dialog>
    
    <!-- 添加/编辑标签对话框 -->
    <el-dialog
      v-model="showAddTagDialog"
      :title="isEditingTag ? '编辑标签' : '新建标签'"
      width="400px"
    >
      <el-form :model="tagForm" :rules="tagRules" ref="tagFormRef">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="标签颜色" prop="color">
          <el-color-picker v-model="tagForm.color" show-alpha show-panel-color />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTagDialog = false">取消</el-button>
        <el-button type="primary" @click="submitTagForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Management } from '@element-plus/icons-vue'

export default {
  name: 'Tasks',
  components: {
    Plus,
    Management
  },
  data() {
    return {
      // 模拟数据
      tasks: [
        {
          task_id: 1,
          user_id: 1,
          project_id: 1,
          project_name: '网站开发',
          title: '设计网站原型',
          description: '创建网站的初步设计原型，包括首页、产品页等',
          category: 'task',
          status: 'todo',
          is_archived: false,
          parent_task_id: null,
          level: 0,
          due_date: '2023-11-10T18:00:00',
          start_date: '2023-11-05T09:00:00',
          completed_at: null,
          created_at: '2023-11-01T10:00:00',
          priority: 'high',
          tags: [
            { tag_id: 1, name: '设计', color: '#409eff' }
          ],
          recurrence_info: null
        },
        {
          task_id: 2,
          user_id: 1,
          project_id: 1,
          project_name: '网站开发',
          title: '前端页面开发',
          description: '基于设计原型开发前端页面',
          category: 'task',
          status: 'todo',
          is_archived: false,
          parent_task_id: null,
          level: 0,
          due_date: '2023-11-20T18:00:00',
          start_date: '2023-11-11T09:00:00',
          completed_at: null,
          created_at: '2023-11-01T10:00:00',
          priority: 'medium',
          tags: [
            { tag_id: 2, name: '前端', color: '#67c23a' }
          ],
          recurrence_info: null
        },
        {
          task_id: 3,
          user_id: 1,
          project_id: null,
          project_name: '无项目',
          title: '每周健身记录',
          description: '记录每周健身情况',
          category: 'note',
          status: 'todo',
          is_archived: false,
          parent_task_id: null,
          level: 0,
          due_date: null,
          start_date: null,
          completed_at: null,
          created_at: '2023-11-01T10:00:00',
          priority: 'low',
          tags: [],
          recurrence_info: {
            category: 'weekly',
            schedule: [1, 3, 5],
            count: 52
          }
        }
      ],
      projects: [
        { project_id: 1, name: '网站开发', color: '#409eff', icon: 'Document' },
        { project_id: 2, name: '学习计划', color: '#67c23a', icon: 'Book' }
      ],
      tags: [
        { tag_id: 1, name: '设计', color: '#409eff' },
        { tag_id: 2, name: '前端', color: '#67c23a' },
        { tag_id: 3, name: '重要', color: '#f56c6c' },
        { tag_id: 4, name: '紧急', color: '#e6a23c' }
      ],
      filterOptions: {
        status: '',
        project: '',
        priority: '',
        dateRange: null,
        tag: ''
      },
      showAddTaskDialog: false,
      isEditMode: false,
      showTagsDialog: false,
      showAddTagDialog: false,
      isEditingTag: false,
      taskForm: {
        title: '',
        description: '',
        category: 'task',
        project_id: null,
        start_date: null,
        due_date: null,
        priority: 'none',
        has_recurrence: false,
        recurrence_category: 'weekly',
        recurrence_count: 1,
        selectedTags: []
      },
      taskRules: {
        title: [
          { required: true, message: '请输入任务标题', trigger: 'blur' }
        ]
      },
      tagForm: {
        name: '',
        color: '#409eff'
      },
      tagRules: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' },
          { min: 1, max: 10, message: '标签名称长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        color: [
          { required: true, message: '请选择标签颜色', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    filteredTasks() {
      let filtered = [...this.tasks]
      
      // 状态筛选
      if (this.filterOptions.status) {
        filtered = filtered.filter(task => task.status === this.filterOptions.status)
      }
      
      // 项目筛选
      if (this.filterOptions.project) {
        filtered = filtered.filter(task => task.project_id === this.filterOptions.project)
      }
      
      // 优先级筛选
      if (this.filterOptions.priority) {
        filtered = filtered.filter(task => task.priority === this.filterOptions.priority)
      }
      
      // 日期范围筛选
      if (this.filterOptions.dateRange && this.filterOptions.dateRange.length === 2) {
        const [start, end] = this.filterOptions.dateRange
        filtered = filtered.filter(task => {
          if (!task.due_date) return false
          const dueDate = new Date(task.due_date)
          return dueDate >= start && dueDate <= end
        })
      }
      
      // 标签筛选
      if (this.filterOptions.tag) {
        filtered = filtered.filter(task => 
          task.tags && task.tags.some(tag => tag.tag_id === this.filterOptions.tag)
        )
      }
      
      return filtered
    }
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return '无'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    },
    getPriorityType(priority) {
      const typeMap = {
        'none': 'info',
        'low': 'success',
        'medium': 'warning',
        'high': 'danger'
      }
      return typeMap[priority] || 'info'
    },
    getPriorityText(priority) {
      const textMap = {
        'none': '无',
        'low': '低',
        'medium': '中',
        'high': '高'
      }
      return textMap[priority] || '无'
    },
    getTaskDueType(task) {
      if (!task.due_date) return 'info'
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      const tomorrow = new Date(today)
      tomorrow.setDate(tomorrow.getDate() + 1)
      const dueDate = new Date(task.due_date)
      
      if (task.status === 'done') return 'success'
      if (dueDate < today) return 'danger'
      if (dueDate < tomorrow) return 'warning'
      return 'info'
    },
    getRecurrenceText(recurrenceInfo) {
      if (!recurrenceInfo) return ''
      
      const { category, schedule, count } = recurrenceInfo
      
      switch (category) {
        case 'weekly':
          const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
          const dayTexts = schedule.map(day => days[day]).join('、')
          return `每周 ${dayTexts}，共 ${count} 次`
        case 'monthly':
          return `每月 ${schedule.join('、')} 号，共 ${count} 次`
        case 'yearly':
          return `每年 ${schedule[0]}，共 ${count} 次`
        case 'days':
          return `每 ${schedule || 1} 天，共 ${count} 次`
        case 'weeks':
          return `每 ${schedule || 1} 周，共 ${count} 次`
        case 'ebinghaus':
          return `艾宾浩斯记忆曲线，共 ${count} 次`
        default:
          return '自定义重复'
      }
    },
    getTagType(color) {
      // 根据颜色返回对应的Element Plus标签类型
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#e6a23c': 'warning',
        '#f56c6c': 'danger',
        '#909399': 'info'
      }
      return colorMap[color] || 'info'
    },
    updateTaskStatus(task) {
      const taskIndex = this.tasks.findIndex(t => t.task_id === task.task_id)
      if (taskIndex !== -1) {
        if (task.status === 'done') {
          this.tasks[taskIndex].status = 'done'
          this.tasks[taskIndex].completed_at = new Date().toISOString()
        } else {
          this.tasks[taskIndex].status = 'todo'
          this.tasks[taskIndex].completed_at = null
        }
        this.$message.success('任务状态已更新')
      }
    },
    editTask(task) {
      this.isEditMode = true
      this.taskForm = {
        task_id: task.task_id,
        title: task.title,
        description: task.description || '',
        category: task.category,
        project_id: task.project_id,
        start_date: task.start_date ? new Date(task.start_date) : null,
        due_date: task.due_date ? new Date(task.due_date) : null,
        priority: task.priority,
        has_recurrence: !!task.recurrence_info,
        recurrence_category: task.recurrence_info?.category || 'weekly',
        recurrence_count: task.recurrence_info?.count || 1,
        selectedTags: task.tags ? task.tags.map(tag => tag.tag_id) : []
      }
      this.showAddTaskDialog = true
    },
    deleteTask(taskId) {
      this.$confirm('确定要删除这个任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.tasks.findIndex(t => t.task_id === taskId)
        if (index !== -1) {
          this.tasks.splice(index, 1)
          this.$message.success('任务删除成功')
        }
      }).catch(() => {
        // 取消删除
      })
    },
    submitTaskForm() {
      this.$refs.taskFormRef.validate((valid) => {
        if (valid) {
          if (this.isEditMode) {
            // 编辑现有任务
            const index = this.tasks.findIndex(t => t.task_id === this.taskForm.task_id)
            if (index !== -1) {
              const updatedTask = {
                ...this.tasks[index],
                title: this.taskForm.title,
                description: this.taskForm.description,
                category: this.taskForm.category,
                project_id: this.taskForm.project_id,
                project_name: this.taskForm.project_id ? this.projects.find(p => p.project_id === this.taskForm.project_id)?.name : '无项目',
                start_date: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
                due_date: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
                priority: this.taskForm.priority,
                tags: this.taskForm.selectedTags.map(tagId => 
                  this.tags.find(tag => tag.tag_id === tagId)
                ).filter(Boolean)
              }
              
              if (this.taskForm.has_recurrence) {
                updatedTask.recurrence_info = {
                  category: this.taskForm.recurrence_category,
                  count: this.taskForm.recurrence_count
                  // schedule会根据category在后端生成
                }
              } else {
                updatedTask.recurrence_info = null
              }
              
              this.tasks[index] = updatedTask
              this.$message.success('任务更新成功')
            }
          } else {
            // 创建新任务
            const newTask = {
              task_id: Date.now(),
              user_id: 1, // 模拟用户ID
              project_id: this.taskForm.project_id,
              project_name: this.taskForm.project_id ? this.projects.find(p => p.project_id === this.taskForm.project_id)?.name : '无项目',
              title: this.taskForm.title,
              description: this.taskForm.description,
              category: this.taskForm.category,
              status: 'todo',
              is_archived: false,
              parent_task_id: null,
              level: 0,
              due_date: this.taskForm.due_date ? this.taskForm.due_date.toISOString() : null,
              start_date: this.taskForm.start_date ? this.taskForm.start_date.toISOString() : null,
              completed_at: null,
              created_at: new Date().toISOString(),
              priority: this.taskForm.priority,
              tags: this.taskForm.selectedTags.map(tagId => 
                this.tags.find(tag => tag.tag_id === tagId)
              ).filter(Boolean),
              recurrence_info: null
            }
            
            if (this.taskForm.has_recurrence) {
              newTask.recurrence_info = {
                category: this.taskForm.recurrence_category,
                count: this.taskForm.recurrence_count
              }
            }
            
            this.tasks.push(newTask)
            this.$message.success('任务创建成功')
          }
          
          // 关闭对话框并重置表单
          this.showAddTaskDialog = false
          this.resetTaskForm()
        }
      })
    },
    resetTaskForm() {
      this.taskForm = {
        title: '',
        description: '',
        category: 'task',
        project_id: null,
        start_date: null,
        due_date: null,
        priority: 'none',
        has_recurrence: false,
        recurrence_category: 'weekly',
        recurrence_count: 1,
        selectedTags: []
      }
      this.isEditMode = false
      if (this.$refs.taskFormRef) {
        this.$refs.taskFormRef.resetFields()
      }
    },
    resetTagForm() {
      this.tagForm = {
        name: '',
        color: '#409eff'
      }
      this.isEditingTag = false
      if (this.$refs.tagFormRef) {
        this.$refs.tagFormRef.resetFields()
      }
    },
    editTag(tag) {
      this.isEditingTag = true
      this.tagForm = {
        tag_id: tag.tag_id,
        name: tag.name,
        color: tag.color
      }
      this.showAddTagDialog = true
    },
    deleteTag(tagId) {
      // 检查是否有任务使用此标签
      const hasTasksWithTag = this.tasks.some(task => 
        task.tags && task.tags.some(tag => tag.tag_id === tagId)
      )
      
      if (hasTasksWithTag) {
        this.$message.warning('此标签已被任务使用，无法删除')
        return
      }
      
      this.$confirm('确定要删除这个标签吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.tags.findIndex(t => t.tag_id === tagId)
        if (index !== -1) {
          this.tags.splice(index, 1)
          this.$message.success('标签删除成功')
        }
      }).catch(() => {
        // 取消删除
      })
    },
    getTagType(color) {
      // 根据颜色值判断对应的el-tag类型
      const colorMap = {
        '#409eff': 'primary',
        '#67c23a': 'success',
        '#f56c6c': 'danger',
        '#e6a23c': 'warning',
        '#909399': 'info'
      }
      return colorMap[color] || 'info'
    },
    submitTagForm() {
      this.$refs.tagFormRef.validate((valid) => {
        if (valid) {
          if (this.isEditingTag) {
            // 编辑现有标签
            const index = this.tags.findIndex(t => t.tag_id === this.tagForm.tag_id)
            if (index !== -1) {
              this.tags[index] = {
                ...this.tags[index],
                name: this.tagForm.name,
                color: this.tagForm.color
              }
              // 更新使用此标签的任务
              this.tasks.forEach(task => {
                if (task.tags && task.tags.length > 0) {
                  task.tags.forEach(tag => {
                    if (tag.tag_id === this.tagForm.tag_id) {
                      tag.name = this.tagForm.name
                      tag.color = this.tagForm.color
                    }
                  })
                }
              })
              this.$message.success('标签更新成功')
            }
          } else {
            // 检查标签名称是否已存在
            const existingTag = this.tags.find(t => t.name === this.tagForm.name)
            if (existingTag) {
              this.$message.warning('标签名称已存在')
              return
            }
            
            // 创建新标签
            const newTag = {
              tag_id: Date.now(),
              name: this.tagForm.name,
              color: this.tagForm.color
            }
            
            this.tags.push(newTag)
            this.$message.success('标签创建成功')
          }
          
          // 关闭对话框并重置表单
          this.showAddTagDialog = false
          this.resetTagForm()
        }
      })
    }
  }
}
</script>

<style scoped>
.tasks-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.filter-section {
  margin-bottom: 20px;
}

.tasks-card {
  border-radius: 8px;
}

.tags-card {
  margin-bottom: 20px;
}

.tags-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tags-management {
  padding: 10px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  color: #303133;
}

.task-title-container {
  display: flex;
  align-items: center;
}

.task-completed {
  text-decoration: line-through;
  color: #909399;
}

.task-detail {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.detail-item {
  margin-bottom: 8px;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-item strong {
  color: #606266;
  margin-right: 8px;
}

.detail-item p {
  margin: 5px 0 0 0;
  color: #303133;
}
</style>