// 인증 관련 API
import apiClient from './axios'
import { UserDetailSettings } from '@/types/user'

export const authAPI = {
  login: (credentials: any) => apiClient.post('/auth/login', credentials),
  logout: () => apiClient.post('/auth/logout'),
  register: (userData: any) => apiClient.post('/auth/register', userData),
  updateUserDetails: (userDetails: UserDetailSettings) => 
    apiClient.put('/auth/user/details', userDetails),
  getUserProfile: () => apiClient.get('/auth/user/profile'),
} 