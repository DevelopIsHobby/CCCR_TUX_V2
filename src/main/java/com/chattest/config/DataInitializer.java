package com.chattest.config;

import com.chattest.chat.UserRole;
import com.chattest.entity.User;
import com.chattest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 기본 사용자 생성
        User user = User.builder()
                .email("admin@com.chattest")
                .name("CCCR관리자")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("default/관리자님.jpg")
                .build();
        User user1 = User.builder()
                .email("user1@com.chattest")
                .name("김민석")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("default/카카오톡_프사.jpg")
                .build();
        User user2 = User.builder()
                .email("user2@com.chattest")
                .name("최민정")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("default/민정님.jpg")
                .build();
        User user3 = User.builder()
                .email("user3@com.chattest")
                .name("윤남석")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("default/남석진우님.jpg")
                .build();
        User user4 = User.builder()
                .email("user4@com.chattest")
                .name("이진우")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("default/남석진우님.jpg")
                .build();

        user.addUserRole(UserRole.USER);
        user1.addUserRole(UserRole.USER);
        user2.addUserRole(UserRole.USER);
        user3.addUserRole(UserRole.USER);
        user4.addUserRole(UserRole.USER);
        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
    }
}
