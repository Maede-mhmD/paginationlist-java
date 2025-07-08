package com.example.usermanagement.repository;

import com.example.usermanagement.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
       Page<ActivityLog> findByAdminUsernameOrderByTimestampDesc(String adminUsername, Pageable pageable);

       @Query("SELECT a FROM ActivityLog a WHERE " +
                     "(:adminUsername IS NULL OR a.adminUsername = :adminUsername) AND " +
                     "(:action IS NULL OR a.action = :action) AND " +
                     "(:fromDate IS NULL OR a.timestamp >= :fromDate) AND " +
                     "(:toDate IS NULL OR a.timestamp <= :toDate) " +
                     "ORDER BY a.timestamp DESC")
       Page<ActivityLog> findLogsWithFilters(@Param("adminUsername") String adminUsername,
                     @Param("action") String action,
                     @Param("fromDate") LocalDateTime fromDate,
                     @Param("toDate") LocalDateTime toDate,
                     Pageable pageable);
}