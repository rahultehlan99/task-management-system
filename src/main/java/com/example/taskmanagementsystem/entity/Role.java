package com.example.taskmanagementsystem.entity;

import com.example.taskmanagementsystem.enums.USER_ROLES;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ROLE")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private USER_ROLES userRoles;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> users = new HashSet<>();
}
