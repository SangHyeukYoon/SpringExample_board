package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.web.dto.BoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/")
    public String index(@RequestParam(value="page", defaultValue = "1") int page,
                        @RequestParam(value="size", defaultValue = "2") int size,
                        Model model) {
        Long count = boardService.count();
        int maxPage;

        if (count == 0) {
            maxPage = 1;
        } else {
            maxPage = (int)(count - 1) / size + 1;
        }

        model.addAttribute("boards", boardService.findAllDesc(page, size));
        model.addAttribute("maxPage", maxPage);
        return "index";
    }

    @GetMapping("/board/save")
    public String postsSave() {
        return "boards-save";
    }

    @GetMapping("/board/read/{id}")
    public String readBoard(@PathVariable Long id, Model model) {
        BoardsResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "boards-read";
    }

    @GetMapping("/board/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        BoardsResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "boards-update";
    }

}
