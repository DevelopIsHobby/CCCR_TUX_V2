package com.chattest.controller;

import com.chattest.dto.PublicDTO;
import com.chattest.entity.Public;
import com.chattest.service.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/public")
@Log4j2
@RequiredArgsConstructor
public class PublicController {
    private final PublicService publicService;

    @GetMapping("/list")
    @ResponseBody
    public List<PublicDTO> getListPublic() {
        log.info("public list......");
        List<PublicDTO> list = publicService.getListPublicByAdmin();
        return list;
    }

    @PostMapping("/register")
    @ResponseBody
    public Public register(@RequestBody PublicDTO publicDTO) {
        log.info("public register.......");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Public publicEntity = null;
        // 테스트를 위해 모든 사용자가 공지사항등록 가능
        publicEntity = publicService.register(publicDTO);

        // 관리자만 공지사항 등록 가능
//        if(name.equals("admin")) {
//            publicEntity = publicService.register(publicDTO);
//        }
        return publicEntity;
    }

    @PostMapping("/remove")
    @ResponseBody
    public void removePublic(@RequestBody Map<String, Long> requestBody) {
        log.info("removePublic...");
        Long pno = requestBody.get("pno");
        log.info("pno: {}", pno);

        publicService.removePublic(pno);
    }

    @PostMapping("/modify")
    @ResponseBody
    public Public modifyPublic(@RequestBody PublicDTO publicDTO) {
        log.info("modifyPublic...");
        log.info("publicDTO {} ", publicDTO);

        Public publicEntity = publicService.modify(publicDTO);

        return publicEntity;
    }
}
