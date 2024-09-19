package com.chattest.chat;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatMessage {
    // 메세지에 대한 객체
    // 메세지 타입, 방번호, 송신자, 메세지 내용
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private MessageType type; // 메세지 타입
    private String roomId; // 방번호
    private String sender; // 메세지 보낸 사람
    private String message; // 메세지
    private String name;
    private String profilePictureUrl;

    private long userCount; // 채팅방 인원수, 채팅방 내에서 메세지가 전달될 때 인원수 갱신시 사용

    public ChatMessage() {

    }

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount, String name, String profilePictureUrl){
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }
}