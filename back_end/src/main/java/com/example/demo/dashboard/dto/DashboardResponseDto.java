package com.example.demo.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "대시보드 통계 데이터 응답")
public class DashboardResponseDto implements Serializable {

    @Schema(description = "전체 사용자 수", example = "150")
    private Integer whole;

    @Schema(description = "팀에 매칭된 전공자 수", example = "45")
    private Integer matchedMajor;

    @Schema(description = "팀에 매칭된 비전공자 수", example = "25")
    private Integer mathcedUnmajor;

    @Schema(description = "팀에 매칭되지 않은 전공자 수", example = "55")
    private Integer unmatchedMajor;

    @Schema(description = "팀에 매칭되지 않은 비전공자 수", example = "25")
    private Integer unmatchedUnmajor;

    @Schema(description = "팀에 매칭된 백엔드 개발자 수", example = "30")
    private Integer matched_back;

    @Schema(description = "팀에 매칭된 프론트엔드 개발자 수", example = "25")
    private Integer matched_front;

    @Schema(description = "팀에 매칭된 AI 개발자 수", example = "15")
    private Integer matched_ai;

    @Schema(description = "팀에 매칭된 디자이너 수", example = "15")
    private Integer matched_design;

    @Schema(description = "팀에 매칭된 프로젝트 매니저 수", example = "15")
    private Integer matched_pm;

    @Schema(description = "팀에 매칭되지 않은 기타 포지션 수", example = "20")
    private Integer unmatched_ai;

    @Schema(description = "팀에 매칭되지 않은 백엔드 개발자 수", example = "35")
    private Integer unmatched_back;

    @Schema(description = "팀에 매칭되지 않은 프론트엔드 개발자 수", example = "25")
    private Integer unmatched_front;

    @Schema(description = "팀에 매칭되지 않은 디자이너 수", example = "25")
    private Integer unmatched_design;

    @Schema(description = "팀에 매칭되지 않은 프로젝트 매니저 수", example = "25")
    private Integer unmatched_pm;

    @Schema(
            description = "도메인별 참여자 수 통계",
            example = """
        {
            "WEB": 45,
            "MOBILE": 32,
            "AI": 28,
            "IOT": 15,
            "GAME": 12,
            "DATA": 18
        }
        """
    )
    private Map<String, Integer> domain;
}