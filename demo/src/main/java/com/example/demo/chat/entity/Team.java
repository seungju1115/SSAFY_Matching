package com.example.demo.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
