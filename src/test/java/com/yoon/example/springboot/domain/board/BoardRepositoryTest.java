package com.yoon.example.springboot.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    BoardsRepository boardsRepository;

    @AfterEach
    private void cleanUp() {
        boardsRepository.deleteAll();
    }

    @Test
    public void Board_등록하기() {
        // given
        String title = "title";
        String author = "author";
        String content = "content";


        boardsRepository.save(Boards.builder()
                        .title(title)
                        .author(author)
                        .content(content)
                        .build());

        // when
        List<Boards> boardsList = boardsRepository.findAll();

        // then
        Boards board = boardsList.get(0);

        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
    }

    @Test
    public void Board_불러오기() {
        // given
        boardsRepository.save(Boards.builder()
                        .title("title1")
                        .author("author1")
                        .content("content1")
                        .build());

        boardsRepository.save(Boards.builder()
                .title("title2")
                .author("author2")
                .content("content2")
                .build());

        // when
        List<Boards> boardsList = boardsRepository.findAllByOrderByIdDesc();

        // then
        Boards board_1 = boardsList.get(0);
        Boards board_2 = boardsList.get(1);

        assertThat(board_1.getId()).isGreaterThan(board_2.getId());
    }

    @Test
    public void BasteTimeEntity_Test() {
        // given
        LocalDateTime now = LocalDateTime.of(2022, 3, 21, 0, 0, 0);

        boardsRepository.save(Boards.builder()
                .title("title1")
                .author("author1")
                .content("content1")
                .build());

        // when
        List<Boards> boardsList = boardsRepository.findAll();

        // then
        Boards board = boardsList.get(0);

        System.out.println(board.getAuthor());
        System.out.println(board.getId());
        System.out.println(board.getCreateDate());

//        assertThat(board.getCreateDate()).isAfter(now);
//        assertThat(board.getModifiedDate()).isAfter(now);
    }

}
