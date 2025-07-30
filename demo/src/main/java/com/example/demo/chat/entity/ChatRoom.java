package com.example.demo.chat.entity;

import com.example.demo.team.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @OneToOne
    @JoinColumn(name = "team_id",unique = true)
    private Team team; // PRIVATE이면 NULL

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatRoomMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatMessage> messages = new ArrayList<>();

    public void setTeam(Team team) {
        this.team = team;
        if (team != null && team.getChatRoom() != this) {
            team.setChatRoom(this);
        }
    }

    public void addMember(ChatRoomMember member) {
        members.add(member);
        member.setChatRoom(this);
    }

    public void removeMember(ChatRoomMember member) {
        members.remove(member);
        member.setChatRoom(null);
    }
}

