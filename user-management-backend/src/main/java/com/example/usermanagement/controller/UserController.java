package com.example.usermanagement.controller;

import com.example.usermanagement.dto.PageResponse;
import com.example.usermanagement.dto.UserRequest;
import com.example.usermanagement.dto.UserStatusRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.ActivityLogService;
import com.example.usermanagement.service.UserService;
import com.example.usermanagement.util.IpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "http://localhost:3000", "http://127.0.0.1:3000" }, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest,
            BindingResult result,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در اعتبارسنجی داده‌ها", "details", errors));
        }

        try {
            User user = userService.createUser(userRequest);

            logUserActivity("CREATE_USER", "ایجاد کاربر جدید", user.getId(), user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در ایجاد کاربر: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int per_page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) Integer age,
            HttpServletRequest request) {

        try {
            PageResponse<User> users = userService.getUsers(page, per_page, name, city, job, age);

            String filters = String.format("name=%s, city=%s, job=%s, age=%s", name, city, job, age);
            activityLogService.logUsersList(getCurrentUsername(), IpUtil.getClientIpAddress(request), filters);

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در دریافت لیست کاربران: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, HttpServletRequest request) {
        try {
            User user = userService.getUserById(id);

            activityLogService.logUserView(getCurrentUsername(), IpUtil.getClientIpAddress(request), id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در دریافت کاربر: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id,
            @Valid @RequestBody UserStatusRequest statusRequest,
            BindingResult result,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در اعتبارسنجی: " + errors));
        }

        try {
            User user = userService.updateUserStatus(id, statusRequest);

            logUserActivity("UPDATE_USER", "بروزرسانی وضعیت کاربر", user.getId(), user, request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در بروزرسانی وضعیت کاربر: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            User userToDelete = userService.getUserById(id);
            userService.deleteUser(id);

            logUserActivity("DELETE_USER", "حذف کاربر", id, userToDelete, request);

            return ResponseEntity.ok(Map.of("message", "کاربر با موفقیت حذف شد"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در حذف کاربر: " + e.getMessage()));
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "SYSTEM";
    }

    private void logUserActivity(String action, String description, Long entityId, Object data,
            HttpServletRequest request) {
        String adminUsername = getCurrentUsername();
        String ipAddress = IpUtil.getClientIpAddress(request);
        String details = "";
        try {
            details = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            details = "Error converting data to JSON";
        }
        activityLogService.logActivity(adminUsername, action, description, "User", entityId, ipAddress, details);
    }
}