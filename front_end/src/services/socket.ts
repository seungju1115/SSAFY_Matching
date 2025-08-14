import { Client, type IMessage, type Frame } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// 구독 정보를 관리하기 위한 타입
interface Subscription {
  topic: string;
  callback: (message: IMessage) => void;
}

// 환경에 따라 적절한 WebSocket URL을 반환하는 함수
const getWebSocketURL = () => {
  if (!import.meta.env.PROD) {
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
        console.log('✅ STOMP Connected! (socket.ts)');
        onConnected?.();
        this.connectionListeners.forEach((listener) => listener(true));
        // 재연결 시 기존 구독들을 다시 설정
        this.subscriptions.forEach((sub) => this.subscribe(sub.topic, sub.callback));
      },
      onDisconnect: () => {
        console.log('STOMP Disconnected. (socket.ts)');
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
  // 이미 구독한 토픽이면 새로 구독하지 않음
  if (this.subscriptions.has(topic)) {
    console.log(`⚠️ Already subscribed to ${topic}, updating callback only.`);
    this.subscriptions.set(topic, { topic, callback });
    return () => this.unsubscribe(topic);
  }

  if (!this.stompClient || !this.stompClient.active) {
    console.error('STOMP client is not connected. Cannot subscribe now.');
    // 연결 안 된 경우: 콜백 저장 후, 연결 시 재구독
    this.subscriptions.set(topic, { topic, callback });
    return () => this.unsubscribe(topic);
  }

  console.log(`✅ Subscribing to ${topic}`);
  const stompSub = this.stompClient.subscribe(topic, (message: IMessage) => {
    // 항상 최신 콜백을 사용
    const sub = this.subscriptions.get(topic);
    if (sub) {
      sub.callback(message);
    }
  });

  this.subscriptions.set(topic, { topic, callback });

  return () => {
    console.log(`🛑 Unsubscribing from ${topic}`);
    stompSub.unsubscribe();
    this.subscriptions.delete(topic);
  };
}

private unsubscribe(topic: string) {
  // 연결 상태일 때만 실제 unsubscribe
  if (this.stompClient && this.stompClient.active && this.subscriptions.has(topic)) {
    console.log(`🛑 Unsubscribing from ${topic}`);
    // STOMP의 unsubscribe는 subscribe할 때 받은 객체로 해야 함
    // 여기선 return 함수 안에서 처리하므로 별도 관리 가능
  }
  this.subscriptions.delete(topic);
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