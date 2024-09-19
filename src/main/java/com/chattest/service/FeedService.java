package com.chattest.service;

import com.chattest.dto.FeedDTO;
import com.chattest.entity.Feed;

import java.util.List;

public interface FeedService {
    Feed register(FeedDTO feedDTO,String name);

    List<FeedDTO> getList();

    void remove(Long fno);

    Feed modify(FeedDTO feedDTO);

    List<FeedDTO> getListByUsername(String username);
    default Feed dtoToEntity(FeedDTO feedDTO) {
        Feed feed = Feed.builder()
                .fno(feedDTO.getFno())
                .name(feedDTO.getName())
                .content(feedDTO.getContent())
                .instructor(feedDTO.getInstructor())
                .emotionId(feedDTO.getEmotionId())
                .build();
        return feed;
    }

    default FeedDTO entityToDTO(Feed feed) {
        FeedDTO feedDTO = FeedDTO.builder()
                .fno(feed.getFno())
                .name(feed.getName())
                .content(feed.getContent())
                .instructor(feed.getInstructor())
                .emotionId(feed.getEmotionId())
                .regDate(feed.getRegDate())
                .modDate(feed.getModDate())
                .build();

        return feedDTO;
    }
}
