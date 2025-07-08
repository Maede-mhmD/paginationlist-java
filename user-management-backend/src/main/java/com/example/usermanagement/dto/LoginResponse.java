package com.example.usermanagement.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String fullName;
    private Long expiresIn;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, String username, String fullName, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
}