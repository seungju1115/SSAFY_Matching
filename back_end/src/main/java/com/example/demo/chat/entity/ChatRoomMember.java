package com.example.demo.chat.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room_member")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
//    이부분은 user를 변경할 일이 없으므로 사용하지 않는다.
    public void setUser(User user) {
        if (this.user != null) {
            this.user.getChatRoomMembers().remove(this);
        }
        this.user = user;
        if (user != null && !user.getChatRoomMembers().contains(this)) {
            user.getChatRoomMembers().add(this);
        }
    }

    public void setChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null) {
            this.chatRoom.getMembers().remove(this);
        }
        this.chatRoom = chatRoom;
        if (chatRoom != null && !chatRoom.getMembers().contains(this)) {
            chatRoom.getMembers().add(this);
        }
    }
}
