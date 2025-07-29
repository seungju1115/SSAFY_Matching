package com.example.demo.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SummaryResoponseDto {
    private Integer whole;
    private Integer backendDemand;
    private Integer backendSupply;
    private Integer FrontendDemand;
    private Integer FrontendSupply;
}
