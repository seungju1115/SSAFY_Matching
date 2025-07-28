// 인증 관련 API
import apiClient from './axios'

export const authAPI = {
  login: (credentials: any) => apiClient.post('/auth/login', credentials),
  logout: () => apiClient.post('/auth/logout'),
  register: (userData: any) => apiClient.post('/auth/register', userData),
} 