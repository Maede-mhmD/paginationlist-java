package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStatusRequest {
    @NotNull(message = "وضعیت فعال بودن نمی‌تواند خالی باشد")
    @JsonProperty("isActive")
    private Boolean isActive;

    public UserStatusRequest() {}

    public UserStatusRequest(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}