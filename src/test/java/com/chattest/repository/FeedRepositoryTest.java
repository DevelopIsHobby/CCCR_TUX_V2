package com.chattest.repository;

import com.chattest.dto.FeedDTO;
import com.chattest.entity.Feed;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FeedRepositoryTest {
    @Autowired FeedRepository feedRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            // 1에서 5 사이의 정수를 랜덤으로 생성
            int emotionId = (int) (Math.random() * 5) + 1;

            Feed feed = Feed.builder()
                    .name("happydaddy")
                    .content("Test content...")
                    .instructor("Test instructor...")
                    .emotionId(emotionId)
                    .build();
            System.out.println(feedRepository.save(feed));
        });
    }

    @Test
    public void updateTest() {
        insertDummies();
        Optional<Feed> result = feedRepository.findById(5L);
        if(result.isPresent()) {
            Feed feed = result.get();

            feed.changeContent("Changed Content");
            feed.changeInstructor("Changed Instructor");

            feedRepository.save(feed);
        }
    }

    @Test
    public void findByUsernameTest() {
        String username = "사용자1";
        List<Feed> feeds = feedRepository.findByName(username);
        System.out.println("feeds = " + feeds);
    }

}