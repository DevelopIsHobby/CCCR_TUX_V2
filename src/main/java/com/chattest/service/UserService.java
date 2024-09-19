package com.chattest.service;

import com.chattest.dto.UserSignupDTO;
import com.chattest.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User addFriend(String userEmail, String friendEmail);

    List<User> getFriends(String userEmail);

    void deleteFriend(String userEmail, String friendEmail);

    void registerUser(UserSignupDTO userSignupDTO);

    User findByEmail(String email);

    void updateUser(String email, String name, String profilePictureUrl);

    String getProfilePictureUrl(String email);
}
