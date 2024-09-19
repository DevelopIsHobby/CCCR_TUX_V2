package com.chattest.controller;

import com.chattest.dto.FriendDTO;
import com.chattest.dto.LoginRequest;
import com.chattest.dto.UserDTO;
import com.chattest.dto.UserSignupDTO;
import com.chattest.entity.User;
import com.chattest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Value("${com.chattest.upload.path}")
    private String uploadPath;

    @Value("${com.chattest.upload.url}") // URL을 반환하기 위한 프로퍼티
    private String uploadUrl;

    @PostMapping("/{userEmail}/friends/{friendEmail}")
    public User addFriend(@PathVariable String userEmail, @PathVariable String friendEmail) {
        return userService.addFriend(userEmail, friendEmail);
    }

    @GetMapping("/{userEmail}/friends")
    public List<User> getFriends(@PathVariable String userEmail) {
        return userService.getFriends(userEmail);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDTO>> getFriendsList(Authentication authentication){
        String userEmail = authentication.getName();
        List<User> friends = userService.getFriends(userEmail); // 이메일을 통해 친구목록을 가져옴

        // User 엔티티에서 필요한 정보만 담은 DTO로 변환
        List<FriendDTO> friendDTOList = friends.stream()
                .map(friend -> FriendDTO.builder().email(friend.getEmail()).name(friend.getName()).profilePictureURl(friend.getProfilePictureUrl()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(friendDTOList);
    }

    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestBody Map<String, String> request, Authentication authentication) {
        String userEmail = authentication.getName();
        String friendEmail = request.get("email");

        userService.addFriend(userEmail, friendEmail); // 친구 추가 로직
        return ResponseEntity.ok("친구가 추가되었습니다.");
    }

    @PostMapping("/deleteFriend")
    public ResponseEntity<String> deleteFriend(@RequestBody Map<String, String> request, Authentication authentication) {
        String userEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일을 가져옴
        try {
            System.out.println("request = " + request);
            userService.deleteFriend(userEmail, request.get("friendEmail"));
            return ResponseEntity.ok("친구 삭제 성공");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 삭제 중 오류");
        }
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody UserSignupDTO userSignupDTO) {
        log.info("signup UserController");
        log.info(userSignupDTO.toString());
        try {
            userService.registerUser(userSignupDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAuth(Authentication authentication) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser() {
        User user = getCurrentUser();
        System.out.println("user = " + user);
        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .roles(user.getRoleSet())
                .friends(user.getFriends())
                .build();
        return ResponseEntity.ok(userDTO);
    }



    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userService.findByEmail(email); // UserService에 이메일로 사용자 검색 메서드 필요
        }
        throw new RuntimeException("User not authenticated");
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<String> getProfilePictureUrl(Principal principal) {
        String email = principal.getName(); // 현재 로그인한 사용자의 이메일을 가져옵니다
        String profilePictureUrl = userService.getProfilePictureUrl(email); // 서비스에서 URL을 가져옵니다
        return ResponseEntity.ok(profilePictureUrl);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestParam String name, @RequestParam String  email,
                                             @RequestParam(value="profilePicture", required = false) MultipartFile profilePicture) {
        System.out.println("/update");
        System.out.println("profilePicture = " + profilePicture);
        // 사용자 정보 업데이트 처리
        User user = getCurrentUser();
        user.setName(name);
        user.setEmail(email);
        if (profilePicture != null  && !profilePicture.isEmpty()) {
            // 프로필 사진 저장 로직
            String profilePictureUrl = saveProfilePicture(profilePicture);
            user.setProfilePictureUrl(profilePictureUrl);
            System.out.println("profilePictureUrl = " + profilePictureUrl);
            System.out.println("user = " + user);
        }
        userService.updateUser(user.getEmail(), user.getName(), user.getProfilePictureUrl());
        return ResponseEntity.ok("Profile updated successfully");
    }


    private String saveProfilePicture(MultipartFile file) {
        // 이미지 파일만 업로드 가능
        if (file == null || file.isEmpty() || !file.getContentType().startsWith("image")) {
            log.warn("This file is not an image type or is empty");
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // 파일 저장 로직 예: 서버의 특정 디렉토리에 저장
        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

        log.info("fileName : " + fileName);
        // 날짜 폴더 생성
        String folderPath = makeFolder();

        // UUID
        String uuid = UUID.randomUUID().toString();

        // 저장할 파일 이름 중간에 "_"를 이용해서 구분
        String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(saveName);

        try {
            // 파일 저장
            file.transferTo(savePath);
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file", e);
        }
        // URL 변환 (URL 인코딩 처리)

        String uploadFolderPath = folderPath + File.separator + uuid + "_" + fileName;
        System.out.println("uploadFolderPath = " + uploadFolderPath);
        return uploadFolderPath;
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath).getAbsoluteFile();
        File targetFolder = new File(uploadPathFolder, folderPath);

        if (!targetFolder.exists()) {
            boolean dirsCreated = targetFolder.mkdirs();
            if (!dirsCreated) {
                log.error("Failed to create directories: " + targetFolder.getAbsolutePath());
                throw new RuntimeException("Failed to create directories");
            }
        }
        return folderPath;
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<Map<String,String>> uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file) {
        String profilePictureUrl = saveProfilePicture(file); // 파일 저장 및 URL 생성
        System.out.println("/upload-profile-picture");
        System.out.println("profilePictureUrl = " + profilePictureUrl);
        Map<String, String> response = new HashMap<>();
        response.put("url", profilePictureUrl); // 클라이언트의 url 반환
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUserByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return ResponseEntity.ok(email);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user found");
    }

}
