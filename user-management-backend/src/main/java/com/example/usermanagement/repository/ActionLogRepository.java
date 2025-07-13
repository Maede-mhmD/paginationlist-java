package com.example.usermanagement.repository;

import com.example.usermanagement.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    @Query("SELECT a FROM ActionLog a ORDER BY a.timestamp DESC")
    List<ActionLog> findAllOrderByTimestampDesc();
}