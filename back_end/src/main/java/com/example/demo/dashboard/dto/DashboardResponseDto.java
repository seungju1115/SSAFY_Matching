package com.example.demo.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;
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

    @Schema(description = "ai가 메인인 개발자의 수", example = "20")
    private Integer ai_main;

    @Schema(description = "백엔드가 메인인 개발자 수", example = "35")
    private Integer back_main;

    @Schema(description = "프론트엔드가 메인인 개발자 수", example = "25")
    private Integer front_main;

    @Schema(description = "디자인이 메인인 개발자 수", example = "25")
    private Integer design_main;

    @Schema(description = "프로젝트 매니저가 메인인 개발자 수", example = "25")
    private Integer pm_main;

    @Schema(description = "ai가 서브인 개발자의 수", example = "20")
    private Integer ai_sub;

    @Schema(description = "백엔드가 서브인 개발자 수", example = "35")
    private Integer back_sub;

    @Schema(description = "프론트엔드가 서브인 개발자 수", example = "25")
    private Integer front_sub;

    @Schema(description = "디자인이 서브인 개발자 수", example = "25")
    private Integer design_sub;

    @Schema(description = "프로젝트 매니저가 서브인 개발자 수", example = "25")
    private Integer pm_sub;


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
    private Map<String, Long> domain;

    @Schema(
            description = "도메인별 포지션 별 인원 수 통계",
            example = """
        {
            "WEB": {back, front, ai, design, pm},
            "MOBILE": {back, front, ai, design, pm},
            "AI": {back, front, ai, design, pm},
            "IOT": {back, front, ai, design, pm},
            "GAME": {back, front, ai, design, pm},
            "DATA": {back, front, ai, design, pm}
        }
        """
    )
    private Map<String, List<Long>> domainPos;

    @Schema(
            description = "기술 스택 별 인원 수",
            example = """
        {
            "Docker": 52,
            "MYSQL": 42,
            "VUE": 51,
            "PYTHON": 86,
            "C++": 75,
            "JAVA": 18
        }
        """
    )
    private Map<String, Long> techstacks;
}