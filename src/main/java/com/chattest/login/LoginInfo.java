package com.chattest.login;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginInfo {
    private String email;
    private String name;
    private String token;

    @Builder
    public LoginInfo(String email, String name, String token) {
        this.email = email;
        this.name = name;
        this.token = token;
    }
}
