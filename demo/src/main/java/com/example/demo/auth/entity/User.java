package com.example.demo.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(unique = true)
    private String email;
    private String role;

    public void setTeam(Team team) {
        this.team = team;
        if (!team.getUsers().contains(this)){
            team.getUsers().add(this);
        }
    }
}
