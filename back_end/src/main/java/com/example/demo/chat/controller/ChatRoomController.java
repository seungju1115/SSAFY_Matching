// ChatRoomController.java - 완전한 Swagger 설정
package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@Tag(
        name = "채팅방 관리",
        description = "채팅방 생성, 조회 및 메시지 히스토리 관리 API"
)
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @Operation(
            summary = "채팅방 메시지 히스토리 조회",
            description = """
            채팅방 입장 시 해당 방의 모든 메시지 로그를 조회합니다.
            
            **사용 시나리오:**
            - 사용자가 채팅방에 입장할 때
            - 채팅 히스토리를 불러올 때
            - 페이지 새로고침 후 이전 대화 복원 시
            
            **응답 데이터:**
            - 메시지는 생성 시간 순으로 정렬됩니다
            - 빈 채팅방의 경우 빈 배열을 반환합니다
            """,
            tags = {"채팅방 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "메시지 히스토리 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "메시지 히스토리 응답",
                                    value = """
                    {
                        "success": true,
                        "message": "요청이 성공했습니다",
                        "data": [
                            {
                                "id": 1,
                                "chatRoomId": 123,
                                "senderId": 1,
                                "message": "안녕하세요!",
                                "createdAt": "2024-01-15T10:30:00"
                            },
                            {
                                "id": 2,
                                "chatRoomId": 123,
                                "senderId": 2,
                                "message": "네, 안녕하세요!",
                                "createdAt": "2024-01-15T10:31:00"
                            }
                        ]
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 채팅방",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "채팅방 없음",
                                    value = """
                    {
                        "success": false,
                        "message": "채팅방을 찾을 수 없습니다",
                        "data": null
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "채팅방 접근 권한 없음"
            )
    })
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(
            @Parameter(
                    description = "조회할 채팅방 ID",
                    required = true,
                    example = "123"
            )
            @PathVariable Long chatRoomId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(chatMessageService.getAllMessagesByChatRoom(chatRoomId)));
    }

    @Operation(
            summary = "1:1 개인 채팅방 생성",
            description = """
            두 사용자 간의 1:1 개인 채팅방을 생성합니다.
            
            **동작 방식:**
            - 이미 두 사용자 간 채팅방이 존재하면 기존 방 정보를 반환
            - 존재하지 않으면 새로운 채팅방을 생성
            - roomType은 반드시 "PRIVATE"로 설정
            
            **요청 예시:**
            ```json
            {
                "roomType": "PRIVATE",
                "user1Id": 1,
                "user2Id": 2
            }
            ```
            
            **주의사항:**
            - user1Id와 user2Id는 서로 달라야 합니다
            - 존재하는 사용자 ID여야 합니다
            """,
            tags = {"채팅방 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "채팅방 생성 성공 (기존 방 반환 포함)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "채팅방 생성 성공",
                                    value = """
                    {
                        "success": true,
                        "message": "채팅방이 생성되었습니다",
                        "data": {
                            "roomId": 456,
                            "roomType": "PRIVATE",
                            "teamId": null
                        }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "유효성 검사 실패",
                                    value = """
                    {
                        "success": false,
                        "message": "입력값 검증에 실패했습니다",
                        "data": {
                            "user1Id": "사용자 ID는 필수입니다",
                            "user2Id": "사용자 ID는 필수입니다"
                        }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "동일한 사용자 ID로 채팅방 생성 불가"
            )
    })
    @PostMapping("/private")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createPrivateChatRoom(
            @Parameter(
                    description = "1:1 채팅방 생성 요청 정보",
                    required = true,
                    schema = @Schema(implementation = ChatRoomRequest.class)
            )
            @Valid @RequestBody ChatRoomRequest chatRoomRequest
    ) {
        if (chatRoomRequest.getUser1Id() == null || chatRoomRequest.getUser2Id() == null) {
            throw new BusinessException(ErrorCode.INVALID_PRIVATEROOM_REQUEST);
        }
        return ResponseEntity.ok(ApiResponse.created(chatRoomService.createPrivateChatRoom(chatRoomRequest)));
    }
}