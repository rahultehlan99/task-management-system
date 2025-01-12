package com.example.taskmanagementsystem.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("S3")
@RequiredArgsConstructor
public class FileStoreServiceS3 implements FileStoreService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public List<String> upload(List<MultipartFile> files) {
        log.info("Request received for uploading files to S3");
        List<String> filesPlaced = new ArrayList<>();
        files.forEach(fl -> {
            try {
                s3Client.putObject(new PutObjectRequest(bucketName, fl.getName(), fl.getInputStream(), null));
                log.info("File {} successfully placed on S3", fl.getOriginalFilename());
                filesPlaced.add(fl.getOriginalFilename());
            } catch (IOException e) {
                log.error("Error placing file {} on S3", fl.getOriginalFilename() + e);
            }
        });

        return filesPlaced;
    }

    @Override
    public List<Resource> download(List<String> fileLocations) {
        log.info("Downloading files from S3!!");
        List<Resource> responseData = new ArrayList<>();
        fileLocations.parallelStream().forEach(fileLocation -> {
            try {
                S3Object s3Object = s3Client.getObject(bucketName, fileLocation);
                responseData.add(new InputStreamResource(s3Object.getObjectContent()));
            } catch (Exception e) {
                log.info("Exception occurred while downloading file {}", fileLocation);
                throw new RuntimeException(e);
            }
        });
        return responseData;
    }

    @Override
    public List<String> getObjectPreSignedUrl(List<String> files) {
        List<String> preSignedUrls = new ArrayList<>();
        files.forEach(file -> {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, file)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(new Date(System.currentTimeMillis() + 600 * 1000));
            preSignedUrls.add(String.valueOf(s3Client.generatePresignedUrl(generatePresignedUrlRequest)));
        });
        return preSignedUrls;
    }

}
