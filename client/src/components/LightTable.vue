<template>
  <el-table
    class="light-table"
    v-bind="$attrs"
  >
    <slot></slot>
  </el-table>
</template>

<script>
export default {
  name: 'LightTable',
  inheritAttrs: false
}
</script>

<style scoped>
.light-table { 
  width: 100%; 
  max-width: 100%; 
  box-sizing: border-box; 
  background: rgba(80, 80, 80, 0.1) !important; 
  backdrop-filter: blur(25px) !important; 
  -webkit-backdrop-filter: blur(25px) !important; 
  border: 1px solid rgba(255, 255, 255, 0.3) !important; 
  border-radius: 14px !important; 
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2) !important; 
  padding: 4px; margin-bottom: 16px; transition: all 0.3s ease !important; 
} 

.light-table :deep(.el-table, .el-table__inner, .el-table__view, .el-table__container, .el-table__body, .el-table__header) {
  background-color: transparent !important;
  overflow-x: hidden !important;
}

.light-table :deep(.el-table__inner) {
  background-color: transparent !important;
  color: rgba(255, 255, 255, 1) !important;
  overflow-x: hidden !important;
}

.light-table :deep(.el-table__inner-wrapper) {
  background-color: transparent !important;
  overflow-x: hidden !important;
}

.light-table :deep(.el-table__header-wrapper) {
  background-color: transparent !important;
  border-bottom: none !important;
  overflow-x: hidden !important;
}

.light-table :deep(.el-table__body-wrapper) {
  background-color: transparent !important;
  overflow-x: hidden !important;
}

.light-table :deep(.el-table__header) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__body) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__view) {
  background-color: transparent !important;
}

/* Ensure table and thead backgrounds are transparent */
.light-table :deep(table) {
  background-color: transparent !important;
  overflow-x: hidden !important;
}

.light-table :deep(thead) {
  background-color: transparent !important;
}

/* Ensure all tr elements are transparent */
.light-table :deep(tr) {
  background-color: transparent !important;
}

/* Ensure striped rows and their cells are transparent */
.light-table :deep(.el-table__row--striped) {
  background-color: transparent !important;
}

.light-table :deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: transparent !important;
}

/* Ensure hover rows have transparent background */
.light-table :deep(.el-table__body tr:hover) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__body tr:hover td) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__header th) {
  background-color: transparent !important;
  color: rgba(255, 255, 255, 0.9) !important;
  font-weight: 600 !important;
  border-bottom: 2px solid rgba(255, 255, 255, 0.2) !important;
}

.light-table :deep(.el-table__body td) {
  color: rgba(255, 255, 255, 1) !important;
  border-bottom: 2px solid rgba(255, 255, 255, 0.1) !important;
  background-color: transparent !important;
}

.light-table :deep(.el-table__cell) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__body-wrapper) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__header-wrapper) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__body td  *:not(.el-tag)) {
  color: rgb(255, 255, 255) !important;
}

/* Ensure expand column and expanded cell backgrounds are transparent */
.light-table :deep(.el-table__expand-column) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__expanded-cell) {
  background-color: transparent !important;
}

/* Ensure all table containers are transparent */
.light-table :deep(.el-table__container) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__inner-wrapper) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__body-inner) {
  background-color: transparent !important;
}

.light-table :deep(.el-table__empty-block) {
  background-color: transparent !important;
}

/* Set all elements inside the table to transparent background */
.light-table :deep(*) {
  background: transparent !important;
  background-color: transparent !important;
  background-image: none !important;
  /* border: 5px !important; */
}

/* Preserve background colors for tags and task details */
.light-table :deep(.el-tag),
.light-table :deep(.task-detail) {
  background-color: inherit !important;
}

/* Make table edge lines thicker */
.light-table :deep(.el-table) {
  border-width: 2px !important;
}

.light-table :deep(.el-table__inner) {
  border-width: 2px !important;
}

.light-table :deep(.el-table__header-wrapper),
.light-table :deep(.el-table__body-wrapper) {
  border-width: 2px !important;
}

.light-table :deep(.el-table__body td) {
  border-width: 2px !important;
}



</style>