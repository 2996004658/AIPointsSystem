<!-- 积分历史记录组件 -->
<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Points History</span>
          <el-button type="primary" @click="fetchHistory">Refresh</el-button>
        </div>
      </template>
      <el-table :data="history" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="User ID" width="100" />
        <el-table-column prop="username" label="Username" />
        <el-table-column prop="type" label="Type" />
        <el-table-column prop="points" label="Points" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.points > 0 ? 'success' : 'danger'">
              {{ scope.row.points > 0 ? '+' + scope.row.points : scope.row.points }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Description" />
        <el-table-column prop="createdAt" label="Time" />
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

// 历史记录数据
const history = ref([])
const loading = ref(false)
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取历史记录
const fetchHistory = async () => {
  loading.value = true
  try {
    const response = await api.get('/admin/points/history', {
      params: {
        page: currentPage.value - 1,
        size: pageSize.value
      }
    })
    if (response.data.code === 200) {
      history.value = response.data.data.content
      total.value = response.data.data.totalElements
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 处理页码变更
const handlePageChange = (page) => {
  currentPage.value = page
  fetchHistory()
}

onMounted(() => {
  fetchHistory()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
