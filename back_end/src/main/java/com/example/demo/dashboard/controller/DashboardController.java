// DashboardController.java - 완전한 Swagger 설정
package com.example.demo.dashboard.controller;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.dto.SummaryResoponseDto;
import com.example.demo.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(
        name = "대시보드",
        description = "시스템 통계 및 현황 정보를 제공하는 대시보드 API"
)
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "대시보드 그래프 데이터 조회",
            description = """
            대시보드에 표시할 각종 통계 그래프 데이터를 조회합니다.
            
            **제공 통계:**
            
            **1. 전체 사용자 현황**
            - 전체 사용자 수
            - 팀 매칭된 전공자/비전공자 수
            - 팀 매칭되지 않은 전공자/비전공자 수
            
            **2. 포지션별 매칭 현황**
            - 백엔드: 매칭됨/매칭안됨
            - 프론트엔드: 매칭됨/매칭안됨  
            - 기타 포지션: 매칭됨/매칭안됨
            
            **3. 도메인별 통계**
            - 각 프로젝트 도메인별 참여자 수
            - 인기 도메인 현황
            
            **캐싱 적용:**
            - 성능 최적화를 위해 캐싱 적용
            - 일정 주기로 데이터 갱신
            
            **사용 시나리오:**
            - 관리자 대시보드 메인 화면
            - 전체 현황 파악
            """,
            tags = {"대시보드"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "대시보드 데이터 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardResponseDto.class),
                            examples = @ExampleObject(
                                    name = "대시보드 통계 데이터",
                                    value = """
                    {
                        "whole": 150,
                        "matchedMajor": 45,
                        "mathcedUnmajor": 25,
                        "unmatchedMajor": 55,
                        "unmatchedUnmajor": 25,
                        "matched_back": 30,
                        "matched_front": 25,
                        "matched_misc": 15,
                        "unmatched_misc": 20,
                        "unmatched_back": 35,
                        "unmatched_front": 25,
                        "domain": {
                            "WEB": 45,
                            "MOBILE": 32,
                            "AI": 28,
                            "IOT": 15,
                            "GAME": 12,
                            "DATA": 18
                        }
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류"
            )
    })
    @GetMapping("/graph")
    @Cacheable(value = "dashboardCache", key = "'graph'")
    public DashboardResponseDto getDashboard() {
        DashboardResponseDto dto = dashboardService.getDashboard();
        return dto;
    }

    @Operation(
            summary = "포지션별 요약 통계 조회",
            description = """
            포지션별 상세 요약 통계 정보를 조회합니다.
            
            **현재 상태:**
            - 구현 예정 상태 (빈 DTO 반환)
            - 향후 포지션별 세부 통계 제공 예정
            
            **예정 기능:**
            - 포지션별 사용자 수
            - 포지션별 매칭률
            - 포지션별 선호 기술 스택
            - 포지션별 프로젝트 선호도
            - 포지션별 팀 구성 현황
            
            **활용 계획:**
            - 포지션 밸런스 분석
            - 인기 포지션 트렌드
            - 교육과정 기획 참고 자료
            - 멘토링 배정 기준
            
            **참고:** 현재는 개발 중이며, 추후 실제 데이터로 채워질 예정입니다.
            """,
            tags = {"대시보드"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "포지션별 통계 조회 성공 (현재는 빈 데이터)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SummaryResoponseDto.class),
                            examples = @ExampleObject(
                                    name = "포지션 요약 데이터 (예정)",
                                    value = """
                    {
                        "note": "현재 구현 중입니다. 향후 포지션별 상세 통계가 제공될 예정입니다."
                    }
                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류"
            )
    })
    @GetMapping("/position")
    public SummaryResoponseDto getPosition() {
        SummaryResoponseDto dto = new SummaryResoponseDto();
        return dto;
    }
}