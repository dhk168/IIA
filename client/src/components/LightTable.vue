<template>
  <el-table
    class="light-table"
    v-bind="$attrs"
    v-on="$listeners"
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
  background: rgba(50, 50, 95, 0.8) !important;
  backdrop-filter: blur(30px) saturate(1.2) !important;
  -webkit-backdrop-filter: blur(30px) saturate(1.2) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 14px !important;
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37) !important;
}

/* Header styles */
.light-table .el-table__header-wrapper {
  border-bottom: none !important;
}

.light-table .el-table__header th {
  background-color: transparent !important;
  color: rgba(255, 255, 255, 0.9) !important;
  font-weight: 600 !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2) !important;
}

/* Body styles */
.light-table .el-table__body-wrapper {
  background-color: transparent !important;
}

.light-table .el-table__body tr {
  background-color: transparent !important;
}

.light-table .el-table__body td {
  color: rgba(255, 255, 255, 0.8) !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1) !important;
}

/* Hover and active states */
.light-table .el-table__body tr:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.light-table .el-table__body tr.current-row {
  background-color: rgba(255, 255, 255, 0.15) !important;
}

/* Border styles */
.light-table .el-table__inner-wrapper::before,
.light-table .el-table__inner-wrapper::after {
  background-color: transparent !important;
}

.light-table .el-table::before {
  background-color: transparent !important;
}

/* Input styles */
.light-table .el-input {
  .el-input__wrapper {
    background: rgba(0, 0, 0, 0.3) !important;
    .el-input__inner {
      color: rgba(255, 255, 255, 0.9) !important;
    }
  }
}

/* Textarea styles */
.light-table .el-textarea {
  .el-textarea__inner {
    background: rgba(0, 0, 0, 0.3) !important;
    color: rgba(255, 255, 255, 0.9) !important;
  }
}

/* Select styles */
.light-table .el-select .el-select__wrapper {
  background: rgba(0, 0, 0, 0.3) !important;
}

.light-table .el-select .el-select__wrapper .el-select__placeholder {
  color: rgba(255, 255, 255, 0.7) !important;
}

.light-table .el-select .el-select__wrapper .el-select__input {
  color: rgba(255, 255, 255, 0.9) !important;
}

/* Custom select styles (if any) */
.light-table .v3ip__custom-select .v3ip__selected,
.light-table .v3ip__custom-select div,
.light-table .v3ip__custom-select input {
  background: rgba(0, 0, 0, 0.3) !important;
  color: rgba(255, 255, 255, 0.9) !important;
}

/* Pagination styles (if used) */
.light-table + .el-pagination {
  color: rgba(255, 255, 255, 0.8) !important;
  margin-top: 16px;
}

.light-table + .el-pagination .btn-prev,
.light-table + .el-pagination .btn-next,
.light-table + .el-pagination .el-pager li {
  background-color: rgba(255, 255, 255, 0.2) !important;
  border-color: rgba(255, 255, 255, 0.3) !important;
  color: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(10px) !important;
  -webkit-backdrop-filter: blur(10px) !important;
  border-radius: 4px !important;
}

.light-table + .el-pagination .btn-prev:hover,
.light-table + .el-pagination .btn-next:hover,
.light-table + .el-pagination .el-pager li:hover {
  background-color: rgba(255, 255, 255, 0.3) !important;
}

.light-table + .el-pagination .el-pager li.is-active {
  background-color: rgba(255, 255, 255, 0.4) !important;
}
</style>