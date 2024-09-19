//package com.chattest.handler;
//
//import com.chattest.chat.ChatMessage;
//import com.chattest.dto.ChatRoom;
//import com.chattest.service.ChatService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//@Log4j2
//@RequiredArgsConstructor
//// 소켓 통신은 서버와 클라이언트 간 1:N 관계를 형성한다.
//// 서버에서는 다양한 클라이언트로부터 오는 메세지를 처리하기 위한 Handler가 필요하다.
//// TextWebSocketHandler를 상속받아 handleTextMessage 메서드를 오버라이딩한다.
//public class WebSockChatHandler extends TextWebSocketHandler {
//    private final ObjectMapper objectMapper;
//    private final ChatService chatService;
//
//    @Override
//    // 클라이언트의 웹 소켓 세션과 문자열로 된 메세지를 매개변수로 받는다.
//    // 내부에서는 메세지의 내용(Payload)를 log로 찍어보며, Welcome이라는 메세지를 세션값으로 활용하여 다시 되돌려준다.
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("payload {}", payload);
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
//        log.info(chatMessage);
//        log.info(room);
//        room.handleActions(session, chatMessage, chatService);
////        TextMessage textMessage = new TextMessage("Welcome");
////        session.sendMessage(textMessage);
//    }
//
//}
