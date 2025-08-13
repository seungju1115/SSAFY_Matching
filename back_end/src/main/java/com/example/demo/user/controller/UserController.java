// UserController.java - 완전한 Swagger 설정
package com.example.demo.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.user.dto.*;
import com.example.demo.user.dto.UserSearchRequest;
import com.example.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/profile")
@Tag(
        name = "사용자 프로필 관리",
        description = "사용자 프로필 생성, 조회, 수정, 삭제 및 팀원 검색 API"
)
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "내 프로필 조회",
            description = """
            현재 로그인한 사용자의 프로필 정보를 조회합니다.
            
            **인증 필요:** JWT 토큰을 통해 사용자를 식별합니다.
            
            **반환 정보:**
            - 기본 정보 (이름, 이메일, 역할 등)
            - 전공 여부, 기수 정보
            - 희망 포지션 및 기술 스택
            - 프로젝트 선호도 및 개인 성향
            - 소속 팀 정보 (있는 경우)
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "내 프로필 조회 성공",
                                    value = """
                    {
                      "status": 200,
                      "message": "요청 성공",
                      "data": {
                        "id": 1,
                        "userName": "홍길동",
                        "role": "student",
                        "email": "honggildong@gmail.com",
                        "userProfile": "저는 ...",
                        "major": false,
                        "lastClass": 3,
                        "wantedPosition": ["BACKEND", "AI"],
                        "projectGoal": ["STABLE", "CHALLENGE"],
                        "projectVive": ["COMMUNICATE", "CONCENTRATE"],
                        "projectExp": "졸업 프로젝트로 쇼핑몰 제작 경험 있음",
                        "qualification": "정보처리기사, SQLD",
                        "techStack": ["SPRING", "DOCKER", "MYSQL"],
                        "teamId": 42,
                        "teamName": "코드마스터즈"
                      }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자 프로필을 찾을 수 없음"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse> getProfileWithoutId() {
        UserProfileResponse userProfileResponse = userService.getProfile();
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @Operation(
            summary = "특정 사용자 프로필 조회",
            description = """
            특정 사용자의 프로필 정보를 조회합니다.
            
            **사용 시나리오:**
            - 팀원 정보 확인
            - 다른 사용자 프로필 보기
            - 관리자의 사용자 정보 조회
            
            **주의사항:**
            - 공개된 정보만 조회 가능
            - 개인정보는 제한적으로 노출
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "프로필 조회 권한 없음"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProfileWithId(
            @Parameter(
                    description = "조회할 사용자 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        UserProfileResponse userProfileResponse = userService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @Operation(
            summary = "사용자 프로필 생성",
            description = """
            새로운 사용자 프로필을 생성합니다.
            
            **필수 입력 정보:**
            - 사용자 이메일
            - 사용자 이름
            - 전공 여부
            - 지난 반 정보
            
            **선택 입력 정보:**
            - 희망 포지션
            - 자기소개
            - 기술 스택
            - 프로젝트 선호도
            - 개인 성향
            - 프로젝트 경험
            - 자격증 정보
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 프로필이 존재함"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse> createProfile(
            @Parameter(
                    description = "생성할 프로필 정보",
                    required = true,
                    schema = @Schema(implementation = UserProfileRequest.class)
            )
            @Valid @RequestBody UserProfileRequest userProfileRequest
    ) {
        UserProfileResponse userProfileResponse = userService.saveProfile(userProfileRequest);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @Operation(
            summary = "사용자 프로필 삭제",
            description = """
            특정 사용자의 프로필을 삭제합니다.
            
            **주의사항:**
            - 관리자 권한 또는 본인만 삭제 가능
            - 삭제 후 복구 불가능
            - 팀에 소속된 경우 팀 탈퇴 처리
            
            **삭제되는 정보:**
            - 프로필 정보 전체
            - 팀 소속 정보
            - 관련 활동 기록
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 삭제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "삭제 권한 없음"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProfile(
            @Parameter(
                    description = "삭제할 사용자 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        userService.deleteProfile(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @Operation(
            summary = "사용자 프로필 수정",
            description = """
            기존 사용자 프로필 정보를 수정합니다.
            
            **수정 가능한 정보:**
            - 기본 정보 (이름, 자기소개)
            - 전공, 비전공, 지난 반
            - 희망 포지션
            - 기술 스택
            - 프로젝트 선호도 및 개인 성향
            - 프로젝트 경험, 자격증
            - 팀 변경 (teamId)
            
            **Patch 방식:**
            - 전달된 필드만 수정됨
            - null 값은 무시됨
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "수정 권한 없음"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProfile(
            @Parameter(
                    description = "수정할 프로필 정보 (부분 수정)",
                    required = true,
                    schema = @Schema(implementation = UserProfileUpdateRequest.class)
            )
            @Valid @RequestBody UserProfileUpdateRequest userProfileUpdateRequest,

            @Parameter(
                    description = "수정할 사용자 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        UserProfileResponse userProfileResponse = userService.updateUserProfile(userProfileUpdateRequest, id);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @Operation(
            summary = "팀원 검색",
            description = """
            조건에 맞는 팀이 없는 사용자들을 검색합니다.
            
            **검색 조건:**
            - 전공, 비전공(true, false)
            - 희망 포지션 (FRONTEND, BACKEND 등)
            - 기술 스택 (JAVA, REACT, PYTHON 등)
            - 프로젝트 목표 (PROFESSIONAL, JOB 등)
            - 개인 성향 (RULE, LEADER 등)
            
            **검색 로직:**
            - 모든 조건은 AND 조건으로 적용
            - 기술 스택과 프로젝트 선호도는 교집합으로 매칭
            - 팀이 없는 사용자만 반환
            
            **활용 시나리오:**
            - 팀 구성 시 적합한 팀원 찾기
            - 프로젝트에 맞는 개발자 검색
            - 스킬 기반 매칭
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "팀원 검색 결과",
                                    value = """
                    {
                        "status": 200,
                        "message": "요청 성공",
                        "data": [
                            {
                                "id": 6,
                                "userName": "홍길동",
                                "userProfile": "새로운 팀을 찾고 있는 백엔드 개발자입니다.",
                                "major": true,
                                "lastClass": 4,
                                "wantedPosition": [
                                    "BACKEND",
                                    "AI"
                                ],
                                "techStack": [],
                                "projectGoal": [
                                    "PROFESSIONAL",
                                    "JOB"
                                ],
                                "projectVive": [
                                    "RULE",
                                    "LEADER"
                                ],
                                "projectExp": "Java Spring 프로젝트 경험",
                                "qualification": "정보처리기사"
                            },
                            {
                                "id": 10,
                                "userName": "조현우",
                                "userProfile": "팀 리딩 경험이 있는 PM 지망생입니다.",
                                "major": true,
                                "lastClass": 4,
                                "wantedPosition": [
                                    "PM",
                                    "BACKEND"
                                ],
                                "techStack": [],
                                "projectGoal": [
                                    "PROFESSIONAL",
                                    "JOB"
                                ],
                                "projectVive": [
                                    "FORMAL",
                                    "LEADER"
                                ],
                                "projectExp": "프로젝트 관리 경험 2회",
                                "qualification": "PMP 준비중"
                            }
                        ]
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 검색 조건"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsersWithoutTeamWithCondition(
            @Parameter(
                    description = "팀원 검색 조건",
                    required = true,
                    schema = @Schema(implementation = UserSearchRequest.class)
            )
            @Valid @RequestBody UserSearchRequest request
    ) {
        List<UserSearchResponse> users = userService.searchUsersWithoutTeam(request);
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @Operation(
            summary = "대기중인 사용자 조회",
            description = """
            UserStatus가 WAITING인 사용자들을 조회합니다.
            
            **조회 조건:**
            - userStatus = WAITING (팀을 찾고 있는 상태)
            - 팀에 소속되지 않은 사용자들
            
            **반환 정보:**
            - 기본 프로필 정보
            - 희망 포지션 및 기술 스택
            - 프로젝트 선호도
            
            **활용 시나리오:**
            - 팀 구성 시 대기중인 개발자 목록 보기
            - 매칭 시스템에서 활용
            """,
            tags = {"사용자 프로필 관리"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "대기중인 사용자 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/waiting")
    public ResponseEntity<ApiResponse> getWaitingUsers() {
        List<UserSearchResponse> waitingUsers = userService.getWaitingUsers();
        return ResponseEntity.ok(ApiResponse.ok(waitingUsers));
    }
}