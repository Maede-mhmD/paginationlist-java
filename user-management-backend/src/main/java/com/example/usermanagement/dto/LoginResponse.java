package com.example.usermanagement.dto;

public class LoginResponse {
    private String username;
    private String token;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}