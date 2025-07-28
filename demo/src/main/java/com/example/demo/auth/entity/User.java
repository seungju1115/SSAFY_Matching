package com.example.demo.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(unique = true)
    private String email;
    private String role;

    @ManyToMany
    @JoinTable(
            name = "USER_TECH_STACK", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "USER_ID"),       // 현재 Entity(User)의 FK
            inverseJoinColumns = @JoinColumn(name = "TECH_STACK_ID") // 연결될 Entity(TechStack)의 FK
    )
    private Set<TechStack> techStacks = new HashSet<>();

    public void setTeam(Team team) {
        this.team = team;
        if (!team.getUsers().contains(this)) {
            team.getUsers().add(this);
        }
    }

    public void addTechStacks(TechStack techStack) {
        techStacks.add(techStack);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
