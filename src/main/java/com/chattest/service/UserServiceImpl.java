package com.chattest.service;

import com.chattest.chat.UserRole;
import com.chattest.dto.UserSignupDTO;
import com.chattest.entity.Public;
import com.chattest.entity.User;
import com.chattest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addFriend(String userEmail, String friendEmail) {
        Optional<User> userOpt = userRepository.findById(userEmail);
        Optional<User> friendOpt = userRepository.findById(friendEmail);

        if(userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            user.getFriends().add(friend);
            userRepository.save(user);

            return user;
        }else {
            throw new RuntimeException("User or Friend not found");
        }
    }

    @Override
    public List<User> getFriends(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new ArrayList<>(user.getFriends()); // 친구 목록 반환
    }

    @Override
    @Transactional
    public void deleteFriend(String userEmail, String friendEmail) {
        User user = userRepository.findById(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        User friend = userRepository.findById(friendEmail)
                .orElseThrow(() -> new RuntimeException("친구를 찾을 수 없습니다."));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }


    @Override
    @Transactional
    public void registerUser(UserSignupDTO userSignupDTO) {
        log.info("UserServiceImpl registerUser.....");

        if(userRepository.existsById(userSignupDTO.getEmail())) {
            throw new IllegalArgumentException("User with this Email already exists!");
        }

        User user = User.builder()
                .email(userSignupDTO.getEmail())
                .password(passwordEncoder.encode(userSignupDTO.getPassword()))
                .name(userSignupDTO.getName())
                .build();
        user.addUserRole(UserRole.USER);

        log.info(user.toString());
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateUser(String email, String name, String profilePictureUrl) {
        log.info("UserServiceImpl updateUser...");
        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(name);
        user.setProfilePictureUrl(profilePictureUrl);
        log.info("updated User: {}", user);
        userRepository.save(user);
    }

    @Override
    public String getProfilePictureUrl(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfilePictureUrl();
    }


}