package com.example.taskmanagementsystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStoreService {
    List<String> upload(List<MultipartFile> file);

    List<Resource> download(List<String> file);

    List<String> getObjectPreSignedUrl(List<String> file);
}
