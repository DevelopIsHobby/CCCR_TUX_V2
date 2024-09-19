package com.chattest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedDTO {
    private Long fno;
    private String name;
    private String content;
    private String instructor;
    private LocalDateTime regDate, modDate;
    private int emotionId;
}
