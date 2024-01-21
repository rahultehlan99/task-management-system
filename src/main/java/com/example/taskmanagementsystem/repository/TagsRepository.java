package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Optional<Tags> findByTagName(String tag);
}
