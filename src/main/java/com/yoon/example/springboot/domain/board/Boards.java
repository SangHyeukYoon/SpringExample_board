package com.yoon.example.springboot.domain.board;

import com.yoon.example.springboot.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Boards extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @OneToMany(mappedBy = "boards", cascade = CascadeType.REMOVE)
    List<UploadedFiles> fileDirs = new ArrayList<>();

    @Builder
    public Boards(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addFiles(UploadedFiles file) {
        fileDirs.add(file);
        file.setBoards(this);
    }

    public List<String> getFilesRealPath() {
        List <String> realPath = new ArrayList<>();

        if (fileDirs == null || fileDirs.isEmpty()) {
            return realPath;
        }

        for (UploadedFiles file: fileDirs) {
            realPath.add(file.getAccessPath());
        }

        return realPath;
    }

    public void deleteImg(UploadedFiles uploadedFile) {
        fileDirs.remove(uploadedFile);
    }

}
