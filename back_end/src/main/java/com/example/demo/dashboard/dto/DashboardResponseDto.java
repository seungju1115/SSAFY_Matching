package com.example.demo.dashboard.dto;

import lombok.*;

import java.io.Serializable;

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
}
