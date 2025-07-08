package com.example.usermanagement.controller;

import com.example.usermanagement.dto.PageResponse;
import com.example.usermanagement.entity.ActivityLog;
import com.example.usermanagement.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = { "http://localhost:3000", "http://127.0.0.1:3000" }, allowCredentials = "true")
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/my-activities")
    public ResponseEntity<?> getMyActivities(Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int per_page) {
        try {
            String username = authentication.getName();
            PageResponse<ActivityLog> logs = activityLogService.getMyActivityLogs(username, page, per_page);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در دریافت لاگ‌ها: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllActivities(Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int per_page,
            @RequestParam(required = false) String admin_username,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String from_date,
            @RequestParam(required = false) String to_date) {
        try {
            LocalDateTime fromDate = null;
            LocalDateTime toDate = null;

            if (from_date != null && !from_date.isEmpty()) {
                fromDate = LocalDateTime.parse(from_date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }

            if (to_date != null && !to_date.isEmpty()) {
                toDate = LocalDateTime.parse(to_date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }

            PageResponse<ActivityLog> logs = activityLogService.getActivityLogs(
                    admin_username, action, fromDate, toDate, page, per_page);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در دریافت لاگ‌ها: " + e.getMessage()));
        }
    }
}