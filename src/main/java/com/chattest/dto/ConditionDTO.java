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
    private Float miniDust;
    private Float superMiniDust;
    private Float co2;
}
