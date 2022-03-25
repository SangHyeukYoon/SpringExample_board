package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.web.dto.boards.BoardsCreateRequestDto;
import com.yoon.example.springboot.web.dto.boards.BoardsSaveRequestDto;
import com.yoon.example.springboot.web.dto.boards.BoardsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class BoardsApiController {

    private final BoardService boardService;

    @PostMapping("/api/v1/board/post")
    public Long create(BoardsCreateRequestDto requestDto) throws Exception {
        BoardsSaveRequestDto saveRequestDto = BoardsSaveRequestDto.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .content(requestDto.getContent())
                .files(requestDto.getFiles())
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

}
