package com.chattest.service;

import com.chattest.dto.FeedDTO;
import com.chattest.entity.Feed;
import com.chattest.repository.FeedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeedServiceImplTest {
    @Autowired
    private FeedService feedService;
    @Autowired
    private FeedRepository feedRepository;

    public void insertDummies() {
        IntStream.rangeClosed(1,10).forEach(i -> {
            Feed feed = Feed.builder()
                    .name("happydaddy")
                    .content("Test content...")
                    .instructor("Test instructor...")
                    .build();
            System.out.println(feedRepository.save(feed));
        });
    }

//    @Test
//    void register() {
//        FeedDTO feedDTO = FeedDTO.builder()
//                .name("Sample Name")
//                .content("Sammple Content")
//                .instructor("Sample Instructor")
//                .build();
//
//        System.out.println(feedService.register(feedDTO));
//    }

    @Test
    void findAll() {
        List<FeedDTO> list = feedService.getList();
        System.out.println("list = " + list);
    }

    @Test
    void findByUsername() {
        insertDummies();
        List<FeedDTO> list = feedService.getListByUsername("사용자1");
        System.out.println("list = " + list);
    }

    @Test
    void deleteTest() {
        feedService.remove(17l);
    }
}