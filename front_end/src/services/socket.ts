import { Client, type IMessage, type Frame } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// const socket = io(process.env.REACT_APP_SOCKET_URL || 'http://localhost:8080')

// 환경에 따라 적절한 WebSocket URL을 반환하는 함수
const getWebSocketURL = () => {
  if (import.meta.env.PROD) {
    // 배포 환경일 경우, 하드코딩된 URL 사용
    return 'https://i13a307.p.ssafy.io/ws-chat';
  }
  // 개발 환경일 경우, .env 파일의 VITE_WEBSOCKET_URL 사용
  // 만약 .env 파일에 값이 없을 경우, 기본 로컬호스트 주소를 사용
  return 'http://localhost:8080/ws-chat';
};

class WebSocketService {
  private stompClient: Client | null = null;
  private subscriptions: Map<string, Subscription> = new Map();
  private connectionListeners: Set<(isConnected: boolean) => void> = new Set();

  // 싱글톤 인스턴스
  private static instance: WebSocketService;
  public static getInstance(): WebSocketService {
    if (!WebSocketService.instance) {
      WebSocketService.instance = new WebSocketService();
    }
    return WebSocketService.instance;
  }

  public connect(onConnected?: () => void): void {
    if (this.stompClient && this.stompClient.active) {
      console.log('STOMP is already connected.');
      onConnected?.();
      return;
    }

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(getWebSocketURL()),
      debug: (str: string) => {
        console.log(new Date(), str);
      },
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('✅ STOMP Connected!');
        onConnected?.();
        this.connectionListeners.forEach((listener) => listener(true));
        // 재연결 시 기존 구독들을 다시 설정
        this.subscriptions.forEach((sub) => this.subscribe(sub.topic, sub.callback));
      },
      onDisconnect: () => {
        console.log('STOMP Disconnected.');
        this.connectionListeners.forEach((listener) => listener(false));
      },
      onStompError: (frame: Frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    this.stompClient.activate();
  }

  public disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  public subscribe(topic: string, callback: (message: IMessage) => void) {
    if (!this.stompClient || !this.stompClient.active) {
      console.error('STOMP client is not connected. Cannot subscribe.');
      this.subscriptions.set(topic, { topic, callback });
      return;
    }

    console.log(`Subscribing to ${topic}`);
    const subscription = this.stompClient.subscribe(topic, (message: IMessage) => {
      callback(message);
    });
    
    this.subscriptions.set(topic, { topic, callback });
    
    return () => {
      console.log(`Unsubscribing from ${topic}`);
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
  }

  public publish(destination: string, body: object): void {
    if (!this.stompClient || !this.stompClient.active) {
      console.error('STOMP client is not connected. Cannot publish.');
      return;
    }
    this.stompClient.publish({
      destination,
      body: JSON.stringify(body),
    });
  }

  public isConnected(): boolean {
    return this.stompClient?.active ?? false;
  }

  public addConnectionListener(listener: (isConnected: boolean) => void) {
    this.connectionListeners.add(listener);
  }

  public removeConnectionListener(listener: (isConnected: boolean) => void) {
    this.connectionListeners.delete(listener);
  }
}

// 싱글톤 인스턴스를 export 합니다.
export const webSocketService = WebSocketService.getInstance();