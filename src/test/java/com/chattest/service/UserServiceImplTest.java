//package com.chattest.service;
//
//import com.chattest.entity.User;
//import com.chattest.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Set;
//
//@SpringBootTest
//class UserServiceImplTest {
//    @Autowired
//    private UserService userService;
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setUp() {
//        // 테스트를 위한 사용자 데이터 설정
//        User user1 = User.builder()
//                .email("user1@example.com")
//                .password("password1")
//                .name("User1")
//                .build();
//
//        User user2 = User.builder()
//                .email("user2@example.com")
//                .password("password2")
//                .name("User2")
//                .build();
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//    }
//
//    @Test
//    public void testAddFriend() {
//        // user1이  user2를 친구로 추가
//        User user1 = userService.addFriend("user1@example.com", "user2@example.com");
//
//        // user1의 친구목록이 user2를 포함하는지 확인
//        Set<User> friends = user1.getFriends();
//        System.out.println("friends = " + friends);
//    }
//
//    @Test
//    public void findByEmailTest() {
//        User user = userService.findByEmail("user1@com.chattest");
//        System.out.println("user = " + user);
//    }
//
//    @Test
//    public void findFriendsListTest() {
//        // user1이  user2를 친구로 추가
//        User user1 = userService.addFriend("user1@example.com", "user2@example.com");
//
//        List<User> friendsList = userService.getFriends("user1@example.com");
//        System.out.println("friendsList = " + friendsList);
//    }
//}