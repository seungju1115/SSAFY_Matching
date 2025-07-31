package com.example.demo.team.entity;

import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.chat.entity.ChatRoom;
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

    @Column(name = "team_name", nullable = false, length = 20)
    @Size(min = 2, max = 20)
    @NotBlank
    private String teamName;

    @Column(name = "team_domain", nullable = false, length = 50)
    @Size(min = 10, max = 50)
    @NotBlank
    private String teamDomain;

    @Column(name = "member_wanted",  nullable = true, length = 50)
    @Size(max = 50)
    private String memberWanted;

    @Column(name = "team_description", nullable = true, length = 300)
    @Size(max = 300)
    private String teamDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_preference",  nullable = true, length = 50)
    @Size(max = 50)
    private ProjectPrefEnum teamPreference;

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
