import { v4 as uuid } from 'uuid'
import type { Message } from '@/types/chat'
import type { ChatTransport } from './transport'

export class MockTransport implements ChatTransport {
  private connected = false
  private listeners: Array<(m: Message) => void> = []
  private chatId = ''

  async connect(opts: { chatId: string }) {
    this.chatId = opts.chatId
    this.connected = true
    this.listeners = [] // 중복 구독 초기화
  }

  async disconnect() {
    this.connected = false
    this.listeners = []
  }

  subscribe(onMessage: (msg: Message) => void) {
    this.listeners.push(onMessage)

    setTimeout(() => {
      if (!this.connected) return
      onMessage({
        id: uuid(),
        chatId: this.chatId,
        senderId: 'mock-bot',
        content: '👋 Mock 연결 완료! 웹소켓 준비되면 드라이버만 교체하면 돼요.',
        timestamp: new Date(),
      })
    }, 200)

    return () => {
      this.listeners = this.listeners.filter(l => l !== onMessage)
    }
  }

  async send(payload: { chatId: string; senderId: string; content: string }) {
    if (!this.connected) throw new Error('Not connected')

    const userMsg: Message = {
      id: uuid(),
      chatId: payload.chatId,
      senderId: payload.senderId,
      content: payload.content,
      timestamp: new Date(),
    }
    this.emit(userMsg)

    setTimeout(() => {
      const botMsg: Message = {
        id: uuid(),
        chatId: payload.chatId,
        senderId: 'mock-bot',
        content: `받았어요: "${payload.content}" (웹소켓 준비되면 실시간으로 바뀝니다)`,
        timestamp: new Date(),
      }
      this.emit(botMsg)
    }, 350)
  }

  isConnected() {
    return this.connected
  }

  private emit(msg: Message) {
    this.listeners.forEach(l => l(msg))
  }
}
