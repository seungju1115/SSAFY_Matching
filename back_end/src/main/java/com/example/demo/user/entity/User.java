package com.example.demo.user.entity;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS"
        , indexes= {
        @Index(name = "user_major_idx", columnList = "major"),
        @Index(name = "user_position_idx", columnList = "wanted_position"),
        @Index(name = "user_team_idx", columnList = "team_id")
        }
        )
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

    @Column(name = "major",  nullable = false)
    private boolean major=false;

    @Column(name = "last_class", nullable = false)
    @NotNull
    private Integer lastClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "wanted_position",  nullable = false)
    @NotNull
    private PositionEnum wantedPosition;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TeamMembershipRequest> membershipRequests = new ArrayList<>();

    @Column(name = "project_experience", nullable = true, length = 300)
    @Size(max = 300)
    private String projectExp;

    @Column(name="qualification", nullable = true, length = 45)
    @Size(max = 45)
    private String qualification;

    @ElementCollection(targetClass = TechEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name="tech_stack",  nullable = true, length = 100)
    @NotEmpty
    @Size(max = 100)
    private Set<TechEnum> techStack;

    @ElementCollection(targetClass = ProjectPrefEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "project_preference", nullable = false, length = 50)
    @NotEmpty
    @Size(max = 50)
    private Set<ProjectPrefEnum> projectPref;

    @ElementCollection(targetClass = PersonalPrefEnum.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "personal_preference", nullable = false, length = 50)
    @NotEmpty
    @Size(max = 50)
    private Set<PersonalPrefEnum> personalPref;

    //@ManyToMany
    //@JoinTable(
    //       name = "USER_TECH_STACK", // 중간 테이블 이름
    //        joinColumns = @JoinColumn(name = "USER_ID"),       // 현재 Entity(User)의 FK
    //        inverseJoinColumns = @JoinColumn(name = "TECH_STACK_ID") // 연결될 Entity(TechStack)의 FK
    //)
    //private Set<TechStack> techStacks = new HashSet<>();

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        if (team != null && !team.getMembers().contains(this)) {
            team.getMembers().add(this);
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
