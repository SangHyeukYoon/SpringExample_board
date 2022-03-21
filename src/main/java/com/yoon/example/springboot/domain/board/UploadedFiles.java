package com.yoon.example.springboot.domain.board;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UploadedFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String filePath;
    private String realName;
    private String accessPath;

    @Builder
    public UploadedFiles(String filePath, String realName, String accessPath) {
        this.filePath = filePath;
        this.realName = realName;
        this.accessPath = accessPath;
    }

    @ManyToOne
    private Boards boards;

}
