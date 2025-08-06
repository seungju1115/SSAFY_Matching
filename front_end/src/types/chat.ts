// Chat 관련 타입 정의
export interface Message {
  id: string
  chatId: string
  senderId: string
  content: string
  timestamp: Date
}

export interface Chat {
  id: string
  participants: string[]
  messages: Message[]
  createdAt: Date
} 