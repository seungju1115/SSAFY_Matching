// 채팅 관련 API
import apiClient from './axios'

// 메시지 타입 enum
export type MessageType = 'CHAT' | 'ENTER' | 'LEAVE'

// 공통 메시지 요청 바디 타입
export interface ChatMessageRequest {
  type: MessageType
  roomId: number
  senderId: number
  message: string
}
// 채팅방 타입
export type RoomType = 'TEAM' | 'PRIVATE'

// 채팅방 생성 요청 DTO
export interface ChatRoomRequest {
  roomType: RoomType
  roomId?: number
  user1Id?: number
  user2Id?: number
  teamId?: number
  userId?: number
}

export const chatAPI = {
  // PRIVATE 채팅방 생성
  createPrivateRoom: (payload: Omit<ChatRoomRequest, 'teamId' | 'userId'>) =>
    apiClient.post('/chatroom/private', payload),

  // 기존 메시지 조회 예시
  getMessages: (chatId: number) =>
    apiClient.get(`/chatroom/${chatId}/messages`),
}