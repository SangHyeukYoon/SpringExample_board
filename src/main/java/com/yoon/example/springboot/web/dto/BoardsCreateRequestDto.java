package com.yoon.example.springboot.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardsCreateRequestDto {

    private String title;
    private String author;
    private String content;
    private List<MultipartFile> files;

    @Builder
    BoardsCreateRequestDto(String title, String author, String content, List<MultipartFile> files) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.files = files;
    }

}
