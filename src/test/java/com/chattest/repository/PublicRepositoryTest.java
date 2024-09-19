package com.chattest.repository;

import com.chattest.entity.Feed;
import com.chattest.entity.Public;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PublicRepositoryTest {
    @Autowired
    PublicRepository publicRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,10).forEach(i -> {
            Public publicEntity = Public.builder()
                    .title("Sample title" + i)
                    .content("Sample content" + i)
                    .writer("admin")
                    .build();

            System.out.println(publicRepository.save(publicEntity));

        });
    }
}