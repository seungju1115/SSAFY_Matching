import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useChat } from '@/hooks/useChat'

export default function TeamChatPage() {
  const { teamId } = useParams<{ teamId: string }>()
  const senderId = localStorage.getItem('userId') ?? 'me'
  const { messages, send, connected } = useChat({
    chatId: teamId ?? '1',
    senderId,
  })

  const [text, setText] = useState('')
  const bottomRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  if (!teamId) return <div className="p-4">팀 ID가 필요합니다.</div>

  const onSend = async () => {
    const trimmed = text.trim()
    if (!trimmed) return
    await send(trimmed)
    setText('')
  }

  return (
    <div className="max-w-xl mx-auto p-4 flex flex-col gap-3">
      <div className="font-semibold">
        채팅 방: {teamId} {connected ? '🟢' : '🔴'}
      </div>

      <div className="h-96 overflow-y-auto bg-slate-50 rounded-lg p-3 border border-slate-200">
        {messages.map(m => (
          <div key={m.id} className="mb-2">
            <div className="text-xs text-slate-500">
              {m.senderId} · {new Date(m.timestamp).toLocaleTimeString()}
            </div>
            <div
              className={[
                'inline-block px-3 py-2 rounded-lg whitespace-pre-wrap break-words max-w-[280px]',
                m.senderId === senderId ? 'bg-blue-100 ml-auto' : 'bg-slate-200',
              ].join(' ')}
              style={{ display: 'inline-block' }}
            >
              {m.content}
            </div>
          </div>
        ))}
        <div ref={bottomRef} />
      </div>

      <div className="flex gap-2">
        <input
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && onSend()}
          placeholder="메시지를 입력하세요"
          className="flex-1 border border-slate-300 rounded-lg px-3 py-2"
        />
        <button
          onClick={onSend}
          className="border border-blue-400 bg-blue-600 text-white rounded-lg px-4 py-2"
        >
          보내기
        </button>
      </div>
    </div>
  )
}
