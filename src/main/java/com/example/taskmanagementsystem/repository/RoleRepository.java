package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.enums.USER_ROLES;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, USER_ROLES> {
}
