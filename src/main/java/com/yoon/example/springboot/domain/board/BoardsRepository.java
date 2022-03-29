package com.yoon.example.springboot.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

//    List<Boards> findAllByOrderByIdDesc();
    List<Boards> findAllByOrderByIdDesc(Pageable pageable);

}
