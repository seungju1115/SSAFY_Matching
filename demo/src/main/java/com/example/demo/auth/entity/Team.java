package com.example.demo.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEAMS")
@Setter
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<User>();

    public void addUser(User user){
        this.users.add(user);
        if (user.getTeam() != this){
            user.setTeam(this);
        }
    }
}
