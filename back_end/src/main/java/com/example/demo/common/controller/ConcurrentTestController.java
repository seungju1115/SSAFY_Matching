package com.example.demo.common.controller;

import com.example.demo.team.dto.TeamOffer;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.service.TeamMembershipRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class ConcurrentTestController {

    @Autowired
    private TeamMembershipRequestService trservice;

    // 동시 요청 시뮬레이션 API
    @GetMapping("/simulate-concurrent")
    public List<String> simulateConcurrentRequests() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = new ArrayList<>();

        // 3개 동시 실행
        for (int i = 1; i <= 3; i++) {
            final int threadNum = i;
            Future<String> future = executor.submit(() -> {
                try {
                    trservice.requestTeamToMember(new TeamOffer(RequestType.INVITE, 1L, "초대", 1L));
                    return "Thread-" + threadNum + ": 성공";
                } catch (Exception e) {
                    return "Thread-" + threadNum + ": 실패 - " + e.getMessage();
                }
            });
            futures.add(future);
        }

        // 결과 수집
        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                results.add(future.get(10, TimeUnit.SECONDS));
            } catch (Exception e) {
                results.add("타임아웃 또는 오류");
            }
        }

        executor.shutdown();
        return results;
    }
}