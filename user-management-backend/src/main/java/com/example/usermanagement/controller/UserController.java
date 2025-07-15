package com.example.usermanagement.controller;

import com.example.usermanagement.dto.PageResponse;
import com.example.usermanagement.dto.UserRequest;
import com.example.usermanagement.dto.UserStatusRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.ActionLogService;
import com.example.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActionLogService actionLogService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در اعتبارسنجی داده‌ها", "details", errors));
        }

        try {
            User user = userService.createUser(userRequest);

            // ثبت لاگ ایجاد کاربر
            actionLogService.logAction("CREATE_USER", user.getId(),
                    "کاربر جدید با نام " + user.getName() + " ایجاد شد");

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
            @RequestParam(required = false) Integer age) {

        try {
            PageResponse<User> users = userService.getUsers(page, per_page, name, city, job, age);

            // ثبت لاگ مشاهده لیست کاربران
            actionLogService.logAction("VIEW_USERS", null,
                    "مشاهده لیست کاربران - صفحه " + page + " با " + per_page + " آیتم");

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در دریافت لیست کاربران: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);

            // ثبت لاگ مشاهده کاربر
            actionLogService.logAction("VIEW_USER", id,
                    "مشاهده اطلاعات کاربر " + user.getName());

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
            BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در اعتبارسنجی: " + errors));
        }

        try {
            User user = userService.updateUserStatus(id, statusRequest);

            // ثبت لاگ تغییر وضعیت کاربر
            String status = statusRequest.getIsActive() ? "فعال" : "غیرفعال";
            actionLogService.logAction("UPDATE_USER_STATUS", id,
                    "وضعیت کاربر " + user.getName() + " به " + status + " تغییر یافت");

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "خطا در بروزرسانی وضعیت کاربر: " + e.getMessage()));
        }
    }
}