package com.chattest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FriendDTO {
    private String email;
    private String name;
    private String profilePictureURl;
}
