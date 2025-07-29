package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.*;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ChatMemberService chatMemberService;

    @Transactional
    public ChatRoomResponse createPrivateChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.PRIVATE);

        User user1 = userRepository.findById(chatRoomRequest.getUser1Id())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        User user2 = userRepository.findById(chatRoomRequest.getUser2Id())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        chatRoom.addMember(chatMemberService.createChatRoomMember(user1, chatRoom));
        chatRoom.addMember(chatMemberService.createChatRoomMember(user2, chatRoom));

        ChatRoom saved = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(saved.getId(), saved.getRoomType(), null);
    }

    @Transactional
    public ChatRoomResponse createTeamChatRoom(ChatRoomRequest chatRoomRequest) {
        Team team = teamRepository.findById(chatRoomRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.TEAM);
        chatRoom.setTeam(team);

        User creator = userRepository.getReferenceById(chatRoomRequest.getUserId());

        chatRoom.addMember(chatMemberService.createChatRoomMember(creator, chatRoom));

        ChatRoom saved = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(saved.getId(), saved.getRoomType(), team.getId());
    }

    @Transactional
    public void addMemberToTeamChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        if (chatRoom.getRoomType() != RoomType.TEAM) {
            throw new IllegalArgumentException("This chat room is not a team chat room.");
        }

        User user = userRepository.getReferenceById(chatRoomRequest.getUserId());

        // 중복 가입 방지
        boolean alreadyMember = chatRoom.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(chatRoomRequest.getUserId()));

        if (alreadyMember) {
            throw new RuntimeException("User is already a member of this chat room.");
        }

        chatRoom.addMember(chatMemberService.createChatRoomMember(user, chatRoom));

        chatRoomRepository.save(chatRoom);  // 변경사항 저장
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        chatRoomRepository.delete(chatRoom);
    }
}
