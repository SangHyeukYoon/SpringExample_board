package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.web.dto.boards.BoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", boardService.findAllDesc());
        return "index";
    }

    @GetMapping("/board/save")
    public String postsSave() {
        return "boards-save";
    }

    @GetMapping("/board/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        BoardsResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "boards-update";
    }

}
