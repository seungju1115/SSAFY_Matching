package com.example.demo.dashboard.service;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.dto.UserCountDto;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.user.dao.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "shortTermCache")
    public DashboardResponseDto getDashboard() {
        DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();

        List<UserCountDto> users=userRepository.CountUsers();
        int whole= users.size();
        int match_major=(int) users.stream().filter(u-> u.isMajor())
                .filter(u->u.getTeam()!=null)
                .count();
        int match_unmajor=(int) users.stream().filter(u-> !u.isMajor())
                .filter(u->u.getTeam()!=null)
                .count();
        int unmatch_major=(int) users.stream().filter(u-> u.isMajor())
                .filter(u->u.getTeam()==null)
                .count();
        int unmatch_unmajor=(int) users.stream().filter(u-> !u.isMajor())
                .filter(u->u.getTeam()==null)
                .count();
        return DashboardResponseDto.builder()
                .whole(whole)
                .matchedMajor(match_major)
                .mathcedUnmajor(match_unmajor)
                .unmatchedMajor(unmatch_major)
                .unmatchedUnmajor(unmatch_unmajor)
                .build();
    }
}
