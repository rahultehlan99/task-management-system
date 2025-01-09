package com.example.taskmanagementsystem.schedulers;

import com.example.taskmanagementsystem.entity.Tasks;
import com.example.taskmanagementsystem.enums.TaskStatus;
import com.example.taskmanagementsystem.repository.TasksRepository;
import com.example.taskmanagementsystem.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("taskReminderScheduler")
@RequiredArgsConstructor
public class TaskReminderScheduler implements SchedulerService {

    private final TasksRepository tasksRepository;
    private final MailService mailService;

    @Override
    @Scheduled(cron = "${reminder.cron.expression}")
    public void schedule() {
        log.info("Getting reminder tasks");
        List<Tasks> todoTasks = tasksRepository.findTasksByStatusAndDeadLine(Arrays.asList(TaskStatus.TODO.name(), TaskStatus.IN_PROGRESS.name()), LocalDateTime.now().plusDays(1));
        todoTasks.forEach(task -> {
            log.info("Reminder mail for task : {}", task.getTaskName());
            mailService.sendMail(String.format("Task : %s is due on : %s", task.getTaskName(), task.getDeadLine()), task.getUsers().getMailId());
        });
    }
}
