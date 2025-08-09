package com.example.demo.dashboard.service;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.dto.UserCountDto;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    @Cacheable(value = "shortTermCache")
    public DashboardResponseDto getDashboard() {

        DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();

        List<UserCountDto> users=new ArrayList<>();
        List<User> list= userRepository.CountUsers();
        for(User user:list){
            users.add(UserCountDto.from(user));
        }
        int whole= users.size();
        int matched_major=0; int matched_unmajor=0; int unmatched_major=0; int unmatched_unmajor=0;

        for (UserCountDto user : users) {
            boolean hasTeam = user.getTeam() != null;
            if (user.getMajor()) {
                if (hasTeam) matched_major++;
                else unmatched_major++;
            } else {
                if (hasTeam) matched_unmajor++;
                else unmatched_unmajor++;
            }
        }


        int matched_back = 0, matched_front = 0, matched_ai = 0, matched_design=0, matched_pm=0;
        int unmatched_back = 0, unmatched_front = 0, unmatched_ai = 0, unmatched_design=0, unmatched_pm=0;

        for (UserCountDto user : users) {
            boolean hasTeam = user.getTeam() != null;
            for(PositionEnum position : user.getPosition()) {
                switch (position) {
                    case BACKEND:
                        if (hasTeam) matched_back++;
                        else unmatched_back++;
                        break;
                    case FRONTEND:
                        if (hasTeam) matched_front++;
                        else unmatched_front++;
                        break;
                    case AI:
                        if (hasTeam) matched_ai++;
                        else unmatched_ai++;
                        break;
                    case DESIGN:
                        if (hasTeam) matched_design++;
                        else unmatched_design++;
                        break;
                    case PM:
                        if (hasTeam) matched_pm++;
                        else unmatched_pm++;
                        break;
                }
            }
        }

        Map<String, Long> map = new HashMap<>();

        map=teamRepository.countDomain().stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
        ));

        return DashboardResponseDto.builder()
                .whole(whole)
                .matchedMajor(matched_major)
                .mathcedUnmajor(matched_unmajor)
                .unmatchedMajor(unmatched_major)
                .unmatchedUnmajor(unmatched_unmajor)
                .matched_back(matched_back)
                .matched_front(matched_front)
                .matched_ai(matched_ai)
                .matched_design(matched_design)
                .matched_pm(matched_pm)
                .unmatched_back(unmatched_back)
                .unmatched_front(unmatched_front)
                .unmatched_ai(unmatched_ai)
                .unmatched_design(unmatched_design)
                .unmatched_pm(unmatched_pm)
                .domain(map)
                .build();
    }

    public DashboardResponseDto getDashboardNoCache() {

        DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();

        List<UserCountDto> users=new ArrayList<>();
        List<User> list= userRepository.CountUsers();
        for(User user:list){
            users.add(UserCountDto.from(user));
        }
        int whole= users.size();
        int matched_major=0; int matched_unmajor=0; int unmatched_major=0; int unmatched_unmajor=0;

        for (UserCountDto user : users) {
            boolean hasTeam = user.getTeam() != null;
            if (user.getMajor()) {
                if (hasTeam) matched_major++;
                else unmatched_major++;
            } else {
                if (hasTeam) matched_unmajor++;
                else unmatched_unmajor++;
            }
        }


        int matched_back = 0, matched_front = 0, matched_ai = 0, matched_design=0, matched_pm=0;
        int unmatched_back = 0, unmatched_front = 0, unmatched_ai = 0, unmatched_design=0, unmatched_pm=0;

        for (UserCountDto user : users) {
            boolean hasTeam = user.getTeam() != null;
            for(PositionEnum position : user.getPosition()) {
                switch (position) {
                    case BACKEND:
                        if (hasTeam) matched_back++;
                        else unmatched_back++;
                        break;
                    case FRONTEND:
                        if (hasTeam) matched_front++;
                        else unmatched_front++;
                        break;
                    case AI:
                        if (hasTeam) matched_ai++;
                        else unmatched_ai++;
                        break;
                    case DESIGN:
                        if (hasTeam) matched_design++;
                        else unmatched_design++;
                        break;
                    case PM:
                        if (hasTeam) matched_pm++;
                        else unmatched_pm++;
                        break;
                }
            }
        }

        Map<String, Long> map = new HashMap<>();

        map=teamRepository.countDomain().stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
        ));

        return DashboardResponseDto.builder()
                .whole(whole)
                .matchedMajor(matched_major)
                .mathcedUnmajor(matched_unmajor)
                .unmatchedMajor(unmatched_major)
                .unmatchedUnmajor(unmatched_unmajor)
                .matched_back(matched_back)
                .matched_front(matched_front)
                .matched_ai(matched_ai)
                .matched_design(matched_design)
                .matched_pm(matched_pm)
                .unmatched_back(unmatched_back)
                .unmatched_front(unmatched_front)
                .unmatched_ai(unmatched_ai)
                .unmatched_design(unmatched_design)
                .unmatched_pm(unmatched_pm)
                .domain(map)
                .build();
    }
}
