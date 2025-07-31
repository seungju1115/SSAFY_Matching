package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMemberService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoomMember createChatRoomMember(User user, ChatRoom chatRoom) {
        ChatRoomMember member = new ChatRoomMember();
        member.setUser(user);
        member.setChatRoom(chatRoom);
        return member;
    }
}
