package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
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

    @PostMapping("/board")
    public Long create(@RequestParam("title") String title,
                       @RequestParam("author") String author,
                       @RequestParam("content") String content,
                       @RequestParam(value = "files", required = false)
                                   List<MultipartFile> files) throws Exception {

        // DEBUG
        if (files != null) {
            for (MultipartFile file : files) {
                System.out.println(file.getOriginalFilename());
            }
        }

        BoardsSaveRequestDto requestDto = BoardsSaveRequestDto.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();

        return boardService.save(requestDto, files);
    }

    @PutMapping("/board/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardsUpdateRequestDto requestDto) {
        boardService.update(id, requestDto);
        return id;
    }

    @DeleteMapping("/board/{id}")
    public Long delete(@PathVariable Long id) {
        boardService.delete(id);
        return id;
    }

}
