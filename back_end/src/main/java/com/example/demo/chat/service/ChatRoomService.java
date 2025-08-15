package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.*;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }
        String currentUsername = authentication.getName(); // User's email

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Long currentUserId = currentUser.getId();
        Long user1Id = chatRoomRequest.getUser1Id();
        Long user2Id = chatRoomRequest.getUser2Id();

        // Security Check: The current user must be one of the participants.
        if (!currentUserId.equals(user1Id) && !currentUserId.equals(user2Id)) {
            throw new BusinessException(ErrorCode.USER_FORBIDDEN);
        }

        // Prevent creating a chat with oneself
        if (user1Id.equals(user2Id)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        // chatRoomMembers를 미리 로딩
        User user1 = userRepository.findByIdWithChatRoomMembers(user1Id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User user2 = userRepository.findByIdWithChatRoomMembers(user2Id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // user1이 속한 PRIVATE 채팅방 목록
        Set<ChatRoom> user1PrivateRooms = user1.getChatRoomMembers().stream()
                .map(ChatRoomMember::getChatRoom)
                .filter(room -> room.getRoomType() == RoomType.PRIVATE)
                .collect(Collectors.toSet());

        // user2가 속한 PRIVATE 채팅방 중, user1과 공통된 방이 있는지 확인
        Optional<ChatRoom> existingRoomOpt = user2.getChatRoomMembers().stream()
                .map(ChatRoomMember::getChatRoom)
                .filter(room -> room.getRoomType() == RoomType.PRIVATE)
                .filter(user1PrivateRooms::contains)
                .findFirst();

        ChatRoom chatRoom = existingRoomOpt.orElseGet(() -> {
            ChatRoom newRoom = new ChatRoom();
            newRoom.setRoomType(RoomType.PRIVATE);

            ChatRoom savedRoom = chatRoomRepository.save(newRoom);

            chatMemberService.createChatRoomMember(user1, savedRoom);
            chatMemberService.createChatRoomMember(user2, savedRoom);

            return savedRoom;
        });

        return new ChatRoomResponse(chatRoom.getId(), chatRoom.getRoomType(), null);
    }

    @Transactional
    public void createTeamChatRoom(ChatRoomRequest chatRoomRequest) {
        Team team = teamRepository.findById(chatRoomRequest.getTeamId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        User creator = userRepository.findById(chatRoomRequest.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.TEAM);
        chatRoom.setTeam(team);

        chatRoomRepository.save(chatRoom);
        chatMemberService.createChatRoomMember(creator, chatRoom);
    }

    @Transactional
    public void addMemberToTeamChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomRequest.getRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (chatRoom.getRoomType() != RoomType.TEAM) {
            throw new BusinessException(ErrorCode.INVALID_CHAT_ROOM_TYPE);
        }

        User user = userRepository.findById(chatRoomRequest.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 중복 가입 방지
        boolean alreadyMember = chatRoom.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(chatRoomRequest.getUserId()));

        if (alreadyMember) {
            throw new BusinessException(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS);
        }

        chatMemberService.createChatRoomMember(user, chatRoom);

        chatRoomRepository.save(chatRoom);  // 변경사항 저장
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatRoomRepository.delete(chatRoom);
    }

}