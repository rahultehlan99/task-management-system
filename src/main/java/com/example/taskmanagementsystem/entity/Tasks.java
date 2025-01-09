package com.example.taskmanagementsystem.entity;

import com.example.taskmanagementsystem.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TASKS")
public class Tasks extends AuditModel {

    @Id
    @UuidGenerator
    private String taskId;

    private String taskName;

    @Column(name = "deadLine")
    private LocalDateTime deadLine;

    private int priority;

    private String taskDescription;

    private List<String> files = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    private boolean reminderEnabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId", referencedColumnName = "taskId")
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.getComments().add(comment);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_tags",
            joinColumns = { @JoinColumn(name = "tasks_id") },
            inverseJoinColumns = { @JoinColumn(name = "tags_id")})
    private Set<Tags> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "userId")
    private Users users;

    public void addFileToTask(String fileName) {
        this.getFiles().add(fileName);
    }
}
