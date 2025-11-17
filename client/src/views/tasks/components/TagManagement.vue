<template>
  <div class="tag-management">
    <!-- Tags List -->
    <el-card class="tags-card">
      <div class="tags-card-header">
        <span>Tags List</span>
        <el-button type="primary" size="small" @click="showAddTagDialog = true">
          <el-icon><Plus /></el-icon> New Tag
        </el-button>
      </div>
      <el-table :data="tagList" stripe style="width: 100%">
        <el-table-column prop="name" label="Tag Name" />
        <el-table-column label="Color">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.color)">
              {{ scope.row.color }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150">
          <template #default="scope">
            <el-button size="small" @click="editTag(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteTag(scope.row.tagId)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Tag Dialog -->
    <el-dialog
      v-model="showAddTagDialog"
      :title="isEditingTag ? 'Edit Tag' : 'New Tag'"
      width="400px"
    >
      <el-form :model="tagForm" :rules="tagRules" ref="tagFormRef">
        <el-form-item label="Tag Name" prop="name">
          <el-input v-model="tagForm.name" placeholder="Enter tag name" />
        </el-form-item>
        <el-form-item label="Tag Color" prop="color">
          <el-color-picker v-model="tagForm.color" show-alpha show-panel-color />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddTagDialog = false">Cancel</el-button>
        <el-button type="primary" @click="submitTagForm">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import { tagAPI, taskTagAPI } from '@/api/reminder'

export default {
  name: 'TagManagement',
  components: {
    Plus
  },
  props: {
    tags: {
      type: Array,
      default: () => []
    },
    tasks: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      showAddTagDialog: false,
      isEditingTag: false,
      tagList: [],
      tagForm: {
        tagId: '',
        name: '',
        color: ''
      },
      tagRules: {
        name: [
          { required: true, message: 'Please enter tag name', trigger: 'blur' },
          { min: 1, max: 10, message: 'Tag name length should be between 1 and 10 characters', trigger: 'blur' }
        ],
        color: [
          { required: true, message: 'Please select tag color', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    tags: {
      handler(newTags) {
        this.tagList = newTags
      },
      immediate: true
    }
  },
  mounted() {
    this.loadTags()
  },
  methods: {
    // 添加毛玻璃提示方法
    createGlassToast(type, message) {
      const toast = document.createElement('div');
      toast.className = `glass-toast glass-toast-${type}`;
      toast.textContent = message;
      document.body.appendChild(toast);
      
      // 自动移除
      setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
      }, 4000);
      
      setTimeout(() => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      }, 4500);
      
      toast.addEventListener('click', () => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast);
        }
      });
    },
    
    getTagType(color) {
      // 根据颜色值判断对应的el-tag类型
      if (!color) return 'info'
      
      // 简单的颜色判断逻辑
      const colorLower = color.toLowerCase()
      if (colorLower.includes('red') || colorLower.includes('f44336')) return 'danger'
      if (colorLower.includes('green') || colorLower.includes('4caf50')) return 'success'
      if (colorLower.includes('blue') || colorLower.includes('2196f3')) return 'primary'
      if (colorLower.includes('orange') || colorLower.includes('ff9800')) return 'warning'
      return 'info'
    },
    
    async loadTags() {
      try {
        const response = await tagAPI.getAllTags()
        // 根据Projects.vue的判断逻辑，检查response.code === 200
        if (response && response.code === 200) {
          this.tagList = response.data || []
          this.$emit('tags-updated', this.tagList)
        } else {
          console.warn('API returned non-success response:', response)
          this.tagList = []
          this.createGlassToast('error', 'Failed to load tags')
        }
      } catch (error) {
        console.error('Failed to load tags:', error)
        this.createGlassToast('error', 'Failed to load tags')
      }
    },
    
    resetTagForm() {
      this.tagForm = {
        tagId: '',
        name: '',
        color: ''
      }
      this.isEditingTag = false
      if (this.$refs.tagFormRef) {
        this.$refs.tagFormRef.resetFields()
      }
    },
    
    editTag(tag) {
      this.isEditingTag = true
      this.tagForm = {
        tagId: tag.tagId,
        name: tag.name,
        color: tag.color
      }
      this.showAddTagDialog = true
    },
    
    async deleteTag(tagId) {
      try {
        await this.$confirm('Are you sure you want to delete this tag?', 'Confirmation', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning'
        })
        
        // 删除标签相关的所有任务标签关联
        const response = await taskTagAPI.deleteTaskTagsByTagId(tagId)
        
        // 根据Projects.vue的判断逻辑，检查response.code === 200
        if (response && response.code === 200) {
          // 更新本地数据
          const updatedTags = this.tagList.filter(tag => tag.tagId !== tagId)
          this.tagList = updatedTags
          this.$emit('tags-updated', this.tagList)
          
          // 更新任务中的标签
          this.tasks.forEach(task => {
            if (task.tags && task.tags.length > 0) {
              task.tags = task.tags.filter(tag => tag.tagId !== tagId)
            }
          })
          
          this.createGlassToast('success', 'Tag deleted successfully')
        } else {
          this.createGlassToast('error', 'Failed to delete tag')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to delete tag:', error)
          this.createGlassToast('error', 'Failed to delete tag')
        }
      }
    },
    
    async submitTagForm() {
      this.$refs.tagFormRef.validate(async (valid) => {
        if (valid) {
          try {
            let result
            
            if (this.isEditingTag) {
              // 更新现有标签
              result = await tagAPI.updateTag(this.tagForm.tagId, {
                name: this.tagForm.name,
                color: this.tagForm.color
              })
              
              if (result.success) {
                const index = this.tagList.findIndex(t => t.tagId === this.tagForm.tagId)
                if (index > -1) {
                  this.tagList[index] = result.data
                  
                  // 更新任务中的标签
                  this.tasks.forEach(task => {
                    if (task.tags && task.tags.length > 0) {
                      task.tags.forEach(tag => {
                        if (tag.tagId === this.tagForm.tagId) {
                          tag.name = this.tagForm.name
                          tag.color = this.tagForm.color
                        }
                      })
                    }
                  })
                }
                this.createGlassToast('success', 'Tag updated successfully')
              }
            } else {
              // 检查标签名是否已存在
              const exists = await tagAPI.existsByName(this.tagForm.name)
              if (exists) {
                this.createGlassToast('warning', 'Tag name already exists')
                return
              }
              
              // 创建新标签
              result = await tagAPI.createTag({
                name: this.tagForm.name,
                color: this.tagForm.color
              })
              
              if (result.success) {
                this.tagList.push(result.data)
                this.createGlassToast('success', 'Tag created successfully')
              }
            }
            
            // 根据Projects.vue的判断逻辑，检查response.code === 200
            if (result && result.code === 200) {
              // 更新或添加新标签到列表中
              if (this.isEditingTag && result.data) {
                const index = this.tagList.findIndex(t => t.tagId === this.tagForm.tagId)
                if (index > -1) {
                  this.tagList[index] = result.data
                }
              } else if (!this.isEditingTag && result.data) {
                this.tagList.push(result.data)
              }
              
              this.$emit('tags-updated', this.tagList)
              this.showAddTagDialog = false
              this.resetTagForm()
              
              this.createGlassToast('success', this.isEditingTag ? 'Tag updated successfully' : 'Tag created successfully')
            } else {
              this.createGlassToast('error', 'Operation failed')
            }
          } catch (error) {
            console.error('Failed to submit tag form:', error)
            this.createGlassToast('error', 'Failed to submit tag form')
          }
        }
      })
    }
  }
}
</script>
