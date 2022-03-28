package com.yoon.example.springboot.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadedFilesRepository extends JpaRepository<UploadedFiles, Long> {

    Optional<UploadedFiles> findByBoards_IdAndRealName(Long boards_id, String fileName);

}
