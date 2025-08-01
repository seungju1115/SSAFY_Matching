package com.example.demo.dashboard.controller;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.dto.SummaryResoponseDto;
import com.example.demo.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;


    @GetMapping("/graph")
    public DashboardResponseDto getDashboard() {
        DashboardResponseDto dto=dashboardService.getDashboard();
        return dto;
    }


    @GetMapping("/position")
    public SummaryResoponseDto  getPosition() {
        SummaryResoponseDto dto=new SummaryResoponseDto();
        return dto;
    }
}
