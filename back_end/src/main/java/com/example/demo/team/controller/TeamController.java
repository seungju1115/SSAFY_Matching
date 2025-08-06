// TeamController.java - 완전한 Swagger 설정
package com.example.demo.team.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.service.TeamMembershipRequestService;
import com.example.demo.team.service.TeamService;
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
@RequestMapping("/team")
@RequiredArgsConstructor
@Tag(
        name = "팀 관리",
        description = "팀 생성, 조회, 수정, 삭제 및 팀원 관리 API"
)
public class TeamController {

    private final TeamService teamService;
    private final TeamMembershipRequestService teamMembershipRequestService;

    @Operation(
            summary = "팀 생성",
            description = """
            새로운 팀을 생성합니다.
            
            **생성 과정:**
            1. 팀 이름과 팀장 정보로 팀 생성
            2. 팀장은 자동으로 팀에 소속됨
            3. 생성된 팀 정보 반환
            
            **필수 정보:**
            - 팀 이름 (중복 불가)
            - 팀장 ID (유효한 사용자 ID)
            
            **주의사항:**
            - 이미 팀에 소속된 사용자는 팀장이 될 수 없음
            - 팀 이름은 최소 2자 이상 20자 이하
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "팀 생성 성공",
                                    value = """
                    {
                        "status": 200,
                        "message": "리소스가 성공적으로 생성되었습니다",
                        "data": {
                            "teamId": 1,
                            "teamName": "프로젝트 A팀",
                            "leaderId": 5,
                            "memberCount": 1
                        }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "중복된 팀 이름 또는 이미 팀에 소속된 사용자"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Parameter(
                    description = "생성할 팀 정보",
                    required = true,
                    schema = @Schema(implementation = TeamCreateRequest.class)
            )
            @Valid @RequestBody TeamCreateRequest teamCreateRequest
    ) {
        TeamResponse teamResponse = teamService.createTeam(teamCreateRequest);
        return ResponseEntity.ok(ApiResponse.created(teamResponse));
    }

    @Operation(
            summary = "전체 팀 목록 조회",
            description = """
            시스템에 등록된 모든 팀의 목록을 조회합니다.
            
            **반환 정보:**
            - 팀 ID, 팀 이름
            - 팀장 정보
            - 현재 팀원 수
            
            **정렬 기준:**
            - 최신 생성 순
            - 활성 팀 우선 표시
            
            **사용 시나리오:**
            - 전체 팀 현황 파악
            - 관리자 대시보드
            - 팀 검색의 기본 데이터
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "팀 목록 조회 성공",
                                    value = """
                    {
                        "status": 200,
                        "message": "요청이 성공했습니다",
                        "data": [
                            {
                                "teamId": 1,
                                "teamName": "프로젝트 A팀",
                                "leaderId": 5,
                                "memberCount": 3
                            },
                            {
                                "teamId": 2,
                                "teamName": "프로젝트 B팀",
                                "leaderId": 8,
                                "memberCount": 2
                            }
                        ]
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        List<TeamResponse> teamResponses = teamService.getAllTeams();
        return ResponseEntity.ok(ApiResponse.ok(teamResponses));
    }

    @Operation(
            summary = "조건별 팀 검색",
            description = """
            특정 조건에 맞는 팀들을 검색합니다.
            
            **검색 조건:**
            - 팀 이름 (부분 일치)
            - 팀장 ID
            - 팀 ID
            
            **검색 로직:**
            - 입력된 조건들은 AND 조건으로 적용
            - 팀 이름은 대소문자 구분 없이 부분 검색
            - 빈 조건은 무시됨
            
            **활용 예시:**
            - 특정 팀장의 팀 찾기
            - 팀 이름으로 검색
            - 팀 필터링
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 검색 조건"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> searchConditionTeam(
            @Parameter(
                    description = "팀 검색 조건",
                    required = true,
                    schema = @Schema(implementation = TeamRequest.class)
            )
            @Valid @RequestBody TeamRequest teamRequest
    ) {
        List<TeamResponse> teamResponses = teamService.searchConditionTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamResponses));
    }

    @Operation(
            summary = "팀 상세 정보 조회",
            description = """
            특정 팀의 상세 정보를 조회합니다.
            
            **상세 정보 포함:**
            - 팀 기본 정보 (ID, 이름, 팀장)
            - 전체 팀원 목록 (ID만)
            
            **접근 권한:**
            - 팀원은 자신의 팀 정보 조회 가능
            - 관리자는 모든 팀 정보 조회 가능
            - 외부 사용자는 기본 정보만 조회 가능
            
            **사용 시나리오:**
            - 팀 상세 페이지
            - 팀원 관리
            - 팀 정보 수정 전 현재 상태 확인
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "팀 상세 정보",
                                    value = """
                    {
                        "status": 200,
                        "message": "요청이 성공했습니다",
                        "data": {
                            "teamId": 1,
                            "teamName": "프로젝트 A팀",
                            "leaderId": 5,
                            "membersId": [5, 12, 18, 23]
                        }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 팀"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "팀 정보 조회 권한 없음"
            )
    })
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeam(
            @Parameter(
                    description = "조회할 팀 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long teamId
    ) {
        TeamDetailResponse teamDetailResponse = teamService.getTeam(teamId);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    @Operation(
            summary = "팀 삭제",
            description = """
            특정 팀을 완전히 삭제합니다.
            
            **삭제 권한:**
            - 팀장만 삭제 가능
            - 관리자는 모든 팀 삭제 가능
            
            **삭제 과정:**
            1. 모든 팀원의 팀 소속 해제
            2. 팀 관련 데이터 정리
            3. 채팅방 등 연관 데이터 처리
            4. 팀 완전 삭제
            
            **주의사항:**
            - 삭제 후 복구 불가능
            - 팀원들에게 자동 알림 발송
            - 진행 중인 프로젝트 확인 필요
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 삭제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 팀"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "팀 삭제 권한 없음"
            )
    })
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @Parameter(
                    description = "삭제할 팀 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long teamId
    ) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @Operation(
            summary = "팀 정보 수정",
            description = """
            기존 팀의 정보를 수정합니다.
            
            **수정 가능한 정보:**
            - 팀 이름
            - 팀장 변경 (팀원 중에서만 가능)
            
            **수정 권한:**
            - 현재 팀장만 수정 가능
            - 관리자는 모든 팀 수정 가능
            
            **팀장 변경 시:**
            - 새 팀장은 반드시 현재 팀원이어야 함
            - 기존 팀장은 일반 팀원으로 변경
            - 팀장 변경 시 모든 팀원에게 알림
            
            **참고:** PUT 방식으로 전체 정보 교체
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 수정 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 팀 또는 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "팀 수정 권한 없음"
            )
    })
    @PutMapping
    public ResponseEntity<ApiResponse<TeamDetailResponse>> modifyTeam(
            @Parameter(
                    description = "수정할 팀 정보",
                    required = true,
                    schema = @Schema(implementation = TeamRequest.class)
            )
            @Valid @RequestBody TeamRequest teamRequest
    ) {
        TeamDetailResponse teamDetailResponse = teamService.modifyTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    @Operation(
            summary = "팀원 직접 초대",
            description = """
            관리자 권한으로 사용자를 팀에 직접 초대합니다.
            
            **직접 초대 특징:**
            - 사용자 동의 없이 즉시 팀에 추가
            - 관리자나 팀장만 사용 가능
            - 초대받은 사용자에게 알림 발송
            
            **초대 조건:**
            - 초대할 사용자가 현재 팀이 없어야 함
            - 팀 정원이 초과되지 않아야 함
            - 유효한 사용자 ID여야 함
            
            **참고:** 일반적인 초대 요청(`/offer`)과 구분됨
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀원 초대 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "초대 불가능한 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자 또는 팀"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "초대 권한 없음"
            )
    })
    @GetMapping("/invitation")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> inviteMemberTeam(
            @Parameter(
                    description = "팀원 초대 정보",
                    required = true,
                    schema = @Schema(implementation = TeamInviteRequest.class)
            )
            @Valid @RequestBody TeamInviteRequest teamInviteRequest
    ) {
        TeamDetailResponse teamDetailResponse = teamService.inviteMemberTeam(teamInviteRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    @Operation(
            summary = "팀 가입 요청 / 초대 요청",
            description = """
            팀 가입 요청 또는 팀원 초대 요청을 처리합니다.
            
            **요청 타입:**
            - `JOIN`: 사용자가 팀에 가입 요청
            - `INVITE`: 팀에서 사용자에게 초대 요청
            
            **가입 요청 (JOIN):**
            - 사용자가 특정 팀에 가입 신청
            - 팀장이 승인/거절 결정
            - 메시지로 가입 이유 전달 가능
            
            **초대 요청 (INVITE):**
            - 팀장이 특정 사용자를 초대
            - 사용자가 수락/거절 결정
            - 메시지로 초대 이유 전달 가능
            
            **처리 흐름:**
            1. 요청 생성 및 저장
            2. 관련 당사자에게 알림 발송
            3. 승인 대기 상태로 변경
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "요청 생성 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 처리 중인 요청이 존재"
            )
    })
    @PostMapping("/offer")
    public ResponseEntity<ApiResponse<Void>> teamOffer(
            @Parameter(
                    description = "팀 가입/초대 요청 정보",
                    required = true,
                    schema = @Schema(implementation = TeamOffer.class)
            )
            @Valid @RequestBody TeamOffer teamOffer
    ) {
        if (teamOffer.getRequestType() != RequestType.INVITE) {
            teamMembershipRequestService.requestMemberToTeam(teamOffer);
        } else {
            teamMembershipRequestService.requestTeamToMember(teamOffer);
        }

        return ResponseEntity.ok(ApiResponse.created(null));
    }

    @Operation(
            summary = "팀원 목록 조회",
            description = """
            특정 팀의 팀원 목록을 조회합니다.
            
            **반환 정보:**
            - 팀원 ID와 사용자명
            - 팀장 여부는 별도 표시되지 않음 (팀 상세에서 확인)
            
            **접근 권한:**
            - 팀원은 자신의 팀 멤버 조회 가능
            - 외부 사용자도 기본 정보 조회 가능
            
            **사용 시나리오:**
            - 팀원 목록 표시
            - 팀 구성원 확인
            - 채팅 참여자 목록
            
            **참고:** ApiResponse 래퍼 없이 직접 반환
            """,
            tags = {"팀 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "팀원 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeamMemberResponse.class),
                            examples = @ExampleObject(
                                    name = "팀원 목록",
                                    value = """
                    [
                        {
                            "memberId": 5,
                            "username": "팀장홍길동"
                        },
                        {
                            "memberId": 12,
                            "username": "김개발"
                        },
                        {
                            "memberId": 18,
                            "username": "이디자인"
                        }
                    ]
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 팀"
            )
    })
    @GetMapping("/{teamId}/members")
    public List<TeamMemberResponse> getTeamMembers(
            @Parameter(
                    description = "팀원을 조회할 팀 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long teamId
    ) {
        return teamService.getTeamMembers(teamId);
    }
}