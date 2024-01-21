package com.example.taskmanagementsystem.factory;

import com.example.taskmanagementsystem.service.FileStoreService;
import com.example.taskmanagementsystem.service.FileStoreServiceLocal;
import com.example.taskmanagementsystem.service.FileStoreServiceS3;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileStoreServiceFactory {

    private final Map<String, FileStoreService> fileStoreMap = new HashMap<>();

    public FileStoreServiceFactory() {
        fileStoreMap.put("S3", new FileStoreServiceS3());
    }

    public FileStoreService getFileStoreService(String param) {
        return fileStoreMap.getOrDefault(param, new FileStoreServiceLocal());
    }
}
