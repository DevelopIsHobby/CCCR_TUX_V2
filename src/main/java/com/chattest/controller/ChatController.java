package com.chattest.controller;

import com.chattest.chat.ChatMessage;
import com.chattest.entity.User;
import com.chattest.repository.ChatRoomRepository;
import com.chattest.service.ChatService;
import com.chattest.service.JwtTokenProvider;
import com.chattest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;

    // WebSocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        System.out.println("message = " + message);
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        System.out.println("nickname = " + nickname);

        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 인원수 세팅
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        // WebSocket에 발행된 메세지를 redis로 발행(publish)
        System.out.println("message = " + message);
        chatService.sendChatMessage(message);
    }
}