package com.yoon.example.springboot.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFilesRepository extends JpaRepository<UploadedFiles, Long> {
}
