package com.chattest.dto;

import com.chattest.chat.UserRole;
import com.chattest.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;


@Log4j2
@Getter
@Setter
@ToString
@Builder
public class UserDTO {
    private String email;
    private String name;
    private String profilePictureUrl;

    private Set<UserRole> roles; // 권한
    private Set<User> friends; // 친구 목록 이메일
}
