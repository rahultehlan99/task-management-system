package com.example.taskmanagementsystem.configurations;

import com.example.taskmanagementsystem.annotations.TaskExists;
import com.example.taskmanagementsystem.interceptors.TaskValidInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TaskValidInterceptor taskValidInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(taskValidInterceptor)
                .addPathPatterns("/api/v1/tasks/**");
    }
}
