import { useState, useEffect } from 'react';
import type { IMessage } from '@stomp/stompjs';
import { useSocket } from '@/hooks/useSocket';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Send, MessageCircle } from 'lucide-react';
import { useTeamStore } from '@/stores/teamStore';
import useUserStore from '@/stores/userStore';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { ScrollArea } from '@/components/ui/scroll-area';
import { chatAPI } from '@/api/chat';

interface TeamChatProps {
  roomId: number;
  teamId: number;
}

interface ChatMessage {
  id: number;
  senderId: number;
  sender: string;
  message: string;
  timestamp: string;
}

export default function TeamChat({ roomId, teamId }: TeamChatProps) {
  const { user } = useUserStore();
  const myId = user?.id;

  const { getTeamDetailById } = useTeamStore();
  const teamInfo = getTeamDetailById(teamId);
  const teamMembers = teamInfo?.members || [];

  const { isConnected, subscribeToRoom, sendChatMessage } = useSocket();

  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([]);
  const [chatMessage, setChatMessage] = useState('');

  // 1. 서버에서 기존 메시지 불러오기
  useEffect(() => {
    const fetchMessages = async () => {
      if (!roomId) return;
      try {
        const response = await chatAPI.getMessages(roomId); // API 호출
        const messages: ChatMessage[] = response.data.data.map((msg: any) => ({
          id: msg.id,
          senderId: msg.senderId,
          sender: teamMembers.find((m) => m.id === msg.senderId)?.userName || '알 수 없음',
          message: msg.message,
          timestamp: msg.createdAt,
        }));
        setChatMessages(messages);
      } catch (err) {
        console.error('팀 메시지 불러오기 실패', err);
      }
    };
    fetchMessages();
  }, [roomId, teamMembers]);

  // 2. 실시간 메시지 구독
  useEffect(() => {
    if (!roomId || !isConnected) return;

    const unsub = subscribeToRoom(roomId, (message: IMessage) => {
      try {
        const parsed = JSON.parse(message.body);
        const senderName =
          teamMembers.find((m) => m.id === parsed.senderId)?.userName || '알 수 없음';

        const newMessage: ChatMessage = {
          id: parsed.id ?? Date.now(),
          senderId: parsed.senderId,
          sender: senderName,
          message: parsed.message,
          timestamp: parsed.createdAt,
        };

        setChatMessages((prev: ChatMessage[]) => [...prev, newMessage]);
      } catch (err) {
        console.error('메시지 파싱 실패', err);
      }
    });

    return () => unsub?.();
  }, [roomId, isConnected, subscribeToRoom, teamMembers]);

  const handleSendMessage = () => {
    if (!chatMessage.trim() || !myId) return;

    sendChatMessage({
      roomId,
      senderId: myId,
      message: chatMessage,
      type: 'CHAT',
    });

    setChatMessage('');
  };

  return (
    <Card className="bg-white border border-gray-200 shadow-sm h-[600px] lg:h-[700px] flex flex-col">
      <CardHeader className="flex-shrink-0">
        <CardTitle className="flex items-center gap-2 text-lg font-medium text-gray-900">
          <MessageCircle className="w-5 h-5" />
          팀 채팅
        </CardTitle>
      </CardHeader>

      <CardContent className="flex-1 flex flex-col p-0">
        <ScrollArea className="flex-1 px-6">
          <div className="space-y-4 py-4">
            {chatMessages.map((msg: ChatMessage) => (
              <div key={msg.id}>
                <div className="flex items-center gap-2 mb-1">
                  <span className="text-sm font-medium text-gray-900">{msg.sender}</span>
                  <span className="text-xs text-gray-500">{msg.timestamp}</span>
                </div>
                <div className="bg-gray-50 p-3 rounded-lg max-w-md">
                  <p className="text-sm text-gray-700">{msg.message}</p>
                </div>
              </div>
            ))}
          </div>
        </ScrollArea>

        <div className="flex-shrink-0 p-4 border-t border-gray-200">
          <div className="flex gap-2">
            <Input
              value={chatMessage}
              onChange={(e) => setChatMessage(e.target.value)}
              placeholder="메시지를 입력하세요..."
              className="flex-1"
              onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
            />
            <Button onClick={handleSendMessage} className="bg-blue-600 hover:bg-blue-700 text-white">
              <Send className="w-4 h-4" />
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
