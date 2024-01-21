package com.example.taskmanagementsystem.service;

import org.springframework.core.io.Resource;

import java.util.List;

public interface FileStoreService {
    List<String> upload(List<Resource> file);

    List<Resource> download(List<String> file);
}
