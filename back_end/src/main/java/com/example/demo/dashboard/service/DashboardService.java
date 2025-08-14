package com.example.demo.dashboard.service;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.dto.TeamDomainCountDto;
import com.example.demo.dashboard.dto.TechStackCountDto;
import com.example.demo.dashboard.dto.UserCountDto;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private UserCountDto mapToUserCountDto(Object[] row) {
        Long teamId = Optional.ofNullable(row[0])
                .map(obj -> ((Number) obj).longValue())
                .orElse(null);
        boolean major = (boolean) row[1];
        String positionsStr = (String) row[2];
        // Position 파싱
        List<PositionEnum> positions = parseEnums(positionsStr, PositionEnum.class);

        return UserCountDto.builder()
                .teamId(teamId)
                .major(major)
                .position(positions)
                .build();
    }

    private <T extends Enum<T>> List<T> parseEnums(String str, Class<T> enumClass) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Enum.valueOf(enumClass, s))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "shortTermCache")
    public DashboardResponseDto getDashboard() {

        DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();

        List<UserCountDto> users = userRepository.CountUsers().stream().map(this::mapToUserCountDto).collect(Collectors.toList());
        int whole = users.size();
        int matched_major = 0;
        int matched_unmajor = 0;
        int unmatched_major = 0;
        int unmatched_unmajor = 0;

        for (UserCountDto user : users) {
            boolean hasTeam = user.getTeamId() != null;
            if (user.getMajor()) {
                if (hasTeam) matched_major++;
                else unmatched_major++;
            } else {
                if (hasTeam) matched_unmajor++;
                else unmatched_unmajor++;
            }
        }


        int back_main = 0, front_main = 0, ai_main = 0, design_main = 0, pm_main = 0;
        int back_sub = 0, front_sub = 0, ai_sub = 0, design_sub = 0, pm_sub = 0;

        for (UserCountDto user : users) {
            if (user.getPosition().size() > 0) {
                PositionEnum position = user.getPosition().get(0);
                switch (position) {
                    case BACKEND:
                        back_main++;
                        break;
                    case FRONTEND:
                        front_main++;
                        break;
                    case AI:
                        ai_main++;
                        break;
                    case DESIGN:
                        design_main++;
                        break;
                    case PM:
                        pm_main++;
                        break;
                }
            }
            if (user.getPosition().size() > 1) {
                PositionEnum position = user.getPosition().get(1);
            switch (position) {
                case BACKEND:
                    back_sub++;
                    break;
                case FRONTEND:
                    front_sub++;
                    break;
                case AI:
                    ai_sub++;
                    break;
                case DESIGN:
                    design_sub++;
                    break;
                case PM:
                    pm_sub++;
                    break;
            }
        }
        }

            Map<String, Long> map = new HashMap<>();

            map = teamRepository.countDomain().stream().collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> (Long) row[1]
            ));

            List<TeamDomainCountDto> domains = teamRepository.countDomainPositions();
            Map<String, List<Long>> domainMap = new HashMap<>();
            for (TeamDomainCountDto domain : domains) {
                List<Long> pos = new ArrayList<>();
                pos.add(domain.getBack());
                pos.add(domain.getFront());
                pos.add(domain.getAi());
                pos.add(domain.getDesign());
                pos.add(domain.getPm());
                domainMap.put(domain.getDomain(), pos);
            }

            List<TechStackCountDto> techStacks = userRepository.countTechStack();
            Map<String, Long> techStackMap = new HashMap<>();
            for (TechStackCountDto techStack : techStacks) {
                techStackMap.put(techStack.getTechStack().name(), techStack.getCount());
            }

            return DashboardResponseDto.builder()
                    .whole(whole)
                    .matchedMajor(matched_major)
                    .mathcedUnmajor(matched_unmajor)
                    .unmatchedMajor(unmatched_major)
                    .unmatchedUnmajor(unmatched_unmajor)
                    .back_main(back_main)
                    .front_main(front_main)
                    .ai_main(ai_main)
                    .design_main(design_main)
                    .pm_main(pm_main)
                    .back_sub(back_sub)
                    .ai_sub(ai_sub)
                    .design_sub(design_sub)
                    .pm_sub(pm_sub)
                    .front_sub(front_sub)
                    .domain(map)
                    .domainPos(domainMap)
                    .techstacks(techStackMap)
                    .build();
    }
}
