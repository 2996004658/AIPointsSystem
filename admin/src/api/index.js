import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000
})

// Request interceptor
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// Response interceptor
api.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                localStorage.removeItem('token')
                router.push('/login')
                ElMessage.error('Session expired, please login again')
            } else {
                ElMessage.error(error.response.data.message || 'Error')
            }
        } else {
            ElMessage.error('Network Error')
        }
        return Promise.reject(error)
    }
)

export default api
