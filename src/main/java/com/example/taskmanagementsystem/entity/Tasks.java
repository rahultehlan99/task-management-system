package com.example.taskmanagementsystem.entity;

import com.example.taskmanagementsystem.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TASKS")
public class Tasks {

    @Id
    @UuidGenerator
    private String taskId;

    private String taskName;

    private LocalDateTime deadLine;

    private int priority;

    private String taskDescription;

    private List<String> files;

    private TaskStatus status = TaskStatus.TODO;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_tags",
            joinColumns = { @JoinColumn(name = "tasks_id") },
            inverseJoinColumns = { @JoinColumn(name = "tags_id")})
    private Set<Tags> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "userId")
    private Users users;

    public void addTag(Tags tags){
        getTags().add(tags);
        tags.getTasks().add(this);
    }
}
