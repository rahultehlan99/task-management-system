package com.example.taskmanagementsystem.validator;

import com.amazonaws.services.s3.AmazonS3;
import com.example.taskmanagementsystem.annotations.BucketExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BucketExistValidator implements ConstraintValidator<BucketExists, String> {
    private final AmazonS3 amazonS3;

    @Override
    public boolean isValid(String bucketName, ConstraintValidatorContext context) {
        return amazonS3.doesBucketExistV2(bucketName);
    }
}
