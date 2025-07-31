package com.example.demo.user.entity;

import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // 외래키: users.team_id → team.id
    private Team team;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TeamMembershipRequest> membershipRequests = new ArrayList<>();

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        if (team != null && !team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }
}

