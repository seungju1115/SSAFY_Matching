package com.example.demo.team.entity;

import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = true, length = 20)
    @Size(min = 2, max = 20)
//    @NotBlank 처음에 팀 생성 시 팀 이름이 없기 때문에 주석처리
    private String teamName;

    // 각 역할별 필요 인원 수, 만약 역할이 추가된다면 별도 엔티티로 작성해야 함.
    @Column(columnDefinition = "int default 0")
    private int backendCount;
    @Column(columnDefinition = "int default 0")
    private int frontendCount;
    @Column(columnDefinition = "int default 0")
    private int aiCount;
    @Column(columnDefinition = "int default 0")
    private int pmCount;
    @Column(columnDefinition = "int default 0")
    private int designCount;

    @Column(name = "team_domain", nullable = false, length = 50)
    @Size(min = 2, max = 50)
    @NotBlank
    private String teamDomain;

    @Column(name = "member_wanted",  nullable = true, length = 50)
    @Size(max = 50)
    private String memberWanted;

    @Column(name = "team_description", nullable = true, length = 300)
    @Size(max = 300)
    private String teamDescription;

    @ElementCollection(targetClass = ProjectGoalEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "team_preference",  nullable = true)
    private Set<ProjectGoalEnum> teamPreference;

    @ElementCollection(targetClass = ProjectViveEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "team_vive",  nullable = true)
    private Set<ProjectViveEnum> teamVive;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    // ✅ 팀장 (1:1 관계)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id") // 외래키: team.leader_id → users.id
    private User leader;

    public void setChatRoom(ChatRoom chatRoom) { //양방향 동기화 메서드
        this.chatRoom = chatRoom;
        if (chatRoom != null && chatRoom.getTeam() != this) {
            chatRoom.setTeam(this);
        }
    }

    // ✅ 팀 멤버들 (1:N 관계)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMembershipRequest> membershipRequests = new ArrayList<>();

}
