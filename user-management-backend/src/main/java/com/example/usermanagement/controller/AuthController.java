package com.example.usermanagement.controller;

import com.example.usermanagement.entity.Admin;
import com.example.usermanagement.service.AdminService;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.LoginResponse;
import com.example.usermanagement.security.JwtTokenProvider;
import com.example.usermanagement.service.ActivityLogService;
import com.example.usermanagement.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:3000", "http://127.0.0.1:3000" }, allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
            BindingResult result,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("error", "خطا در اعتبارسنجی", "details", errors));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Admin admin = adminService.findByUsername(loginRequest.getUsername());
            // Log login activity
            String ipAddress = IpUtil.getClientIpAddress(request);
            activityLogService.logLogin(loginRequest.getUsername(), ipAddress);

            LoginResponse response = new LoginResponse(
                    jwt,
                    admin.getUsername(),
                    admin.getFullName(),
                    (long) tokenProvider.getJwtExpirationInMs() / 1000);

        return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "نام کاربری یا رمز عبور اشتباه است"));
        }
    }
}