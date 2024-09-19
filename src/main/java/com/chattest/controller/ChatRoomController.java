package com.chattest.controller;

import com.chattest.dto.ChatRoom;
import com.chattest.dto.FriendDTO;
import com.chattest.entity.User;
import com.chattest.login.LoginInfo;
import com.chattest.repository.ChatRoomRepository;
import com.chattest.service.JwtTokenProvider;
import com.chattest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @GetMapping("/user")
    @ResponseBody
    public LoginInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 이메일을 가져와서 User 객체를 검색
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String name = user.getName();
        System.out.println("name = " + name);

        return LoginInfo.builder().email(email).name(name).token(jwtTokenProvider.generateToken(email)).build();
    }

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom();
        chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId())));
        return chatRooms;
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        return chatRoomRepository.createChatRoom(chatRoom);
    }

//    // 채팅방 입장 화면
//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) {
//        model.addAttribute("roomId", roomId);
//        return "/chat/roomdetail";
//    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<ChatRoom> roomInfo(@PathVariable String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findRoomById(roomId);

        System.out.println("chatRoom = " + chatRoom.getParticipant());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = ((UserDetails) auth.getPrincipal()).getUsername();

        // 방의 참여자 확인
        if (chatRoom.getName().contains("[일대일]")) {
            if (chatRoom.getParticipant() != null &&
                    !chatRoom.getParticipant().contains(currentEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 접근 거부
            }
        }

        return ResponseEntity.ok(chatRoom);
    }


    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable String roomId) {
        chatRoomRepository.deleteRoom(roomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }

    // 일대일 대화방 개설
    @PostMapping("/private-room")
    @ResponseBody
    public ChatRoom createPrivateRoom(@RequestBody FriendDTO friendDTO) {
        String friendEmail = friendDTO.getEmail();
        System.out.println("friendEmail = " + friendEmail);
        // 현재 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = ((UserDetails) auth.getPrincipal()).getUsername();
        System.out.println("currentUserEmail = " + currentUserEmail);
        // 채팅방 이름을 두 사용자의 이메일로 조합
        User user = userService.findByEmail(currentUserEmail);

        String roomName = "[일대일] " + user.getName() + "과 " + friendDTO.getName() + "의 대화방";
        System.out.println("roomName = " + roomName);

        // 이미 존재하는 방이 있는지 확인
        ChatRoom existingRoom = chatRoomRepository.findRoomById(roomName);
        if(existingRoom != null) {
            return existingRoom;
        }

        // 새로운 채팅방 생성
        ChatRoom newRoom = new ChatRoom();
        newRoom.setName(roomName);
        newRoom.setParticipant(Arrays.asList(currentUserEmail, friendEmail));
        System.out.println("newRoom = " + newRoom.getParticipant());
        return chatRoomRepository.createChatRoom(newRoom);
    }
}
