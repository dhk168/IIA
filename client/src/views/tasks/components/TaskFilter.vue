<template>
    <el-row :gutter="20">
      <el-col :md="6" :sm="12">
        <LightSelect v-model="localFilter.status" placeholder="Task Status" clearable :options="statusOptions" />
      </el-col>
      <el-col :md="6" :sm="12">
        <LightSelect v-model="localFilter.priority" placeholder="Priority" clearable :options="priorityOptions" />
      </el-col>
      <el-col :md="6" :sm="12">
        <LightSelect v-model="localFilter.tag" placeholder="Tags Filter" clearable :options="tagOptions" />
      </el-col>
      <el-col :md="6" :sm="12">
        <LightSelect v-model="localFilter.project" placeholder="Project Filter" clearable :options="projectOptions" />
      </el-col>
      <el-col :md="12" :sm="24">
        <LightDate v-model="localFilter.dateRange" type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date" />
      </el-col>
    </el-row>
</template>

<script>
import LightSelect from '@/components/LightSelect.vue';
import LightDate from '@/components/LightDate.vue';

export default {
  name: 'TaskFilter',
  components: {
    LightSelect,
    LightDate
  },
  props: {
    tags: {
      type: Array,
      default: () => []
    },
    projects: {
      type: Array,
      default: () => []
    },
    filter: {
      type: Object,
      default: () => ({
        status: '',
        priority: '',
        dateRange: null,
        tag: '',
        project: ''
      })
    }
  },
  data() {
    return {
      localFilter: { ...this.filter },
      statusOptions: [
        { label: 'All', value: '' },
        { label: 'To Do', value: 'todo' },
        { label: 'Completed', value: 'done' },
        { label: 'Abandoned', value: 'abandoned' }
      ],
      priorityOptions: [
        { label: 'All', value: '' },
        { label: 'None', value: 'none' },
        { label: 'Low', value: 'low' },
        { label: 'Medium', value: 'medium' },
        { label: 'High', value: 'high' }
      ]
    };
  },
  computed: {
    tagOptions() {
      const options = [{ label: 'All', value: '' }];
      if (Array.isArray(this.tags)) {
        options.push(...this.tags.map(tag => ({ label: tag.name, value: tag.tagId })));
      }
      return options;
    },
    projectOptions() {
      const sortedProjects = [...this.projects].sort((a, b) => {
        const nameA = (a?.name || '').toLowerCase();
        const nameB = (b?.name || '').toLowerCase();
        return nameA.localeCompare(nameB);
      });
      
      const options = [{ label: 'All', value: '' }];
      if (Array.isArray(sortedProjects)) {
        options.push(...sortedProjects.map(project => ({ label: project.name, value: project.projectId })));
      }
      return options;
    }
  },
  watch: {
    filter: {
      deep: true,
      immediate: true,
      handler(newFilter) {
        if (JSON.stringify(newFilter) !== JSON.stringify(this.localFilter)) {
          this.localFilter = { ...newFilter };
        }
      }
    },
    localFilter: {
      deep: true,
      handler(newFilter) {
        if (JSON.stringify(newFilter) !== JSON.stringify(this.filter)) {
          this.$emit('update:filter', newFilter);
        }
      }
    }
  }
};
</script>
