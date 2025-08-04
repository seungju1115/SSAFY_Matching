package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatMessageRepository;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.entity.ChatMessage;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageResponse saveMessage(ChatMessageRequest dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setMessage(dto.getMessage());

        ChatMessage saved = chatMessageRepository.save(message);

        return new ChatMessageResponse(
                saved.getId(),
                saved.getChatRoom().getId(),
                saved.getSender().getId(),
                saved.getMessage(),
                saved.getCreatedAt()
        );
    }

    public List<ChatMessageResponse> getAllMessagesByChatRoom(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(msg -> new ChatMessageResponse(
                        msg.getId(),
                        msg.getChatRoom().getId(),
                        msg.getSender().getId(),
                        msg.getMessage(),
                        msg.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
