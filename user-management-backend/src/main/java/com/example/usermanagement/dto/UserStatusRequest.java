package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusRequest {
    @NotNull(message = "وضعیت فعال بودن باید مشخص شود")
    private Boolean isActive;

    // Constructors
    public UserStatusRequest() {}

    public UserStatusRequest(Boolean isActive) {
        this.isActive = isActive;
    }

    // Getters and Setters
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}