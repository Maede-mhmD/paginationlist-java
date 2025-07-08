package com.example.usermanagement.service;

import com.example.usermanagement.dto.PageResponse;
import com.example.usermanagement.entity.ActivityLog;
import com.example.usermanagement.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void logActivity(String adminUsername, String action, String description, String entityType, Long entityId, String ipAddress, String details) {
        ActivityLog log = new ActivityLog(adminUsername, action, description, entityType, entityId, ipAddress, details);
        activityLogRepository.save(log);
    }

    public void logUserCreation(String adminUsername, String ipAddress, Long userId, String userDetails) {
        logActivity(adminUsername, "CREATE_USER", "ایجاد کاربر جدید", "User", userId, ipAddress, userDetails);
    }

    public void logUserUpdate(String adminUsername, String ipAddress, Long userId, String userDetails) {
        logActivity(adminUsername, "UPDATE_USER", "بروزرسانی وضعیت کاربر", "User", userId, ipAddress, userDetails);
    }

    public void logUserDelete(String adminUsername, String ipAddress, Long userId, String userDetails) {
        logActivity(adminUsername, "DELETE_USER", "حذف کاربر", "User", userId, ipAddress, userDetails);
    }

    public void logUserView(String adminUsername, String ipAddress, Long userId) {
        logActivity(adminUsername, "VIEW_USER", "مشاهده جزئیات کاربر", "User", userId, ipAddress, null);
    }

    public void logUsersList(String adminUsername, String ipAddress, String filters) {
        logActivity(adminUsername, "LIST_USERS", "مشاهده لیست کاربران", "User", null, ipAddress, filters);
    }

    public void logLogin(String adminUsername, String ipAddress) {
        logActivity(adminUsername, "LOGIN", "ورود به سیستم", "Auth", null, ipAddress, null);
    }

    public PageResponse<ActivityLog> getActivityLogs(String adminUsername, String action, LocalDateTime fromDate, LocalDateTime toDate, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<ActivityLog> logPage = activityLogRepository.findLogsWithFilters(adminUsername, action, fromDate, toDate, pageable);
        
        return new PageResponse<>(
            logPage.getContent(),
            logPage.getTotalElements(),
            logPage.getTotalPages(),
            page,
            perPage
        );
    }

    public PageResponse<ActivityLog> getMyActivityLogs(String adminUsername, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<ActivityLog> logPage = activityLogRepository.findByAdminUsernameOrderByTimestampDesc(adminUsername, pageable);
        
        return new PageResponse<>(
            logPage.getContent(),
            logPage.getTotalElements(),
            logPage.getTotalPages(),
            page,
            perPage
        );
    }
}