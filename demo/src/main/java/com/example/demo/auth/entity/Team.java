package com.example.demo.auth.entity;

import com.example.demo.auth.Enum.ProjectPrefEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<User>();

    public void addUser(User user){
        this.users.add(user);
        if (user.getTeam() != this){
            user.setTeam(this);
        }
    }
}
