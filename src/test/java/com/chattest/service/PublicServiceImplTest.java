//package com.chattest.service;
//
//import com.chattest.dto.PublicDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PublicServiceImplTest {
//    @Autowired
//    private PublicService publicService;
//
//    @Test
//    public void testRegister() {
//        PublicDTO publicDTO = PublicDTO.builder()
//                .title("Sample Title")
//                .content("Sample content")
//                .writer("admin")
//                .build();
//
//        System.out.println(publicService.register(publicDTO));
//    }
//
//    @Test
//    public void findAll() {
//        List<PublicDTO> list = publicService.getListPublic();
//        System.out.println("list = " + list);
//    }
//
//    @Test
//    public void findByUserame() {
//        List<PublicDTO> list = publicService.getListPublicByAdmin();
//        System.out.println("list = " + list);
//    }
//
//}