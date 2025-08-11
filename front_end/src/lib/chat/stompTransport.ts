// front_end/src/lib/chat/stompTransport.ts
import { Client, IMessage, StompSubscription } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import type { ChatTransport } from './transport'
import type { Message } from '@/types/chat'

// 환경: .env에 VITE_API_BASE=http://localhost:8080 처럼 넣어두면 좋음
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

// 백엔드 설정 요약:
// - endpoint: /ws-chat
// - appPrefix: /app
// - MessageMapping: /chat.sendMessage (공개), /chat.private (1:1)
// - publish destinations (서버->클라):
//   - /topic/chat/room/{roomId}   (공개 채팅방)
//   - /queue/chat/room/{roomId}   (1:1 채팅)

export class StompTransport implements ChatTransport {
  private client: Client | null = null
  private connected = false
  private subs: StompSubscription[] = []
  private chatId = ''

  async connect(opts: { chatId: string; token?: string }) {
    if (this.connected) return
    this.chatId = opts.chatId

    this.client = new Client({
      // SockJS 팩토리 사용 (브라우저 호환성)
      webSocketFactory: () => new SockJS(`${API_BASE}/ws-chat`),
      reconnectDelay: 3000, // 자동 재연결 (ms)
      debug: () => {},      // 필요시 console.log로 교체

      connectHeaders: opts.token
        ? { Authorization: `Bearer ${opts.token}` }
        : {},
    })

    return new Promise<void>((resolve, reject) => {
      if (!this.client) return reject(new Error('STOMP client not created'))

      this.client.onConnect = () => {
        this.connected = true
        resolve()
      }
      this.client.onStompError = (frame) => {
        console.error('STOMP error', frame)
      }
      this.client.onWebSocketError = (ev) => {
        console.error('WebSocket error', ev)
      }
      this.client.onDisconnect = () => {
        this.connected = false
        this.subs = []
      }

      try {
        this.client.activate()
      } catch (e) {
        reject(e)
      }
    })
  }

  async disconnect() {
    if (!this.client) return
    // 구독 해제
    this.subs.forEach(s => s.unsubscribe())
    this.subs = []
    // 연결 종료
    await this.client.deactivate()
    this.client = null
    this.connected = false
  }

  subscribe(onMessage: (msg: Message) => void) {
    if (!this.client || !this.connected) {
      throw new Error('Not connected')
    }

    // 공개 채팅방 토픽
    const topicDest = `/topic/chat/room/${this.chatId}`
    // 1:1 큐 (서버가 해당 경로로 보내는 경우 대비)
    const queueDest = `/queue/chat/room/${this.chatId}`

    const handler = (m: IMessage) => {
      try {
        const data = JSON.parse(m.body) as {
          id: number
          chatRoomId: number
          senderId: number
          message: string
          createdAt?: string
        }

        const mapped: Message = {
          id: String(data.id ?? cryptoRandomId()),
          chatId: String(data.chatRoomId ?? this.chatId),
          senderId: String(data.senderId ?? ''),
          content: data.message ?? '',
          timestamp: data.createdAt ? new Date(data.createdAt) : new Date(),
        }
        onMessage(mapped)
      } catch (e) {
        console.error('Invalid message payload', e, m.body)
      }
    }

    const s1 = this.client.subscribe(topicDest, handler)
    const s2 = this.client.subscribe(queueDest, handler)
    this.subs.push(s1, s2)

    // unsubscribe 반환
    return () => {
      try { s1.unsubscribe() } catch {}
      try { s2.unsubscribe() } catch {}
      this.subs = this.subs.filter(x => x !== s1 && x !== s2)
    }
  }

  async send(payload: { chatId: string; senderId: string; content: string }) {
    if (!this.client || !this.connected) throw new Error('Not connected')

    // 공개 채팅으로 전송 (필요 시 /app/chat.private 로 바꿔 1:1 전송)
    const sendDest = `/app/chat.sendMessage`

    const body = JSON.stringify({
      chatRoomId: numOrNull(payload.chatId),
      senderId: numOrNull(payload.senderId),
      message: payload.content,
    })

    this.client.publish({
      destination: sendDest,
      body,
    })
  }

  isConnected() {
    return this.connected
  }
}

// 유틸
function numOrNull(v: string): number | null {
  const n = Number(v)
  return Number.isFinite(n) ? n : null
}

// STOMP에서 id가 없을 때를 대비한 임시 id
function cryptoRandomId() {
  try {
    // 브라우저 Web Crypto
    const a = crypto.getRandomValues(new Uint32Array(4))
    return [...a].map(x => x.toString(16)).join('-')
  } catch {
    // 폴백
    return Math.random().toString(36).slice(2)
  }
}
