package com.example.usermanagement.security;

import com.example.usermanagement.entity.Admin;
import com.example.usermanagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("کاربر با نام کاربری " + username + " یافت نشد"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities("ROLE_ADMIN")
                .build();
    }
}