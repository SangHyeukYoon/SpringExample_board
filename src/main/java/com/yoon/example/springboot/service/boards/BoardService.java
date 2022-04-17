package com.yoon.example.springboot.service.boards;

import com.yoon.example.springboot.domain.board.*;

import com.yoon.example.springboot.domain.user.User;
import com.yoon.example.springboot.domain.user.UserRepository;
import com.yoon.example.springboot.web.UnauthorizedException;
import com.yoon.example.springboot.web.dto.BoardsListResponseDto;
import com.yoon.example.springboot.web.dto.BoardsResponseDto;
import com.yoon.example.springboot.web.dto.BoardsSaveRequestDto;
import com.yoon.example.springboot.web.dto.BoardsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardsRepository boardsRepository;
    private final UploadedFilesRepository filesRepository;
    private final UserRepository userRepository;

    private final FileSaveUtil fileSaveUtil;
    private final S3Uploader s3Uploader;

    @Transactional
    public Long save(BoardsSaveRequestDto requestDto) throws Exception {
//        List<UploadedFiles> uploadedFiles = fileSaveUtil.saveFiles(requestDto.getFiles());
        List<UploadedFiles> uploadedFiles = s3Uploader.uploadFiles(requestDto.getFiles());

        User user = userRepository.findByEmail(requestDto.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        Boards boards = Boards.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .content(requestDto.getContent())
                .user(user)
                .build();

        if (! uploadedFiles.isEmpty()) {
            for (UploadedFiles uploadedFile: uploadedFiles) {
                boards.addFiles(uploadedFile);
            }
        }

        return boardsRepository.save(boards).getId();
    }

    public Long count() {
        return boardsRepository.count();
    }

    @Transactional
    public Long update(Long id, BoardsUpdateRequestDto requestDto) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        User user = userRepository.findByEmail(requestDto.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if (! user.equals(board.getUser())) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        board.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public List<BoardsListResponseDto> findAllDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return boardsRepository.findAllByOrderByIdDesc(pageable).stream()
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

    @Transactional
    public void delImg(Long id, String fileName) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));
        UploadedFiles uploadedFile = filesRepository.findByBoards_IdAndRealName(id, fileName)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다. fileName=" + fileName));

        board.deleteImg(uploadedFile);
        filesRepository.delete(uploadedFile);

        // TODO: delete real file?
    }

}
