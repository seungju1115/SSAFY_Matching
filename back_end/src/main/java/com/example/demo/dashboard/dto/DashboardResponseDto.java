package com.example.demo.dashboard.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DashboardResponseDto implements Serializable {
    private Integer whole;
    private Integer matchedMajor;
    private Integer mathcedUnmajor;
    private Integer unmatchedMajor;
    private Integer unmatchedUnmajor;

    private Integer matched_back;
    private Integer matched_front;
    private Integer matched_misc;
    private Integer unmatched_misc;
    private Integer unmatched_back;
    private Integer unmatched_front;

    private Map<String, Integer> domain;
}
