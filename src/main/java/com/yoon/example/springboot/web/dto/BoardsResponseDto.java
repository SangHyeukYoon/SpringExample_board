package com.yoon.example.springboot.web.dto;

import com.yoon.example.springboot.domain.board.Boards;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private List<String> filePath;

    public BoardsResponseDto(Boards entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.filePath = entity.getFilesRealPath();
    }

}
