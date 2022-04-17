package com.yoon.example.springboot.domain.board;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private final UploadedFilesRepository filesRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public List<UploadedFiles> uploadFiles(List<MultipartFile> files) throws Exception {
        List<UploadedFiles> uploadedFilesList = new ArrayList<>();

        if (files != null && ! files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                String realName = multipartFile.getOriginalFilename();
                File uploadFile = File.createTempFile(UUID.randomUUID().toString(), "_" + realName);
                multipartFile.transferTo(uploadFile);

                uploadedFilesList.add(upload2S3(uploadFile, realName));
            }
        }

        return uploadedFilesList;
    }

    private UploadedFiles upload2S3(File uploadFile, String realName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, uploadFile.getName(), uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadedUrl = amazonS3Client.getUrl(bucket, uploadFile.getName()).toString();
        String bucket = "board-image-bucket";
        int bucketIndex = uploadedUrl.indexOf(bucket) + bucket.length();
        String resizedUrl = uploadedUrl.substring(0, bucketIndex)
                + "-resize" + uploadedUrl.substring(bucketIndex);

        UploadedFiles uploadedFiles = UploadedFiles.builder()
                .realName(realName)
                .filePath(uploadedUrl)
                .accessPath(resizedUrl)
                .build();

        filesRepository.save(uploadedFiles);

        return uploadedFiles;
    }

}
