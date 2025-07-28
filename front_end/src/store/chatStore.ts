// Chat 상태 관리
import { create } from 'zustand'

interface ChatState {
  messages: []
  setMessages: (messages: any[]) => void
}

export const useChatStore = create<ChatState>((set) => ({
  messages: [],
  setMessages: (messages) => set({ messages }),
})) 