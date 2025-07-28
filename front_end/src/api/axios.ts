// Axios 인스턴스 및 설정
import axios from 'axios'

const apiClient = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  timeout: 10000,
})

export default apiClient 