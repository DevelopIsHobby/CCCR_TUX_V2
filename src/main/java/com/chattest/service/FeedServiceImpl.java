package com.chattest.service;

import com.chattest.dto.FeedDTO;
import com.chattest.entity.Feed;
import com.chattest.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    private final FeedRepository feedRepository;

    @Override
    public Feed register(FeedDTO feedDTO, String name) {
        log.info("FeedServiceImpl DTO : " + feedDTO.toString());
        feedDTO.setName(name);
        Feed entity = dtoToEntity(feedDTO);
        log.info("dto converted to entity : " + entity);

        feedRepository.save(entity);

        return entity;
    }

    @Override
    public List<FeedDTO> getList() {
        List<Feed> feedList = feedRepository.findAll();
        Function<Feed, FeedDTO> fn = (entity -> entityToDTO(entity));
        List<FeedDTO> feedDTOList = feedList.stream().map(fn).collect(Collectors.toList());
        return feedDTOList;
    }

    @Override
    public List<FeedDTO> getListByUsername(String username) {
        List<Feed> feeds = feedRepository.findByName(username);
        Function<Feed, FeedDTO> fn = (entity -> entityToDTO(entity));
        List<FeedDTO> feedDTOs = feeds.stream().map(fn).collect(Collectors.toList());

        return feedDTOs;
    }

    @Override
    public void remove(Long fno) {
        feedRepository.deleteById(fno);
    }

    @Override
    public Feed modify(FeedDTO feedDTO) {
        Optional<Feed> result = feedRepository.findById(feedDTO.getFno());
        Feed feed = null;

        if(result.isPresent()) {
            feed = result.get();

            feed.changeInstructor(feedDTO.getInstructor());
            feed.changeContent(feedDTO.getContent());
            feed.changeEmotionId(feedDTO.getEmotionId());

            feedRepository.save(feed);
        }
        return feed;
    }

}
