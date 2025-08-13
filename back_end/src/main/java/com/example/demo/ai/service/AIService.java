package com.example.demo.ai.service;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.PersonToTeamDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.ai.dto.TeamToPersonDto;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final RestTemplate restTemplate;

    @Value("${recsys.base-url:http://recsys-server:8000}")
    private String recsysBaseUrl;

    private CandidateDto mapToCandidateDto(Object[] row) {
        Long userId = ((Number) row[0]).longValue();
        String userName = (String) row[1];
        String positionsStr = (String) row[2];
        String goalsStr = (String) row[3];
        String vivesStr = (String) row[4];

        // Position 파싱
        List<String> positions = parsePositions(positionsStr);
        String mainPos = positions.isEmpty() ? null : positions.get(0);
        String subPos = positions.size() > 1 ? positions.get(1) : null;

        return CandidateDto.builder()
                .userId(userId)
                .userName(userName)
                .mainPos(mainPos)
                .subPos(subPos)
                .goals(parseEnums(goalsStr, ProjectGoalEnum.class))
                .vives(parseEnums(vivesStr, ProjectViveEnum.class))
                .build();
    }

    private List<String> parsePositions(String positionsStr) {
        if (positionsStr == null || positionsStr.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(positionsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private <T extends Enum<T>> Set<T> parseEnums(String str, Class<T> enumClass) {
        if (str == null || str.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Enum.valueOf(enumClass, s))
                .collect(Collectors.toSet());
    }

    // ==================== 기존 메서드들 (그대로 유지) ====================
    public TeamToPersonDto findTeamToPersonDtoById(Long teamId){
        TeamToPersonDto teamToPersonDto = new TeamToPersonDto();
        TeamAIDto curTeam = TeamAIDto.from(teamRepository.findTeamAIDtoById(teamId));
        List<Object[]> results = userRepository.findAllCandidates();
        List<CandidateDto> candidateDtos=results.stream()
                .map(this::mapToCandidateDto)
                .toList();
        teamToPersonDto.setCurrentTeam(curTeam);
        teamToPersonDto.setCandidates(candidateDtos);
        return teamToPersonDto;
    }

    public PersonToTeamDto findPersonToTeamDtoById(Long personId){
        PersonToTeamDto personToTeamDto = new PersonToTeamDto();
        CandidateDto curPerson = CandidateDto.from(userRepository.findCurUser(personId));
        List<Team> teams=teamRepository.findAvailableTeams();
        List<TeamAIDto> availableTeams = new ArrayList<>();
        for(Team team:teams){
            availableTeams.add(TeamAIDto.from(team));
        }
        personToTeamDto.setPerson(curPerson);
        personToTeamDto.setTeams(availableTeams);
        return personToTeamDto;
    }

    // ==================== 새로 추가: RecSys 호출 메서드들 ====================

    /**
     * 팀에게 후보자 추천 (RecSys 호출)
     */
    @Cacheable(value = "shortTermCache", key = "'person candidates :' + #teamId")
    public List<CandidateDto> recommendCandidatesForTeam(Long teamId, boolean all) {
        try {
            // 기존 메서드로 데이터 준비
            TeamToPersonDto teamToPersonDto = findTeamToPersonDtoById(teamId);

            log.info("Requesting candidate recommendations for team: {}",
                    teamToPersonDto.getCurrentTeam().getTeamName());

            // RecSys API 호출
            return callRecsysForCandidates(teamToPersonDto,all);

        } catch (Exception e) {
            log.error("Failed to get AI recommendations for team: {}", teamId, e);
            // 폴백: 기존 데이터 그대로 반환
            return findTeamToPersonDtoById(teamId).getCandidates();
        }
    }

    /**
     * 개인에게 팀 추천 (RecSys 호출)
     */
    @Cacheable(value = "shortTermCache", key = "'team candidates:' + #personId")
    public List<TeamAIDto> recommendTeamsForPerson(Long personId, boolean all) {
        try {
            // 기존 메서드로 데이터 준비
            PersonToTeamDto personToTeamDto = findPersonToTeamDtoById(personId);

            log.info("Requesting team recommendations for person: {}",
                    personToTeamDto.getPerson().getUserName());

            // RecSys API 호출
            return callRecsysForTeams(personToTeamDto,all);

        } catch (Exception e) {
            log.error("Failed to get AI recommendations for person: {}", personId, e);
            // 폴백: 기존 데이터 그대로 반환
            return findPersonToTeamDtoById(personId).getTeams();
        }
    }

    // ==================== RecSys 호출 헬퍼 메서드들 ====================

    private List<CandidateDto> callRecsysForCandidates(TeamToPersonDto teamToPersonDto, boolean all) {
        String url = recsysBaseUrl + "/recommend/candidates";
        int top_k=5;
        if(all) top_k=teamToPersonDto.getCandidates().size();
        try {
            // RecSys 요청 형식으로 변환
            Map<String, Object> request = Map.of(
                    "team_info", convertTeamToRecsysFormat(teamToPersonDto.getCurrentTeam()),
                    "member_infos", convertMembersToRecsysFormat(teamToPersonDto.getCurrentTeam().getMembers()),
                    "candidate_pool", convertCandidatesToRecsysFormat(teamToPersonDto.getCandidates()),
                    "alpha", 0.5,
                    "top_k", top_k
            );

            log.info("Sending request to RecSys: {}", request);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");

                log.info("RecSys response: {}", responseBody);

                return mapRecsysResultsToCandidates(results, teamToPersonDto.getCandidates());
            }

            log.warn("Invalid response from RecSys for candidate recommendations");
            return getFallbackCandidates(teamToPersonDto);

        } catch (Exception e) {
            log.error("RecSys API call failed for candidates", e);
            return getFallbackCandidates(teamToPersonDto);
        }
    }

    private List<TeamAIDto> callRecsysForTeams(PersonToTeamDto personToTeamDto, boolean all) {
        String url = recsysBaseUrl + "/recommend/teams";
        int top_k=5;
        if(all) top_k=personToTeamDto.getTeams().size();

        try {
            // RecSys 요청 형식으로 변환
            Map<String, Object> request = Map.of(
                    "person", convertPersonToRecsysFormat(personToTeamDto.getPerson()),
                    "team_pool", convertTeamPoolToRecsysFormat(personToTeamDto.getTeams()),
                    "team_members_map", convertTeamMembersMapToRecsysFormat(personToTeamDto.getTeams()),
                    "alpha", 0.5,
                    "top_k", top_k
            );

            log.info("Sending team request to RecSys: {}", request);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");

                log.info("RecSys team response: {}", responseBody);

                return mapRecsysResultsToTeams(results, personToTeamDto.getTeams());
            }

            log.warn("Invalid response from RecSys for team recommendations");
            return getFallbackTeams(personToTeamDto);

        } catch (Exception e) {
            log.error("RecSys API call failed for teams", e);
            return getFallbackTeams(personToTeamDto);
        }
    }

    // ==================== Spring DTO → RecSys 형식 변환 메서드들 ====================

    private Map<String, Object> convertTeamToRecsysFormat(TeamAIDto teamDto) {
        return Map.of(
                "team_id", teamDto.getTeamId().toString(),
                "team_name", teamDto.getTeamName(),
                "recruit_positions", convertPositionsToRecSysFormat(teamDto.getMemberWanted()),
                "goals", convertGoalEnumsToKorean(teamDto.getGoals()),
                "vibes", convertViveEnumsToKorean(teamDto.getVives())
        );
    }

    private List<Map<String, Object>> convertMembersToRecsysFormat(List<CandidateDto> members) {
        if (members == null) return Collections.emptyList();

        return members.stream()
                .map(member -> Map.of(
                        "user_id", member.getUserId().toString(),
                        "name", member.getUserName(),
                        "main_pos", convertSinglePositionToRecSysFormat(member.getMainPos()),
                        "sub_pos", convertSinglePositionToRecSysFormat(member.getSubPos()),
                        "goals_", convertGoalEnumsToKorean(member.getGoals()),
                        "vibes_", convertViveEnumsToKorean(member.getVives())
                ))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertCandidatesToRecsysFormat(List<CandidateDto> candidates) {
        return candidates.stream()
                .map(candidate -> Map.of(
                        "user_id", candidate.getUserId().toString(),
                        "name", candidate.getUserName(),
                        "main_pos", convertSinglePositionToRecSysFormat(candidate.getMainPos()),
                        "sub_pos", convertSinglePositionToRecSysFormat(candidate.getSubPos()),
                        "goals_", convertGoalEnumsToKorean(candidate.getGoals()),
                        "vibes_", convertViveEnumsToKorean(candidate.getVives())
                ))
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertPersonToRecsysFormat(CandidateDto person) {
        return Map.of(
                "user_id", person.getUserId().toString(),
                "name", person.getUserName(),
                "main_pos", convertSinglePositionToRecSysFormat(person.getMainPos()),
                "sub_pos", convertSinglePositionToRecSysFormat(person.getSubPos()),
                "goals_", convertGoalEnumsToKorean(person.getGoals()),
                "vibes_", convertViveEnumsToKorean(person.getVives())
        );
    }

    private List<Map<String, Object>> convertTeamPoolToRecsysFormat(List<TeamAIDto> teams) {
        return teams.stream()
                .map(team -> Map.of(
                        "team_id", team.getTeamId().toString(),
                        "team_name", team.getTeamName(),
                        "recruit_positions", convertPositionsToRecSysFormat(team.getMemberWanted()),
                        "goals", convertGoalEnumsToKorean(team.getGoals()),
                        "vibes", convertViveEnumsToKorean(team.getVives())
                ))
                .collect(Collectors.toList());
    }

    private List<List<Map<String, Object>>> convertTeamMembersMapToRecsysFormat(List<TeamAIDto> teams) {
        return teams.stream()
                .map(team -> convertMembersToRecsysFormat(team.getMembers()))
                .collect(Collectors.toList());
    }

    // ==================== 데이터 형식 변환 헬퍼 메서드들 ====================

    private String convertSinglePositionToRecSysFormat(String position) {
        if (position == null) return "";

        // PositionEnum → RecSys 형식 변환
        Map<String, String> positionMap = Map.of(
                "PM", "pm",
                "BACKEND", "backend",
                "FRONTEND", "frontend",
                "DESIGN", "design",
                "DESIGNER", "design",
                "AI", "ai"
        );

        return positionMap.getOrDefault(position.toUpperCase(), position.toLowerCase());
    }

    private List<String> convertPositionsToRecSysFormat(List<String> positions) {
        if (positions == null) return Collections.emptyList();

        return positions.stream()
                .map(this::convertSinglePositionToRecSysFormat)
                .collect(Collectors.toList());
    }

    private List<String> convertGoalEnumsToKorean(Set<ProjectGoalEnum> goals) {
        if (goals == null) return Collections.emptyList();

        return goals.stream()
                .map(ProjectGoalEnum::getPref)
                .collect(Collectors.toList());
    }

    private List<String> convertViveEnumsToKorean(Set<ProjectViveEnum> vives) {
        if (vives == null) return Collections.emptyList();

        return vives.stream()
                .map(ProjectViveEnum::getPref)
                .collect(Collectors.toList());
    }

    // ==================== RecSys 결과 → Spring DTO 변환 메서드들 ====================

    private List<CandidateDto> mapRecsysResultsToCandidates(
            List<Map<String, Object>> results, List<CandidateDto> originalCandidates) {

        // user_id로 원본 후보자들을 매핑
        Map<String, CandidateDto> candidateMap = originalCandidates.stream()
                .collect(Collectors.toMap(
                        candidate -> candidate.getUserId().toString(),
                        Function.identity()
                ));

        return results.stream()
                .map(result -> {
                    String candidateId = result.get("user_id").toString();
                    CandidateDto candidate = candidateMap.get(candidateId);

                    if (candidate != null) {
                        log.info("Recommended candidate: {} (similarity: {})",
                                candidate.getUserName(), result.get("similarity"));
                    } else {
                        log.warn("Candidate not found for ID: {}", candidateId);
                    }
                    return candidate;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<TeamAIDto> mapRecsysResultsToTeams(
            List<Map<String, Object>> results, List<TeamAIDto> originalTeams) {

        // team_id로 원본 팀들을 매핑
        Map<String, TeamAIDto> teamMap = originalTeams.stream()
                .collect(Collectors.toMap(
                        team -> team.getTeamId().toString(),
                        Function.identity()
                ));

        return results.stream()
                .map(result -> {
                    String teamId = result.get("team_id").toString();
                    TeamAIDto team = teamMap.get(teamId);

                    if (team != null) {
                        log.info("Recommended team: {} (similarity: {})",
                                team.getTeamName(), result.get("similarity"));
                    } else {
                        log.warn("Team not found for ID: {}", teamId);
                    }
                    return team;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ==================== 폴백 메서드들 ====================

    private List<CandidateDto> getFallbackCandidates(TeamToPersonDto teamToPersonDto) {
        log.info("Using fallback candidate recommendations");

        List<String> wantedPositions = teamToPersonDto.getCurrentTeam().getMemberWanted();

        return teamToPersonDto.getCandidates().stream()
                .filter(candidate ->
                        wantedPositions.contains(candidate.getMainPos()) ||
                                (candidate.getSubPos() != null && wantedPositions.contains(candidate.getSubPos())))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<TeamAIDto> getFallbackTeams(PersonToTeamDto personToTeamDto) {
        log.info("Using fallback team recommendations");

        Set<ProjectGoalEnum> personGoals = personToTeamDto.getPerson().getGoals();

        return personToTeamDto.getTeams().stream()
                .filter(team -> !Collections.disjoint(team.getGoals(), personGoals))
                .limit(3)
                .collect(Collectors.toList());
    }
}