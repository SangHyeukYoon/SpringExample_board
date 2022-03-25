package com.yoon.example.springboot.web;

import com.yoon.example.springboot.domain.board.Boards;
import com.yoon.example.springboot.domain.board.BoardsRepository;
import com.yoon.example.springboot.domain.board.UploadedFiles;
import com.yoon.example.springboot.web.dto.boards.BoardsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BoardsRepository boardsRepository;

    @AfterEach
    public void cleanUp() {
        boardsRepository.deleteAll();
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public void board_등록하기() throws Exception {
        // given
        String title = "title";
        String author = "author";
        String content = "content";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        String testFilePath = "/Users/yoon/IdeaProjects/SpringExample/files/img_1.jpeg";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", title);
        body.add("author", author);
        body.add("content", content);
        body.add("files", new FileSystemResource(new File(testFilePath)));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "http://localhost:" + port + "/board";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Boards> all = boardsRepository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

        List<UploadedFiles> uploadedFiles = all.get(0).getFileDirs();
        assertThat(uploadedFiles.get(0).getRealName()).isEqualTo("img_1.jpeg");
    }

    @Test
    public void Boards_수정하기() {
        // given
        Boards savedBoard = boardsRepository.save(Boards.builder()
                        .title("title")
                        .content("content")
                        .author("author")
                        .build());

        Long boardId = savedBoard.getId();

        String newTitle = "title2";
        String newContent = "content2";

        BoardsUpdateRequestDto requestDto = BoardsUpdateRequestDto.builder()
                .title(newTitle)
                .content(newContent)
                .build();

        String url = "http://localhost:" + port + "/board/" + boardId;

        HttpEntity<BoardsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(boardId);

        List<Boards> all = boardsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(newTitle);
        assertThat(all.get(0).getContent()).isEqualTo(newContent);
    }

}
