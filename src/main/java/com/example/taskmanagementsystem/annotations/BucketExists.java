package com.example.taskmanagementsystem.annotations;

import com.example.taskmanagementsystem.validator.BucketExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BucketExistValidator.class)
public @interface BucketExists {

    String message() default "No bucket exists with the configured name.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
