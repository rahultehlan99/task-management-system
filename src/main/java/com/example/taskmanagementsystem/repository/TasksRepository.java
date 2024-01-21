package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Tasks;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, String> {
    Optional<Tasks> findTaskByTaskId(String taskId);

    @Query(value = "Select tasks.* from TASKS tasks JOIN TASK_TAGS tt on tasks.task_Id = tt.tasks_id " +
            " JOIN TAGS tags on tt.TAGS_ID = tags.TAG_ID where tags.TAG_NAME in :tags", nativeQuery = true)
    List<Tasks> findAllByTags(@Param(value = "tags") List<String> tags, Pageable pageable);
}
