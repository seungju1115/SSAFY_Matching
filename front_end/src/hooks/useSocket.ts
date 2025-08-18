import { useState, useEffect, useCallback } from 'react';
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
      console.log('useSocket: isConnected changed to', connected);
      setIsConnected(connected);
    };

    webSocketService.addConnectionListener(listener);

    if (!webSocketService.isConnected()) {
      console.log('useSocket: Connecting to WebSocket...');
      webSocketService.connect();
    }

    return () => {
      webSocketService.removeConnectionListener(listener);
    };
  }, []);

  const subscribe = useCallback((topic: string, callback: (message: IMessage) => void): () => void => {
    return webSocketService.subscribe(topic, callback) ?? (() => {});
  }, []);


  // 그룹 채팅방 구독용 함수
  const subscribeToRoom = useCallback((roomId: string | number, callback: (message: IMessage) => void) => {
    return subscribe(`/topic/chat/room/${roomId}`, callback);
  }, [subscribe]);

  // 1:1 채팅방 구독용 함수
  const subscribeToPrivate = useCallback((roomId: string | number, callback: (message: IMessage) => void) => {
    return subscribe(`/queue/chat/room/${roomId}`, callback);
  }, [subscribe]);

  // 범용 publish 함수 (유연성을 위해 유지)
  const publish = useCallback((destination: string, body: object) => {
    webSocketService.publish(destination, body);
  }, []);

  // 그룹 채팅 메시지 전송용 함수
  const sendChatMessage = useCallback((message: ChatMessageRequest) => {
    publish('/app/chat.sendMessage', message);
  }, [publish]);

  // 1:1 개인 메시지 전송용 함수
  const sendPrivateMessage = useCallback((message: ChatMessageRequest) => {
    publish('/app/chat.private', message);
  }, [publish]);


  const disconnect = useCallback(() => {
    webSocketService.disconnect();
  }, []);

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