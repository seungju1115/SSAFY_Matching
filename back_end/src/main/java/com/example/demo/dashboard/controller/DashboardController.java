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

import java.time.LocalDateTime;

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
                                                  "whole": 10,
                                                  "matchedMajor": 4,
                                                  "mathcedUnmajor": 1,
                                                  "unmatchedMajor": 3,
                                                  "unmatchedUnmajor": 2,
                                                  "ai_main": 4,
                                                  "back_main": 2,
                                                  "front_main": 0,
                                                  "design_main": 4,
                                                  "pm_main": 0,
                                                  "ai_sub": 0,
                                                  "back_sub": 4,
                                                  "front_sub": 5,
                                                  "design_sub": 0,
                                                  "pm_sub": 1,
                                                  "domain": {
                                                    "서버 개발": 1,
                                                    "AI/ML": 1,
                                                    "UX/UI": 1,
                                                    "전체 개발": 1,
                                                    "웹 개발": 1
                                                  },
                                                  "domainPos": {
                                                    "서버 개발": [
                                                      3,
                                                      0,
                                                      0,
                                                      1,
                                                      1
                                                    ],
                                                    "AI/ML": [
                                                      1,
                                                      1,
                                                      1,
                                                      1,
                                                      3
                                                    ],
                                                    "UX/UI": [
                                                      1,
                                                      2,
                                                      1,
                                                      1,
                                                      0
                                                    ],
                                                    "전체 개발": [
                                                      2,
                                                      1,
                                                      2,
                                                      1,
                                                      1
                                                    ],
                                                    "웹 개발": [
                                                      1,
                                                      1,
                                                      2,
                                                      1,
                                                      0
                                                    ]
                                                  },
                                                  "techstacks": {
                                                    DOCKER : 51
                                                    JAVA : 39
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
    public ResponseEntity<DashboardResponseDto> getDashboard() {
        long time = System.nanoTime();
        DashboardResponseDto dto = dashboardService.getDashboard();
        long elapsed = System.nanoTime() - time;
        return ResponseEntity.ok()
                .header("X-Execution-Time-Ms", String.valueOf(elapsed))
                .body(dto);
    }
}