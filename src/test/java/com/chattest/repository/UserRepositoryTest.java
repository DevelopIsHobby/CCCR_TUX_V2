package com.chattest.repository;

import com.chattest.chat.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.chattest.entity.User;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        // 1-8까지는 USER만 지정
        // 9 는 USER, MEMBER
        // 10은 USER, MEMBER, ADMIN

        IntStream.rangeClosed(1,10).forEach(i -> {
            User user = User.builder()
                    .email("user" + i + "@com.chattest")
                    .name("사용자" + i)
                    .password(passwordEncoder.encode("1234"))
                    .build();

            // default role
            user.addUserRole(UserRole.USER);
            if(i > 8) {
                user.addUserRole(UserRole.MEMBER);
            }
            if(i>9) {
                user.addUserRole(UserRole.ADMIN);
            }

            userRepository.save(user);
        });
    }

    @Test
    public void insertAdminUser() {
        User user = User.builder()
                .email("admin@com.chattest")
                .name("CCCR관리자")
                .password(passwordEncoder.encode("1234"))
                .build();

        user.addUserRole(UserRole.USER);
        userRepository.save(user);
    }

    @Test
    public void findByEmailTest() {
        User user = userRepository.findById("user1@com.chattest").orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("user = " + user);
    }
}