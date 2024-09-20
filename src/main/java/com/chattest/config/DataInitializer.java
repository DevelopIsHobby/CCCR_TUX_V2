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
                .profilePictureUrl("2024/09/20/eb3e6354-4523-4326-adc6-7c297392cb84_관리자님.jpg")
                .build();
        User user1 = User.builder()
                .email("user1@com.chattest")
                .name("김민석")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("2024/09/20/ba01307c-6d31-4e13-9938-0366a5eff353_카카오톡_프사.jpg")
                .build();
        User user2 = User.builder()
                .email("user2@com.chattest")
                .name("최민정")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("2024/09/20/3737676e-4893-4167-acf0-959c35d5b126_민정님.jpg")
                .build();
        User user3 = User.builder()
                .email("user3@com.chattest")
                .name("윤남석")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("2024/09/20/a9f43c1c-14ac-4bcb-b954-25e751a0c339_남석진우뉨.jpg")
                .build();
        User user4 = User.builder()
                .email("user4@com.chattest")
                .name("이진우")
                .password(passwordEncoder.encode("1234"))
                .profilePictureUrl("2024/09/20/1addad11-79e4-4ad9-80da-5dc509fb6497_남석진우뉨.jpg")
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
