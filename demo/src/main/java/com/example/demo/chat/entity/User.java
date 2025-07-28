package com.example.demo.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    //필요함
    public void addChatRoomMember(ChatRoomMember member) {
        chatRoomMembers.add(member);
        member.setUser(this);
    }

    public void removeChatRoomMember(ChatRoomMember member) {
        chatRoomMembers.remove(member);
        member.setUser(null);
    }
}

