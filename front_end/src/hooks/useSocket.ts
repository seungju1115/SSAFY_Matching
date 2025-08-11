import { useState, useEffect } from 'react';
import { type IMessage } from '@stomp/stompjs';
import { webSocketService } from '@/services/socket';

export const useSocket = () => {
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

  const subscribe = (topic: string, callback: (message: IMessage) => void) => {
    return webSocketService.subscribe(topic, callback);
  };

  const publish = (destination: string, body: object) => {
    webSocketService.publish(destination, body);
  };

  const disconnect = () => {
    webSocketService.disconnect();
  };

  return { isConnected, subscribe, publish, disconnect };
}; 