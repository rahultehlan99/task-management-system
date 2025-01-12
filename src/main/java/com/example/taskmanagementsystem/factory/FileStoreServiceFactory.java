package com.example.taskmanagementsystem.factory;

import com.example.taskmanagementsystem.service.FileStoreService;
import com.example.taskmanagementsystem.service.FileStoreServiceS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileStoreServiceFactory {

    @Autowired
    private final Map<String, FileStoreService> fileStoreMap = new HashMap<>();

    public FileStoreService getFileStoreService(String param) {
        return fileStoreMap.get(param);
    }
}
