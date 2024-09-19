package com.chattest.controller;

import com.chattest.dto.FeedDTO;
import com.chattest.entity.Feed;
import com.chattest.entity.User;
import com.chattest.service.FeedService;
import com.chattest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/feed")
@Log4j2
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    private final UserService userService;
    @GetMapping("/list")
    @ResponseBody
    public List<FeedDTO> getList() {
        log.info("list....");
//         로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        System.out.println("name = " + name);

        List<FeedDTO> list = null;

        if(name != null) {
            list = feedService.getList();
        }
        return list;
    }

    @GetMapping("/listMyPage")
    @ResponseBody
    public List<FeedDTO> getListByName() {
        log.info("listMyPage....");
//         로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        // 이메일을 가져와서 User 객체를 검색
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String name = user.getName();
        System.out.println("name = " + name);


        List<FeedDTO> list = null;

        if(name != null) {
            list = feedService.getListByUsername(name);
        }
        return list;
    }

    @PostMapping("/register")
    @ResponseBody
    public Feed register(@RequestBody FeedDTO feedDTO) {
        log.info("register....");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        // 이메일을 가져와서 User 객체를 검색
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String name = user.getName();
        System.out.println("name = " + name);

        Feed createdFeedDTO = feedService.register(feedDTO, name);


        return createdFeedDTO;
    }

    @PostMapping("/remove")
    @ResponseBody
    public void remove(@RequestBody Map<String, Long> requestBody) {
        log.info("remove....");
        Long fno = requestBody.get("fno");
        log.info("fno: " + fno);

        feedService.remove(fno);
    }

    @PostMapping("/modify")
    @ResponseBody
    public Feed modify(@RequestBody FeedDTO feedDTO) {
        log.info("modify...");
        log.info("feedDTO: " + feedDTO.toString());

        Feed feed = feedService.modify(feedDTO);

        return feed;
    }
}
