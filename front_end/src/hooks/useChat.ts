import { useCallback, useEffect, useRef, useState } from 'react'
import type { Message } from '@/types/chat'
import { chatTransport } from '@/lib/chat'

interface UseChatOptions {
  chatId: string
  senderId: string
  token?: string
}

export function useChat({ chatId, senderId, token }: UseChatOptions) {
  const [messages, setMessages] = useState<Message[]>([])
  const unsubscribeRef = useRef<() => void>()
  const startedRef = useRef(false) // StrictMode 중복 연결 방지

  const connect = useCallback(async () => {
    if (startedRef.current || chatTransport.isConnected()) return
    await chatTransport.connect({ chatId, token })
    unsubscribeRef.current = chatTransport.subscribe((m) => {
      // 같은 id가 이미 있으면 추가하지 않음
      setMessages((prev) => (prev.some(x => x.id === m.id) ? prev : [...prev, m]))
    })
    startedRef.current = true
  }, [chatId, token])

  const disconnect = useCallback(async () => {
    if (!startedRef.current && !chatTransport.isConnected()) return
    unsubscribeRef.current?.()
    await chatTransport.disconnect()
    startedRef.current = false
  }, [])

  const send = useCallback(async (content: string) => {
    await chatTransport.send({ chatId, senderId, content })
  }, [chatId, senderId])

  useEffect(() => {
    connect()
    return () => {
      disconnect()
    }
  }, [connect, disconnect])

  return {
    messages,
    send,
    connected: chatTransport.isConnected(),
  }
}
