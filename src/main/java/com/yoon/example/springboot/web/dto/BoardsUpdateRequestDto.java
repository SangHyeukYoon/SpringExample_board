package com.yoon.example.springboot.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardsUpdateRequestDto {

    private String title;
    private String content;
    private String userEmail;

    @Builder
    public BoardsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
