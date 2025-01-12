package com.example.taskmanagementsystem.utils;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;

public class Encoder {

    // TODO : basic client encoder and decoder for content stored in S3
    public static byte[] encodeStream(Resource resource) throws IOException {
        return Base64.getEncoder().encode(resource.getContentAsByteArray());
    }

    public static byte[] decodeStream(byte[] file) {
        return Base64.getDecoder().decode(file);
    }
}
