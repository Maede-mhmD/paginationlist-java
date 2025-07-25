package com.example.usermanagement.service;

import com.example.usermanagement.entity.AppUser;
import com.example.usermanagement.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
        this.appUserRepository =appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        AppUser appUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("کاربر با نام کاربری"  + username + "یافت نشد"));
        return appUser;    
    }

    // @Autowired
    // private AppUserRepository appUserRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     AppUser appUser = appUserRepository.findByUsername(username)
    //         .orElseThrow(() -> new UsernameNotFoundException("کاربر با نام کاربری " + username + " یافت نشد"));
    //     return appUser;
    // }
}