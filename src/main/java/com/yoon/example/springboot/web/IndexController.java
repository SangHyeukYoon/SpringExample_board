package com.yoon.example.springboot.web;

import com.yoon.example.springboot.domain.board.Boards;
import com.yoon.example.springboot.domain.board.BoardsRepository;
import com.yoon.example.springboot.domain.user.User;
import com.yoon.example.springboot.domain.user.UserRepository;
import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.service.oauth.dto.SessionUser;
import com.yoon.example.springboot.web.dto.BoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final BoardService boardService;
    private final HttpSession httpSession;

    private final UserRepository userRepository;
    private final BoardsRepository boardsRepository;

    @GetMapping("/")
    public String index(@RequestParam(value="page", defaultValue = "1") int page,
                        @RequestParam(value="size", defaultValue = "10") int size,
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

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("nickname", user.getName());
        }

        return "index";
    }

    @GetMapping("/board/save")
    public String postsSave(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        model.addAttribute("nickname", user.getName());

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
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        User currentUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 없습니다."));
        Boards boards = boardsRepository.getById(id);

//        if (currentUser.equals(boards.getUser())) {
//            throw new UnauthorizedException("사용자 불일치");
//        }

        BoardsResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "boards-update";
    }

    @GetMapping("/register")
    public String registerForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getName());
            model.addAttribute("email", principal.getEmail());
        }

        return "register";
    }

}
