package com.example.usermanagement.service;

import com.example.usermanagement.entity.Admin;
import com.example.usermanagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Admin createAdmin(String username, String password, String fullName) {
        if (adminRepository.existsByUsername(username)) {
            throw new RuntimeException("نام کاربری قبلاً استفاده شده است");
        }

        Admin admin = new Admin(username, passwordEncoder.encode(password), fullName);
        return adminRepository.save(admin);
    }

    public Admin findByUsername(String username) {
         return adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));
    }

     public Admin save(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
}