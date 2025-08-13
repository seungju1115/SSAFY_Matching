import { Client, type IMessage, type Frame } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// 구독 정보를 관리하기 위한 타입
interface Subscription {
  topic: string;
  callback: (message: IMessage) => void;
}

// 환경에 따라 적절한 WebSocket URL을 반환하는 함수
const getWebSocketURL = () => {
  if (import.meta.env.PROD) {
    // 배포(Production) 환경: 외부 접속용 URL을 사용합니다.
    return 'https://i13a307.p.ssafy.io/ws-chat';
  }
  // 개발(Development) 환경: Docker 컨테이너 간 통신을 위해 서비스 이름을 사용합니다.
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