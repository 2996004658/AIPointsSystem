<!-- 用户列表组件 -->
<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>User List</span>
          <el-button type="primary" @click="fetchUsers">Refresh</el-button>
        </div>
      </template>
      <el-table :data="users" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="Username" />
        <el-table-column prop="points" label="Points" width="100" />
        <el-table-column prop="deviceId" label="Device ID" />
        <el-table-column prop="openid" label="OpenID" />
        <el-table-column prop="createdAt" label="Created At" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

// 用户数据列表
const users = ref([])
// 加载状态
const loading = ref(false)

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await api.get('/admin/users')
    if (response.data.code === 200) {
      users.value = response.data.data
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
