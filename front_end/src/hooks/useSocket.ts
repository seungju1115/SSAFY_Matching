import { useState, useEffect } from 'react';
import { type IMessage } from '@stomp/stompjs';
import { webSocketService } from '@/services/socket';
import type { ChatMessageRequest } from '@/api/chat';

interface UseSocketReturn {
  isConnected: boolean;
  subscribe: (topic: string, callback: (message: IMessage) => void) => () => void;
  subscribeToRoom: (roomId: string | number, callback: (message: IMessage) => void) => () => void;
  subscribeToPrivate: (roomId: string | number, callback: (message: IMessage) => void) => () => void;
  publish: (destination: string, body: object) => void;
  sendChatMessage: (message: ChatMessageRequest) => void;
  sendPrivateMessage: (message: ChatMessageRequest) => void;
  disconnect: () => void;
}

export const useSocket = (): UseSocketReturn => { // Explicitly define return type
  const [isConnected, setIsConnected] = useState(webSocketService.isConnected());

  useEffect(() => {
    const listener = (connected: boolean) => {
      setIsConnected(connected);
    };

    webSocketService.addConnectionListener(listener);

    if (!webSocketService.isConnected()) {
      webSocketService.connect();
    }

    return () => {
      webSocketService.removeConnectionListener(listener);
    };
  }, []);

  const subscribe = (topic: string, callback: (message: IMessage) => void): () => void => {
  return webSocketService.subscribe(topic, callback) ?? (() => {});
};


  // 그룹 채팅방 구독용 함수
  const subscribeToRoom = (roomId: string | number, callback: (message: IMessage) => void) => {
    return subscribe(`/topic/chat/room/${roomId}`, callback);
  };

  // 1:1 채팅방 구독용 함수
  const subscribeToPrivate = (roomId: string | number, callback: (message: IMessage) => void) => {
    return subscribe(`/queue/chat/room/${roomId}`, callback);
  };

  // 범용 publish 함수 (유연성을 위해 유지)
  const publish = (destination: string, body: object) => {
    webSocketService.publish(destination, body);
  };

  // 그룹 채팅 메시지 전송용 함수
  const sendChatMessage = (message: ChatMessageRequest) => {
    publish('/app/chat.sendMessage', message);
  };

  // 1:1 개인 메시지 전송용 함수
  const sendPrivateMessage = (message: ChatMessageRequest) => {
    publish('/app/chat.private', message);
  };


  const disconnect = () => {
    webSocketService.disconnect();
  };

  return { 
    isConnected, 
    subscribe, 
    subscribeToRoom, 
    subscribeToPrivate, 
    publish, 
    sendChatMessage, 
    sendPrivateMessage, 
    disconnect 
  };
}; 