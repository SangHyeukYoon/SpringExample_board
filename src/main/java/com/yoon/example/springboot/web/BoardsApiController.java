package com.yoon.example.springboot.web;

import com.yoon.example.springboot.service.boards.BoardService;
import com.yoon.example.springboot.service.oauth.RegisterOidcUserService;
import com.yoon.example.springboot.service.oauth.dto.SessionUser;
import com.yoon.example.springboot.web.dto.BoardsCreateRequestDto;
import com.yoon.example.springboot.web.dto.BoardsSaveRequestDto;
import com.yoon.example.springboot.web.dto.BoardsUpdateRequestDto;
import com.yoon.example.springboot.web.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class BoardsApiController {

    private final BoardService boardService;
    private final RegisterOidcUserService oidcUserService;

    private final HttpSession httpSession;

    @PostMapping("/api/v1/board/post")
    public Long create(BoardsCreateRequestDto requestDto) throws Exception {
        List<MultipartFile> files = requestDto.getFiles();

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        BoardsSaveRequestDto saveRequestDto = BoardsSaveRequestDto.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .content(requestDto.getContent())
                .files(files)
                .userEmail(user.getEmail())
                .build();

        return boardService.save(saveRequestDto);
    }

    @PutMapping("/api/v1/board/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardsUpdateRequestDto requestDto) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        requestDto.setUserEmail(user.getEmail());

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

    // TODO: Refactor to new class
    @PostMapping("/register")
    public Long registerUser(@RequestBody RegisterRequestDto requestDto, @AuthenticationPrincipal OidcUser principal) {
        return oidcUserService.registerUser(requestDto.getNickname(), principal);
    }

}
