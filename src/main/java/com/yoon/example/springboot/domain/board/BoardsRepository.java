package com.yoon.example.springboot.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

    Optional<List<Boards>> findAllByOrderByIdDesc();

}
