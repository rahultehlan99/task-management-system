package com.example.taskmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`COMMENT`")
public class Comment extends AuditModel {

    @Id
    @UuidGenerator
    private String commentId;

    private String commentDescription;

    private String userId;

    private String taskId;
}
