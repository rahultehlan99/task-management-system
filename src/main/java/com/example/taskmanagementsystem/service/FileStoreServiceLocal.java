package com.example.taskmanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
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
@Service("fileStoreServiceLocal")
public class FileStoreServiceLocal implements FileStoreService {

    private static final String imageLocationDirectory = System.getenv("image.upload.dir");

    @Override
    public List<String> upload(List<Resource> files) {
        log.info("Request received for uploading files");
        final List<String> uploadedFileNames = new ArrayList<>();
        files.parallelStream().forEach(file -> {
            Path filePath = Paths.get(imageLocationDirectory, file.getFilename());
            try {
                Files.write(filePath, file.getContentAsByteArray());
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
        List<Resource> responseData = new ArrayList<>();
        fileLocations.parallelStream().forEach(fileLocation -> {
            Path filePath = Paths.get(imageLocationDirectory + "/" + fileLocation);
            try {
                ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(filePath));
                responseData.add(byteArrayResource);
            } catch (IOException e) {
                log.info("Exception occurred while downloading file {}", fileLocation);
                throw new RuntimeException(e);
            }
        });
        return responseData;
    }
}
