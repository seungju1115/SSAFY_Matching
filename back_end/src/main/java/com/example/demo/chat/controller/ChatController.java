// ChatController.java - 완전한 Swagger 설정
package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(
        name = "채팅 WebSocket",
        description = "실시간 채팅 메시지 처리 API (WebSocket STOMP)"
)
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @Operation(
            summary = "다중 채팅방 메시지 전송",
            description = """
            다중 채팅방에 메시지를 전송합니다.
            
            **WebSocket 연결 방법:**
            1. `/ws` 엔드포인트로 WebSocket 연결
            2. STOMP 클라이언트 사용
            3. `/app/chat.sendMessage`로 메시지 전송
            4. `/topic/chat/room/{roomId}` 구독하여 메시지 수신
            
            **JavaScript 예시:**
            ```javascript
            const socket = new SockJS('/ws');
            const stompClient = Stomp.over(socket);
            
            stompClient.connect({}, function() {
                // 채팅방 구독
                stompClient.subscribe('/topic/chat/room/123', function(message) {
                    const chatMessage = JSON.parse(message.body);
                    console.log(chatMessage);
                });

                // 메시지 전송
                stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
                    type: "CHAT",
                    roomId: 123,
                    senderId: 1,
                    message: "안녕하세요!"
                }));
            });
            ```
            """,
            tags = {"채팅 WebSocket"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "메시지 전송 성공 - 구독자들에게 브로드캐스트됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageResponse.class),
                            examples = @ExampleObject(
                                    name = "전송된 메시지",
                                    value = """
                    {
                        "id": 123,
                        "chatRoomId": 1,
                        "senderId": 1,
                        "message": "안녕하세요!",
                        "createdAt": "2024-01-15T10:30:00"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 메시지 형식"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 채팅방"
            )
    })
    @MessageMapping("/chat.sendMessage")
    public void sendRoomMessage(
            @Parameter(
                    description = "다중 채팅방 메시지 정보",
                    required = true,
                    schema = @Schema(implementation = ChatMessageRequest.class)
            )
            ChatMessageRequest socketMessage
    ) {
        log.info("다중 채팅 메시지 수신: roomId={}, senderId={}",
                socketMessage.getRoomId(), socketMessage.getSenderId());

        ChatMessageResponse savedMessage = chatMessageService.saveMessage(socketMessage);
        messagingTemplate.convertAndSend("/topic/chat/room/" + savedMessage.getChatRoomId(), savedMessage);

        log.info("메시지 브로드캐스트 완료: messageId={}", savedMessage.getId());
    }

    @Operation(
            summary = "1:1 채팅 메시지 전송",
            description = """
            1:1 채팅방에 개인 메시지를 전송합니다.
            
            **WebSocket 연결 방법:**
            1. `/ws` 엔드포인트로 WebSocket 연결
            2. STOMP 클라이언트 사용
            3. `/app/chat.private`로 메시지 전송
            4. `/queue/chat/room/{roomId}` 구독하여 메시지 수신
            
            **차이점:**
            - `/topic/` : 다중 사용자 브로드캐스트 (N:N)
            - `/queue/` : 개인 메시지 큐 (1:1)
            
            **JavaScript 예시:**
            ```javascript
            // 1:1 채팅방 구독
            stompClient.subscribe('/queue/chat/room/456', function(message) {
                const chatMessage = JSON.parse(message.body);
                console.log('개인 메시지:', chatMessage);
            });
            
            // 1:1 메시지 전송
            stompClient.send('/app/chat.private', {}, JSON.stringify({
                type: "CHAT",
                roomId: 456,
                senderId: 1,
                message: "개인 메시지입니다"
            }));
            ```
            """,
            tags = {"채팅 WebSocket"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "1:1 메시지 전송 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageResponse.class),
                            examples = @ExampleObject(
                                    name = "1:1 메시지 응답",
                                    value = """
                    {
                        "id": 124,
                        "chatRoomId": 456,
                        "senderId": 1,
                        "message": "개인 메시지입니다",
                        "createdAt": "2024-01-15T10:32:00"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 메시지 형식"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "1:1 채팅방 접근 권한 없음"
            )
    })
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(
            @Parameter(
                    description = "1:1 채팅 메시지 정보",
                    required = true,
                    schema = @Schema(implementation = ChatMessageRequest.class)
            )
            ChatMessageRequest socketMessage
    ) {
        log.info("1:1 채팅 메시지 수신: roomId={}, senderId={}",
                socketMessage.getRoomId(), socketMessage.getSenderId());

        ChatMessageResponse savedMessage = chatMessageService.saveMessage(socketMessage);
        messagingTemplate.convertAndSend("/queue/chat/room/" + savedMessage.getChatRoomId(), savedMessage);

        log.info("1:1 메시지 전송 완료: messageId={}", savedMessage.getId());
    }
}