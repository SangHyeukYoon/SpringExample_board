package com.yoon.example.springboot.web.dto.boards;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardsSaveRequestDto {

    private String title;
    private String author;
    private String content;
    private List<MultipartFile> files;

    @Builder
    BoardsSaveRequestDto(String title, String author, String content, List<MultipartFile> files) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.files = files;
    }
}
