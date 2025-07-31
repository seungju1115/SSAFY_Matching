// 채팅 관련 API
import apiClient from './axios'

export const chatAPI = {
  getMessages: (chatId: string) => apiClient.get(`/chat/${chatId}/messages`),
  sendMessage: (chatId: string, message: any) => apiClient.post(`/chat/${chatId}/messages`, message),
} 