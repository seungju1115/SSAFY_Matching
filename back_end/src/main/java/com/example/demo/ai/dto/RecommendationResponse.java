package com.example.demo.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

// RecSys 응답을 받기 위한 DTO
@Getter
@Setter
@NoArgsConstructor
public class RecommendationResponse {
    private String status;
    private String message;
    private List<Map<String, Object>> results;
}