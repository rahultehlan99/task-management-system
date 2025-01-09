package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Tasks;
import com.example.taskmanagementsystem.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, String> {
    Optional<Tasks> findTaskByTaskId(String taskId);

    @Query(value = "Select tasks.* from TASKS tasks JOIN TASK_TAGS tt on tasks.task_Id = tt.tasks_id " +
            " JOIN TAGS tags on tt.TAGS_ID = tags.TAG_ID where tags.TAG_NAME in :tags", nativeQuery = true)
    List<Tasks> findAllByTags(@Param(value = "tags") List<String> tags, Pageable pageable);

    @Query(value = "Select t.* from TASKS t JOIN USERS u ON t.users_id = u.userId where u.userId =:userId", nativeQuery = true)
    List<Tasks> findAllTasksOrderByUpdatedAtDesc(@Param("userId") long userId);

    @Query(value = "Select t.* from TASKS t where t.status in (:statuses) and t.dead_Line < :deadline and t.reminder_Enabled = true", nativeQuery = true)
    List<Tasks> findTasksByStatusAndDeadLine(@Param("statuses") List<String> taskStatusList,@Param("deadline") LocalDateTime date);

}

