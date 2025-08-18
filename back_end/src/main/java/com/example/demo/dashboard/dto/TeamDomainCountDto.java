package com.example.demo.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "팀별 도메인 카운트 집계용 DTO (내부 사용) 0=back 1=front 2=ai 3=design 4=pm")
public class TeamDomainCountDto {
    private String domain;
    private Long back;
    private Long ai;
    private Long pm;
    private Long front;
    private Long design;
}
