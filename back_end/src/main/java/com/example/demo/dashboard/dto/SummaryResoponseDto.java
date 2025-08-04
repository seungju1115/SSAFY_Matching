package com.example.demo.dashboard.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SummaryResoponseDto implements Serializable {
    private Integer whole;
    private Integer backendDemand;
    private Integer backendSupply;
    private Integer FrontendDemand;
    private Integer FrontendSupply;
}
