package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessage;
import com.example.demo.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ChatControllerTest {

    private ChatController chatController;               // 테스트 대상 컨트롤러
    private ChatService chatService;
    private SimpMessagingTemplate messagingTemplate;     // 메시지 전송 담당 객체(모킹)

    @BeforeEach
    void setUp() {
        // SimpMessagingTemplate 객체를 Mockito를 이용해 모킹(mock) 처리
        messagingTemplate = mock(SimpMessagingTemplate.class);
        chatService = mock(ChatService.class);
        // 모킹된 messagingTemplate을 주입하여 컨트롤러 인스턴스 생성
        chatController = new ChatController(messagingTemplate,chatService);
    }

    @Test
    void testSendRoomMessage() {
        // 1. 테스트용 메시지 객체 생성 및 초기화
        ChatMessage message = new ChatMessage();
        message.setRoomId("room1");          // 채팅방 ID 설정
        message.setSender("userA");          // 발신자 정보 설정
        message.setMessage("Hello Room");    // 전송할 메시지 내용 설정
        message.setType(true);               // 메시지 타입 설정 (true: 다중 채팅)

        // 2. 테스트 대상 메서드 호출 (실제 메시지 전송 로직 실행)
        chatController.sendRoomMessage(message);

        // 3. messagingTemplate.convertAndSend()가 정확히 1번 호출됐는지 검증
        // ArgumentCaptor를 이용해 실제 전달된 인자값을 캡처해서 확인
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        // 호출 검증 및 인자 캡처
        verify(messagingTemplate, times(1)).convertAndSend(destinationCaptor.capture(), payloadCaptor.capture());

        // 캡처된 목적지(destination)와 전송된 메시지(payload) 추출
        String destination = destinationCaptor.getValue();
        ChatMessage sentMessage = (ChatMessage) payloadCaptor.getValue();

        // 4. 목적지(destination)가 예상한 채팅방 토픽 경로인지 확인
        assertThat(destination).isEqualTo("/topic/chat/room/" + message.getRoomId());

        // 5. 전송된 메시지 내용이 올바른지 필드별로 검증
        assertThat(sentMessage.getMessage()).isEqualTo("Hello Room");
        assertThat(sentMessage.getSender()).isEqualTo("userA");
        assertThat(sentMessage.isType()).isTrue();
    }

    @Test
    void testSendPrivateMessage() {
        // 1. 테스트용 개인 메시지 객체 생성 및 초기화
        ChatMessage message = new ChatMessage();
        message.setRoomId("privateRoom");        // 개인 채팅방 ID 설정
        message.setSender("userB");               // 발신자 정보 설정
        message.setMessage("Private Hello");     // 메시지 내용 설정
        message.setType(false);                   // 메시지 타입 설정 (false: 1:1 채팅)

        // 2. 테스트 대상 메서드 호출 (개인 메시지 전송 로직 실행)
        chatController.sendPrivateMessage(message);

        // 3. messagingTemplate.convertAndSend()가 1번 호출됐는지 검증
        // ArgumentCaptor를 이용해 실제 전달된 인자값을 캡처
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        verify(messagingTemplate, times(1)).convertAndSend(destinationCaptor.capture(), payloadCaptor.capture());

        // 캡처된 목적지와 메시지 추출
        String destination = destinationCaptor.getValue();
        ChatMessage sentMessage = (ChatMessage) payloadCaptor.getValue();

        // 4. 목적지(destination)가 예상한 개인 채팅 큐 경로인지 확인
        assertThat(destination).isEqualTo("/queue/chat/room/" + message.getRoomId());

        // 5. 메시지 내용과 발신자, 타입 필드 검증
        assertThat(sentMessage.getMessage()).isEqualTo("Private Hello");
        assertThat(sentMessage.getSender()).isEqualTo("userB");
        assertThat(sentMessage.isType()).isFalse();
    }
}
