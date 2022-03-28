package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.web.dto.BoardsCreateRequestDto;
import com.yoon.example.springboot.web.dto.BoardsSaveRequestDto;
import com.yoon.example.springboot.web.dto.BoardsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class BoardsApiController {

    private final BoardService boardService;

    @PostMapping("/api/v1/board/post")
    public Long create(BoardsCreateRequestDto requestDto) throws Exception {
        List<MultipartFile> files = requestDto.getFiles();

        BoardsSaveRequestDto saveRequestDto = BoardsSaveRequestDto.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .content(requestDto.getContent())
                .files(files)
                .build();

        return boardService.save(saveRequestDto);
    }

    @PutMapping("/api/v1/board/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardsUpdateRequestDto requestDto) {
        boardService.update(id, requestDto);
        return id;
    }

    @DeleteMapping("/api/v1/board/{id}")
    public Long delete(@PathVariable Long id) {
        boardService.delete(id);
        return id;
    }

    @DeleteMapping("/api/v1/board/delImg/{id}/{fileName}")
    public Long deleteImg(@PathVariable Long id, @PathVariable String fileName) {
        boardService.delImg(id, fileName);
        return id;
    }

}
