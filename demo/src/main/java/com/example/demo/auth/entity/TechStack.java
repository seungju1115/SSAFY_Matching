package com.example.demo.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TECH_STACKS")
@Setter
@Getter
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TECH_STACK_ID")
    private Long id;

    private String name; // Java, Python 등

    @ManyToMany(mappedBy = "techStacks")
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TechStack techStack = (TechStack) o;
        return Objects.equals(id, techStack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
