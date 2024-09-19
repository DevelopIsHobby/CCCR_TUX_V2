package com.chattest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Feed extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @Column(length=100, nullable = false)
    private String name;

    @Column(length=1500, nullable = false)
    private String content;

    @Column(length=50, nullable = false)
    private String instructor;

    @Column
    private int emotionId;

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void changeEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }
}
