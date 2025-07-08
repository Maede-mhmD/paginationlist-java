package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class UserRequest {
    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    private String name;

    @Min(value = 1, message = "سن باید بیشتر از 0 باشد")
    @Max(value = 150, message = "سن باید کمتر از 150 باشد")
    private Integer age;

    @NotBlank(message = "شهر نمی‌تواند خالی باشد")
    private String city;

    @NotBlank(message = "شغل نمی‌تواند خالی باشد")
    private String job;

    // Constructors
    public UserRequest() {}

    public UserRequest(String name, Integer age, String city, String job) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.job = job;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
}