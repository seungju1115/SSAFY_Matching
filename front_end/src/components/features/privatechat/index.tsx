import { useState, useEffect, useRef, useCallback } from 'react';
import type { IMessage } from '@stomp/stompjs';
import { useSocket } from '@/hooks/useSocket';
import useUserStore from '@/stores/userStore';
import { useUser } from '@/hooks/useUser';
import { chatAPI } from '@/api/chat';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Send, MessageCircle } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { ScrollArea } from '@/components/ui/scroll-area';
interface PrivateChatProps {
  otherUserId: number;
}

interface ChatMessage {
  id: number;
  senderId: number;
  sender: string;
  message: string;
  timestamp: string;
}

export default function PrivateChat({ otherUserId }: PrivateChatProps) {
  const { user } = useUserStore();
  const myId = user?.id;

  const { getUserProfile } = useUser();
  const { isConnected, subscribeToPrivate, sendPrivateMessage } = useSocket();

  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([]);
  const [chatMessage, setChatMessage] = useState('');
  const [otherUserName, setOtherUserName] = useState('알 수 없음');
  const [roomId, setRoomId] = useState<number>();

  const subscribedRoomId = useRef<number | null>(null);
  const otherUserNameRef = useRef(otherUserName);

  // 렌더링 시 로그
  console.log('[PrivateChat] Render', {
    otherUserId,
    roomId,
    chatMessagesLength: chatMessages.length,
    chatMessage,
    otherUserName,
  });

  useEffect(() => {
    otherUserNameRef.current = otherUserName;
  }, [otherUserName]);

  const fetchOtherUserName = useCallback(async () => {
  if (!otherUserNameRef.current) { // 이미 가져온 이름이 없을 때만
    const profile = await getUserProfile(otherUserId);
    if (profile?.userName) {
      setOtherUserName(profile.userName);
      otherUserNameRef.current = profile.userName;
    }
  }
}, [otherUserId, getUserProfile]);

useEffect(() => {
  fetchOtherUserName();
}, [fetchOtherUserName]);

  // 채팅방 생성/조회
  useEffect(() => {
    console.log('[PrivateChat] useEffect: initPrivateChat', { myId, otherUserId, isConnected });
    if (!myId || !isConnected) return;

    const initPrivateChat = async () => {
      try {
        const response = await chatAPI.createPrivateRoom({
          roomType: 'PRIVATE',
          user1Id: myId,
          user2Id: otherUserId,
        });
        const newRoomId = response.data.data?.roomId;
        if (newRoomId) {
          setRoomId(newRoomId);
          console.log('[PrivateChat] RoomId set', newRoomId);
        }
      } catch (err) {
        console.error('[PrivateChat] 1:1 채팅방 생성 실패', err);
      }
    };

    initPrivateChat();
  }, [myId, otherUserId, isConnected]);

  // 기존 메시지 불러오기
  useEffect(() => {
    console.log('[PrivateChat] useEffect: fetchMessages', roomId);
    if (!roomId) return;

    const fetchMessages = async () => {
      try {
        const response = await chatAPI.getMessages(roomId);
        const messages: ChatMessage[] = response.data.data.map((msg: any) => ({
          id: msg.id,
          senderId: msg.senderId,
          sender: msg.senderId === myId ? '나' : otherUserNameRef.current,
          message: msg.message,
          timestamp: msg.createdAt,
        }));
        setChatMessages(messages);
        console.log('[PrivateChat] Messages set', messages.length);
      } catch (err) {
        console.error('[PrivateChat] 1:1 메시지 불러오기 실패', err);
      }
    };

    fetchMessages();
  }, [roomId, myId]);

  // 메시지 구독 (중복 방지)
  useEffect(() => {
    console.log('[PrivateChat] useEffect: subscribeToPrivate', roomId);
    if (!roomId || !isConnected || subscribedRoomId.current === roomId) return;
    if (subscribedRoomId.current === roomId) return;
    subscribedRoomId.current = roomId;
    console.log('[PrivateChat] Subscribing to room', roomId);

    const unsub = subscribeToPrivate(roomId, (message: IMessage) => {
      console.log('[PrivateChat] Received message', message.body);
      try {
        const parsed = JSON.parse(message.body);
        setChatMessages((prev) => [
          ...prev,
          {
            id: parsed.id ?? Date.now(),
            senderId: parsed.senderId,
            sender: parsed.senderId === myId ? '나' : otherUserNameRef.current,
            message: parsed.message,
            timestamp: parsed.createdAt,
          },
        ]);
      } catch (err) {
        console.error('[PrivateChat] 메시지 파싱 실패', err);
      }
    });

    return () => {
      unsub?.();
      console.log('[PrivateChat] Unsubscribed from room', roomId);
    };
  }, [roomId, isConnected, subscribeToPrivate, myId]);

  const handleSendMessage = () => {
    console.log('[PrivateChat] handleSendMessage', { chatMessage, myId, roomId });
    if (!chatMessage.trim() || !myId || !roomId) return;

    sendPrivateMessage({
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
          1:1 채팅
        </CardTitle>
      </CardHeader>

      <CardContent className="flex-1 flex flex-col p-0">
        <ScrollArea className="flex-1 px-6">
          <div className="space-y-4 py-4">
            {chatMessages.map((msg) => (
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
            <Button
              onClick={handleSendMessage}
              className="bg-blue-600 hover:bg-blue-700 text-white"
            >
              <Send className="w-4 h-4" />
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

