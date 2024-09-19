package com.chattest.config;

import com.chattest.chat.ChatMessage;
import com.chattest.repository.ChatRoomRepository;
import com.chattest.service.ChatService;
import com.chattest.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;


    // websocket을 통해 들어온 요청이 처리 되기 전에 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결 시 헤더의 JWT 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) { // websocket 연결 요청
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT {}", jwtToken);

            // Header의 jwt token 검증
            jwtTokenProvider.validateToken(jwtToken);
        } else if(StompCommand.SUBSCRIBE == accessor.getCommand()){  // 채팅룸 구독요청
            // header정보에서 구독 destination 정보를 얻고, roomId를 추출한다.
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            //채티방에 들어온 클라이언트 sessionId를 roomId와 매핑해놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");


            chatRoomRepository.setUserEnterInfo(sessionId, roomId);

            // 채팅방 인원수를 +1한다.
            chatRoomRepository.plusUserCount(roomId);
            // 클라이언트 입장 메세지를 채팅방에 발송한다.(redis publish)
            if(!roomId.equals("")) {
                String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
                log.info("SUBSCRIBED {}, {}", name, roomId);
            }
        } else if(StompCommand.DISCONNECT == accessor.getCommand()) { // websocket 연결 종료
            // 연결이 종료된 클라이언트 sessionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
            // 채팅방의 인원수를 -1 한다.
            chatRoomRepository.minusUserCount(roomId);
            // 클라이언트 퇴장 메세지를 채팅방에 발송한다. (redis publish)
            if(roomId != null && !roomId.equals("")){
                String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
                // 퇴장한 클라이언트의 roomId 매핑정보를 삭제한다.
                chatRoomRepository.removeUserEnterInfo(sessionId);
                log.info("DISCONNECTED {}, {}", sessionId, roomId);
            }
        }

        return message;
    }
}
