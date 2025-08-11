import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Send } from 'lucide-react'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { useChat } from '@/hooks/useChat'

export default function TeamChat() {
  // URL /team/:teamId 에서 팀 ID 사용
  const { teamId } = useParams<{ teamId: string }>()
  const senderId = localStorage.getItem('userId') ?? 'me'

  const { messages, send } = useChat({
    chatId: teamId ?? '1',
    senderId,
  })

  const [chatMessage, setChatMessage] = useState('')
  const bottomRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleSendMessage = async () => {
    const text = chatMessage.trim()
    if (!text) return
    await send(text)
    setChatMessage('')
  }

  return (
    <>
      {/* ✅ 원래 구조: ScrollArea + 메시지 리스트 */}
      <ScrollArea className="flex-1 px-6">
        <div className="space-y-4 py-4">
          {messages.map((m) => (
            <div key={m.id}>
              <div className="flex items-center gap-2 mb-1">
                <span className="text-sm font-medium text-gray-900">{m.senderId}</span>
                <span className="text-xs text-gray-500">
                  {new Date(m.timestamp).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}
                </span>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg max-w-md">
                <p className="text-sm text-gray-700">{m.content}</p>
              </div>
            </div>
          ))}
          <div ref={bottomRef} />
        </div>
      </ScrollArea>

      {/* ✅ 원래 구조: 하단 입력줄 */}
      <div className="flex-shrink-0 p-4 border-t border-gray-200">
        <div className="flex gap-2">
          <Input
            value={chatMessage}
            onChange={(e) => setChatMessage(e.target.value)}
            placeholder="메시지를 입력하세요..."
            className="flex-1"
            onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
          />
          <Button 
            onClick={handleSendMessage}
            className="bg-blue-600 hover:bg-blue-700 text-white"
          >
            <Send className="w-4 h-4" />
          </Button>
        </div>
      </div>
    </>
  )
}
