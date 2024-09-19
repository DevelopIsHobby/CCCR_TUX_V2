package com.chattest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDTO {
    private String email;
    private String password;
    private String name;
}
