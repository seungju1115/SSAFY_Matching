package com.example.demo.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DashboardResponseDto {
    private Integer whole;
    private Integer matchedMajor;
    private Integer mathcedUnmajor;
    private Integer unmatchedMajor;
    private Integer unmatchedUnmajor;
}
