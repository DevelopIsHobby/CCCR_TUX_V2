package com.chattest.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionDTO {
    private String time;
    private Float temp;
    private Float humidity;
    private Float mini_dust;
    private Float super_mini_dust;
    private Float co2;
}
