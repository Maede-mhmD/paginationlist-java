package com.example.usermanagement.controller;

import com.example.usermanagement.entity.AppUser;
import com.example.usermanagement.repository.AppUserRepository;
import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.dto.ApiResponse;
import com.example.usermanagement.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RegisterController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ActionLogService actionLogService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request,
            HttpServletRequest servletRequest) {
        
        System.out.println("Register request received for username: " + request.getUsername());
        
        try {
            // بررسی وجود نام کاربری
            if (appUserRepository.existsByUsername(request.getUsername())) {
                System.out.println("Username already exists: " + request.getUsername());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "نام کاربری قبلاً استفاده شده است"));
            }
            
            // بررسی طول رمز عبور
            if (request.getPassword().length() < 6) {
                System.out.println("Password too short");
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "رمز عبور باید حداقل 6 کاراکتر باشد"));
            }
            
            
            // ساخت کاربر جدید
            AppUser newUser = new AppUser(
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()));
            
            System.out.println("Saving user to database...");
            AppUser savedUser = appUserRepository.save(newUser);

            System.out.println("User saved with ID: " + savedUser.getId());

            // ثبت لاگ ثبت‌نام
            actionLogService.logAction("REGISTER", savedUser.getId(), 
                    "کاربر جدید " + request.getUsername() + " ثبت‌نام کرد");

            System.out.println("Starting authentication...");
            // لاگین خودکار پس از ثبت نام
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // ایجاد session
            HttpSession session = servletRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            
            System.out.println("Registration successful for: " + request.getUsername());

            return ResponseEntity.ok(new ApiResponse(true, "ثبت نام با موفقیت انجام شد"));
            
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "خطا در ثبت نام: " + e.getMessage()));
        }
    }
}