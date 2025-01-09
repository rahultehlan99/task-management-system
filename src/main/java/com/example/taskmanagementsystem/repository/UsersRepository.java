package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query(value = "Select * from USERS where USER_NAME =:username", nativeQuery = true)
    Optional<Users> findByUserName(@Param("username") String username);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE USERS SET FAILED_ATTEMPTS = FAILED_ATTEMPTS + 1 where USER_NAME =:userName", nativeQuery = true)
    void increaseFailedAttempts(String userName);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE USERS SET FAILED_ATTEMPTS = 0 where USER_NAME =:userName", nativeQuery = true)
    void clearFailedPasswordAttempts(String userName);
}
