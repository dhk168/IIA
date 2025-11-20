<template>
  <LightDialog
      v-model="dialogVisible"
      title="Tags Management"
      width="500px"
      @close="handleClose"
    >
      <div class="tag-management">
        <!-- Tags List -->
        <el-card class="tags-card">
          <div class="tags-card-header">
            <span>Tags List</span>
            <el-button type="primary" size="small" @click="showAddTagDialog = true">
              <el-icon><Plus /></el-icon> New Tag
            </el-button>
          </div>
          <light-table :data="tagList" stripe style="width: 100%">
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
          </light-table>
        </el-card>
      </div>
    </LightDialog>

    <!-- Add/Edit Tag Dialog -->
    <LightDialog
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
    </LightDialog>

    <!-- Delete Confirm Dialog -->
    <LightDialog
      v-model="showDeleteConfirm"
      title="Delete Tag"
      width="400px"
    >
      <div>Are you sure you want to delete this tag?</div>
      <template #footer>
        <el-button @click="handleDeleteCancel">No</el-button>
        <el-button type="danger" @click="handleDeleteConfirm">Yes</el-button>
      </template>
    </LightDialog>
</template>

<script>
import { Plus } from '@element-plus/icons-vue'
import { tagAPI, taskTagAPI } from '@/api/reminder'
import LightDialog from '@/components/LightDialog.vue'
import LightTable from '@/components/LightTable.vue'

export default {
  inject: ['showToast'],
  name: 'TagManagement',
  emits: ['tags-updated', 'close', 'update:model-value'],
  components: {
    Plus,
    LightDialog,
    LightTable
  },
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
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
      dialogVisible: this.modelValue,
      showAddTagDialog: false,
      showDeleteConfirm: false,
      isEditingTag: false,
      tagList: [],
      tagToDelete: null,
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
    },
    modelValue: {
      handler(newVal) {
        this.dialogVisible = newVal
      },
      immediate: true
    },
    dialogVisible: {
      handler(newVal) {
        this.$emit('update:model-value', newVal)
      },
      immediate: true
    }
  },
  mounted() {
    this.loadTags()
  },
  methods: {
    handleClose() {
      this.$emit('close');
    },
    
    getTagType(color) {
      // Determine the corresponding el-tag type based on the color value
      if (!color) return 'info'
      
      // Simple color judgment logic
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
        // According to the judgment logic in Projects.vue, check if response.code === 200
        if (response && response.code === 200) {
          this.tagList = response.data || []
          this.$emit('tags-updated', this.tagList)
        } else {
          console.warn('API returned non-success response:', response)
          this.tagList = []
          this.showToast('error', 'Failed to load tags')
        }
      } catch (error) {
        console.error('Failed to load tags:', error)
        this.showToast('error', 'Failed to load tags')
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
      this.tagToDelete = tagId
      this.showDeleteConfirm = true
    },
    
    async handleDeleteConfirm() {
      try {
        // Delete all task tag associations related to the tag
        const response = await taskTagAPI.deleteTaskTagsByTagId(this.tagToDelete)
        
        // According to the judgment logic in Projects.vue, check if response.code === 200
        if (response && response.code === 200) {
          // Update local data
          const updatedTags = this.tagList.filter(tag => tag.tagId !== this.tagToDelete)
          this.tagList = updatedTags
          this.$emit('tags-updated', this.tagList)
          
          // Update tags in tasks
          this.tasks.forEach(task => {
            if (task.tags && task.tags.length > 0) {
              task.tags = task.tags.filter(tag => tag.tagId !== this.tagToDelete)
            }
          })
          
          this.showToast('success', 'Tag deleted successfully')
        } else {
          this.showToast('error', 'Failed to delete tag')
        }
      } catch (error) {
        console.error('Failed to delete tag:', error)
        this.showToast('error', 'Failed to delete tag')
      } finally {
        this.showDeleteConfirm = false
      }
    },
    
    handleDeleteCancel() {
      this.showDeleteConfirm = false
    },
    
    async submitTagForm() {
      this.$refs.tagFormRef.validate(async (valid) => {
        if (valid) {
          try {
            let result
            
            if (this.isEditingTag) {
              // Update existing tag
              result = await tagAPI.updateTag(this.tagForm.tagId, {
                name: this.tagForm.name,
                color: this.tagForm.color
              })
              
              if (result.success) {
                const index = this.tagList.findIndex(t => t.tagId === this.tagForm.tagId)
                if (index > -1) {
                  this.tagList[index] = result.data
                  
                  // Update tags in tasks
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
                this.showToast('success', 'Tag updated successfully')
              }
            } else {
              // Check if the tag name already exists
              const exists = await tagAPI.existsByName(this.tagForm.name)
              if (exists) {
                this.showToast('warning', 'Tag name already exists')
                return
              }
              
              // Create new tag
              result = await tagAPI.createTag({
                name: this.tagForm.name,
                color: this.tagForm.color
              })
              
              if (result.success) {
                this.tagList.push(result.data)
                this.showToast('success', 'Tag created successfully')
              }
            }
            
            // According to the judgment logic in Projects.vue, check if response.code === 200
            if (result && result.code === 200) {
              // Update or add new tag to the list
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
              
              this.showToast('success', this.isEditingTag ? 'Tag updated successfully' : 'Tag created successfully')
            } else {
              this.showToast('error', 'Operation failed')
            }
          } catch (error) {
            console.error('Failed to submit tag form:', error)
            this.showToast('error', 'Failed to submit tag form')
          }
        }
      })
    }
  }
}
</script>

<style scoped>
  .tags-card-header {
    span{
      font-size: 16px;
      font-weight: 600;
      color: rgb(200, 200, 200);
    }
  }
</style>