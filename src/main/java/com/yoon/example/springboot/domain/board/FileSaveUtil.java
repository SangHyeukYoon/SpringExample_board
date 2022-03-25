package com.yoon.example.springboot.domain.board;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FileSaveUtil {

    private final UploadedFilesRepository filesRepository;

    @Value("${resource.path}")
    private String resourcePath;

    public List<UploadedFiles> saveFiles(List<MultipartFile> files) throws Exception {
        List<UploadedFiles> uploadedFiles = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return uploadedFiles;
        }

        for (MultipartFile file: files) {
            String path = resourcePath + file.getOriginalFilename();
            String accessPath = "/img/" + file.getOriginalFilename();

            file.transferTo(new File(path));

            UploadedFiles uploadedFile = UploadedFiles.builder()
                    .filePath(path)
                    .realName(file.getOriginalFilename())
                    .accessPath(accessPath)
                    .build();
            filesRepository.save(uploadedFile);

            uploadedFiles.add(uploadedFile);

        }

        return uploadedFiles;
    }

}
