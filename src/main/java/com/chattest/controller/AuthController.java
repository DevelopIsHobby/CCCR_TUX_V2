package com.chattest.controller;

import com.chattest.dto.UserDTO;
import com.chattest.entity.User;
import com.chattest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String index() {
        return "forward:/index.html";
    }
    @PostMapping("/login-process")
    public String login(UserDTO userDTO) {
        User isValidUser = userService.findByEmail(userDTO.getEmail());
        if(isValidUser != null) {
            return "redirect:/index.html";
        }
        return "";
    }
}
