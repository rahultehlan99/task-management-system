package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.utils.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("fileStoreServiceS3")
public class FileStoreServiceS3 implements FileStoreService {

    private static final String imageLocationDirectory = System.getenv("image.upload.dir.aws.s3");

    @Value("${s3.username:S3USERNAME}")
    private String s3UserName;

    @Value("${s3.accessKey:S3ACCESSKEY}")
    private String s3AccessKey;

    @Override
    public List<String> upload(List<Resource> files) {
        log.info("Request received for uploading files to S3");
        log.info("Getting files from S3 with {} and {}", s3UserName, s3AccessKey);
        final List<String> uploadedFileNames = new ArrayList<>();
        files.parallelStream().forEach(file -> {
            Path filePath = Paths.get(imageLocationDirectory, file.getFilename() + ".txt");
            try {
                Files.write(filePath, Encoder.encodeStream(file));
                uploadedFileNames.add(file.getFilename());
            } catch (IOException e) {
                log.info("Exception occurred while uploading file {}", file.getFilename());
                throw new RuntimeException(e);
            }
        });
        return uploadedFileNames;
    }

    @Override
    public List<Resource> download(List<String> fileLocations) {
        log.info("Downloading files from S3!!");
        List<Resource> responseData = new ArrayList<>();
        fileLocations.parallelStream().forEach(fileLocation -> {
            Path filePath = Paths.get(imageLocationDirectory + "/" + fileLocation);
            try {
                Resource byteArrayResource = new ByteArrayResource(Encoder.decodeStream(Files.readAllBytes(filePath)));
                responseData.add(byteArrayResource);
            } catch (IOException e) {
                log.info("Exception occurred while downloading file {}", fileLocation);
                throw new RuntimeException(e);
            }
        });
        return responseData;
    }
}
