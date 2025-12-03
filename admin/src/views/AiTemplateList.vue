<!-- AI图片模板列表管理页面 -->
<template>
  <div class="ai-template-container">
    <!-- 顶部操作栏 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索模板标题"
            clearable
            @clear="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterCategory" placeholder="选择分类" clearable @change="handleFilter">
            <el-option label="全部分类" value="" />
            <el-option label="工作" value="工作" />
            <el-option label="生活" value="生活" />
            <el-option label="学习" value="学习" />
            <el-option label="NSFW" value="NSFW" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterMode" placeholder="选择模式" clearable @change="handleFilter">
            <el-option label="全部模式" value="" />
            <el-option label="Generate" value="generate" />
            <el-option label="Edit" value="edit" />
          </el-select>
        </el-col>
        <el-col :span="10" class="button-group">
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button type="success" @click="handleImport" :loading="importing">
            <el-icon><Download /></el-icon>
            导入数据
          </el-button>
          <el-button type="warning" @click="handleReimport" :loading="importing">
            <el-icon><Refresh /></el-icon>
            重新导入
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 统计信息 -->
    <el-card class="stats-card" v-if="stats">
      <el-statistic title="模板总数" :value="stats.totalCount" />
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table :data="templateList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="预览图" width="120">
          <template #default="{ row }">
            <el-image
              v-if="row.preview"
              :src="row.preview"
              :preview-src-list="[row.preview]"
              fit="cover"
              style="width: 80px; height: 60px; border-radius: 4px;"
            />
            <span v-else class="no-preview">无预览</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subCategory" label="子分类" width="120" show-overflow-tooltip />
        <el-table-column prop="mode" label="模式" width="100">
          <template #default="{ row }">
            <el-tag :type="row.mode === 'generate' ? 'success' : 'info'">
              {{ row.mode }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewDetail(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        class="pagination"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="模板详情" width="800px">
      <div v-if="currentTemplate" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="ID">{{ currentTemplate.id }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ currentTemplate.title }}</el-descriptions-item>
          <el-descriptions-item label="分类">
            <el-tag>{{ currentTemplate.category }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="子分类">{{ currentTemplate.subCategory || '-' }}</el-descriptions-item>
          <el-descriptions-item label="模式">
            <el-tag :type="currentTemplate.mode === 'generate' ? 'success' : 'info'">
              {{ currentTemplate.mode }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="作者">{{ currentTemplate.author }}</el-descriptions-item>
          <el-descriptions-item label="来源链接" :span="2">
            <el-link :href="currentTemplate.link" target="_blank" type="primary">
              {{ currentTemplate.link }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ formatDate(currentTemplate.createTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="preview-section" v-if="currentTemplate.preview">
          <h4>预览图</h4>
          <el-image :src="currentTemplate.preview" fit="contain" style="max-width: 100%;" />
        </div>

        <div class="prompt-section">
          <h4>提示词内容</h4>
          <el-input
            v-model="currentTemplate.prompt"
            type="textarea"
            :rows="10"
            readonly
            class="prompt-textarea"
          />
          <el-button type="primary" size="small" @click="copyPrompt" class="copy-btn">
            <el-icon><DocumentCopy /></el-icon>
            复制提示词
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Download, Refresh, DocumentCopy } from '@element-plus/icons-vue'
import api from '../api'

// 数据状态
const templateList = ref([])
const loading = ref(false)
const importing = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const stats = ref(null)

// 筛选条件
const searchKeyword = ref('')
const filterCategory = ref('')
const filterMode = ref('')

// 详情对话框
const detailVisible = ref(false)
const currentTemplate = ref(null)

// 加载模板列表
const loadTemplates = async () => {
  loading.value = true
  try {
    const response = await api.get('/ai-template/list')
    if (response.data.success) {
      let list = response.data.data || []
      
      // 应用筛选
      if (filterCategory.value) {
        list = list.filter(item => item.category === filterCategory.value)
      }
      if (filterMode.value) {
        list = list.filter(item => item.mode === filterMode.value)
      }
      if (searchKeyword.value) {
        list = list.filter(item => 
          item.title.toLowerCase().includes(searchKeyword.value.toLowerCase())
        )
      }
      
      total.value = list.length
      
      // 分页
      const start = (currentPage.value - 1) * pageSize.value
      const end = start + pageSize.value
      templateList.value = list.slice(start, end)
    }
  } catch (error) {
    ElMessage.error('加载模板列表失败')
  } finally {
    loading.value = false
  }
}

// 加载统计信息
const loadStats = async () => {
  try {
    const response = await api.get('/ai-template/stats')
    if (response.data.success) {
      stats.value = response.data.data
    }
  } catch (error) {
    console.error('加载统计信息失败', error)
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadTemplates()
}

// 筛选
const handleFilter = () => {
  currentPage.value = 1
  loadTemplates()
}

// 导入数据
const handleImport = async () => {
  try {
    await ElMessageBox.confirm('确定要从GitHub导入数据吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    importing.value = true
    const response = await api.post('/ai-template/import')
    if (response.data.success) {
      ElMessage.success(`导入成功！共导入 ${response.data.data.importedCount} 条数据`)
      loadTemplates()
      loadStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('导入失败')
    }
  } finally {
    importing.value = false
  }
}

// 重新导入数据
const handleReimport = async () => {
  try {
    await ElMessageBox.confirm('重新导入会清空现有数据，确定继续吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    importing.value = true
    const response = await api.post('/ai-template/reimport')
    if (response.data.success) {
      ElMessage.success(`重新导入成功！共导入 ${response.data.data.importedCount} 条数据`)
      currentPage.value = 1
      loadTemplates()
      loadStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重新导入失败')
    }
  } finally {
    importing.value = false
  }
}

// 查看详情
const handleViewDetail = (row) => {
  currentTemplate.value = row
  detailVisible.value = true
}

// 复制提示词
const copyPrompt = () => {
  if (currentTemplate.value && currentTemplate.value.prompt) {
    navigator.clipboard.writeText(currentTemplate.value.prompt)
    ElMessage.success('提示词已复制到剪贴板')
  }
}

// 分页处理
const handleSizeChange = () => {
  currentPage.value = 1
  loadTemplates()
}

const handlePageChange = () => {
  loadTemplates()
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 初始化
onMounted(() => {
  loadTemplates()
  loadStats()
})
</script>

<style scoped>
.ai-template-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.button-group {
  text-align: right;
}

.stats-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.no-preview {
  color: #909399;
  font-size: 12px;
}

.detail-content {
  max-height: 70vh;
  overflow-y: auto;
}

.preview-section {
  margin-top: 20px;
}

.preview-section h4 {
  margin-bottom: 10px;
  color: #303133;
}

.prompt-section {
  margin-top: 20px;
  position: relative;
}

.prompt-section h4 {
  margin-bottom: 10px;
  color: #303133;
}

.prompt-textarea {
  font-family: 'Courier New', monospace;
  font-size: 13px;
}

.copy-btn {
  margin-top: 10px;
}
</style>
