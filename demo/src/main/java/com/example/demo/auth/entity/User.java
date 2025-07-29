package com.example.demo.auth.entity;

import com.example.demo.auth.Enum.PersonalPrefEnum;
import com.example.demo.auth.Enum.ProjectPrefEnum;
import com.example.demo.auth.Enum.TechEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Column(name = "user_id")
    private Long id;

    @Column(name="user_name", length = 10, nullable = false)
    @NotBlank
    @Size(max = 10)
    private String userName;

    @Column(name="role", length = 10, nullable = false)
    @NotBlank
    @Size(max = 10)
    private String role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name= "user_email",unique = true, nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String email;

    @Column(name="user_profile", length = 550)
    @Size(max = 550)
    private String userProfile;

    //private String projectStep;

    @Column(name = "major",  nullable = false, length = 5)
    @Size(max = 5)
    @NotBlank
    private String major;

    @Column(name = "last_class", nullable = false)
    @NotBlank
    private Integer lastClass;

    @Column(name = "wanted_postion",  nullable = false,  length = 50)
    @NotBlank
    @Size(max = 50)
    private String wantedPosition;

    @ElementCollection(targetClass = ProjectPrefEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "project_preference", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private Set<ProjectPrefEnum> projectPref;

    @ElementCollection(targetClass = PersonalPrefEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "personal_preference", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private Set<PersonalPrefEnum> personalPref;

    @Column(name = "project_experience", nullable = true, length = 300)
    @Size(max = 300)
    private String projectExp;

    @Column(name="qualification", nullable = true, length = 45)
    @Size(max = 45)
    private String qualification;

    @ElementCollection(targetClass = TechEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name="tech_stack",  nullable = true, length = 100)
    @Size(max = 100)
    private Set<TechEnum> techStack;

    //@ManyToMany
    //@JoinTable(
    //       name = "USER_TECH_STACK", // 중간 테이블 이름
    //        joinColumns = @JoinColumn(name = "USER_ID"),       // 현재 Entity(User)의 FK
    //        inverseJoinColumns = @JoinColumn(name = "TECH_STACK_ID") // 연결될 Entity(TechStack)의 FK
    //)
    //private Set<TechStack> techStacks = new HashSet<>();

    public void setTeam(Team team) {
        this.team = team;
        if (!team.getUsers().contains(this)) {
            team.getUsers().add(this);
       }
    }

    //public void addTechStacks(TechStack techStack) {
    //    techStacks.add(techStack);
    //}

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
