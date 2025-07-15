package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank(message = "نام کاربری نمی‌تواند خالی باشد")
    @Size(min = 3, max = 50, message = "نام کاربری باید بین 3 تا 50 کاراکتر باشد")
    private String username;
    
    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد")
    @Size(min = 6, message = "رمز عبور باید حداقل 6 کاراکتر باشد")
    private String password;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}