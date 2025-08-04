// 매칭 관련 API
import apiClient from './axios'

export const matchingAPI = {
  getMatches: () => apiClient.get('/matching'),
  createMatch: (matchData: any) => apiClient.post('/matching', matchData),
  deleteMatch: (id: string) => apiClient.delete(`/matching/${id}`),
} 