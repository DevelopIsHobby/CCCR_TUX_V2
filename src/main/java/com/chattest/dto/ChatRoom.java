package com.chattest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;
    private String roomId;  // 방번호
    private String name;  // 방이름
    private long userCount;
    private List<String> participant; // 참여자 목록 추가

    public static ChatRoom create(String name, List<String> participant) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.setParticipant(participant);
        return chatRoom;
    }

}