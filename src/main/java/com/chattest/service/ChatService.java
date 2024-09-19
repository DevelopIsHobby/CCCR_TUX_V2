package com.chattest.service;

import com.chattest.chat.ChatMessage;
import com.chattest.entity.User;
import com.chattest.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    // Destination정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if(lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    // 채팅방에 메세지 발송
    public void sendChatMessage(ChatMessage chatMessage) {
        System.out.println("chatMessage = " + chatMessage);
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        User user = userService.findByEmail(chatMessage.getSender());
        String name = user.getName();
        System.out.println("name = " + name);

        String profilePictureUrl = user.getProfilePictureUrl();

        chatMessage.setProfilePictureUrl(profilePictureUrl);

        if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(name + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");

        } else if(ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(name + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        System.out.println("chatMessage = " + chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
