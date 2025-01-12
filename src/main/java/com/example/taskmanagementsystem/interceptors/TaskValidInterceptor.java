package com.example.taskmanagementsystem.interceptors;

import com.example.taskmanagementsystem.annotations.TaskExists;
import com.example.taskmanagementsystem.repository.TasksRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class TaskValidInterceptor implements HandlerInterceptor {

    private final TasksRepository tasksRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(TaskExists.class)) {
                Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(RequestMappingHandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String taskId = pathVariables.get("taskId");
                if (taskId == null || tasksRepository.findTaskByTaskId(taskId) == null) {
                    throw new NoSuchElementException(String.format("No task with given id : %s", taskId));
                }
            }
        }
        return true;
    }
}
