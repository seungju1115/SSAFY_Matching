package com.example.demo.team.entity;

import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
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
    private Long id;

    private String name;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    public void setChatRoom(ChatRoom chatRoom) { //양방향 동기화 메서드
        this.chatRoom = chatRoom;
        if (chatRoom != null && chatRoom.getTeam() != this) {
            chatRoom.setTeam(this);
        }
    }

    // ✅ 팀장 (1:1 관계)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id") // 외래키: team.leader_id → users.id
    private User leader;

    // ✅ 팀 멤버들 (1:N 관계)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

}
