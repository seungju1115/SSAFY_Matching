import type { Message } from '@/types/chat'

export interface ChatTransport {
  connect(opts: { chatId: string; token?: string }): Promise<void>
  disconnect(): Promise<void>
  subscribe(onMessage: (msg: Message) => void): () => void // unsubscribe 반환
  send(payload: { chatId: string; senderId: string; content: string }): Promise<void>
  isConnected(): boolean
}
