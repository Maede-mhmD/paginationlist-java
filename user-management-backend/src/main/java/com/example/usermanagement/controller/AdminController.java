package com.example.usermanagement.controller;

import com.example.usermanagement.entity.Admin;
import com.example.usermanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // این endpoint فقط برای تست و ایجاد اولین admin
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String fullName = request.get("fullName");

            Admin admin = adminService.createAdmin(username, password, fullName);
            return ResponseEntity.ok(Map.of("message", "ادمین با موفقیت ایجاد شد", "id", admin.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}