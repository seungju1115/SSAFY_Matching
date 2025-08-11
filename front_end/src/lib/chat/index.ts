import type { ChatTransport } from './transport'
import { MockTransport } from './mockTransport'

// 나중에 웹소켓 준비되면 여기만 교체하면 됨
// import { StompTransport } from './stompTransport'
// export const chatTransport: ChatTransport = new StompTransport()

export const chatTransport: ChatTransport = new MockTransport()
export type { ChatTransport } from './transport'
