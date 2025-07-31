package com.example.demo.dashboard.service;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public DashboardResponseDto getDashboard() {
        DashboardResponseDto dashboardResponseDto=new DashboardResponseDto();
        return dashboardResponseDto;
    }
}
