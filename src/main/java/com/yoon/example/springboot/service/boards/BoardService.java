package com.yoon.example.springboot.service.boards;

import com.yoon.example.springboot.domain.board.Boards;
import com.yoon.example.springboot.domain.board.BoardsRepository;
import com.yoon.example.springboot.domain.board.FileSaveUtil;
import com.yoon.example.springboot.domain.board.UploadedFiles;
import com.yoon.example.springboot.web.dto.boards.BoardsListResponseDto;
import com.yoon.example.springboot.web.dto.boards.BoardsResponseDto;
import com.yoon.example.springboot.web.dto.boards.BoardsSaveRequestDto;
import com.yoon.example.springboot.web.dto.boards.BoardsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardsRepository boardsRepository;
    private final FileSaveUtil fileSaveUtil;

    @Transactional
    public Long save(BoardsSaveRequestDto requestDto) throws Exception {
        Boards boards = Boards.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .content(requestDto.getContent())
                .build();

        List<UploadedFiles> uploadedFiles = fileSaveUtil.saveFiles(requestDto.getFiles());
        for (UploadedFiles uploadedFile: uploadedFiles) {
            boards.addFiles(uploadedFile);
        }

        return boardsRepository.save(boards).getId();
    }

    @Transactional
    public Long update(Long id, BoardsUpdateRequestDto requestDto) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        board.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public List<BoardsListResponseDto> findAllDesc() {
        return boardsRepository.findAllByOrderByIdDesc()
                .orElseGet(ArrayList::new).stream()
                .map(BoardsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardsResponseDto findById(Long id) {
        Boards entity = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        return new BoardsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));
        boardsRepository.delete(board);
    }

}
