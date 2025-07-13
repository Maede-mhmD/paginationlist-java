package com.example.usermanagement.dto;

import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Size(min = 2, max = 100, message = "نام باید بین 2 تا 100 کاراکتر باشد")
    private String name;

    @NotNull(message = "سن نمی‌تواند خالی باشد")
    @Min(value = 1, message = "سن باید بیشتر از 0 باشد")
    @Max(value = 150, message = "سن باید کمتر از 150 باشد")
    private Integer age;

    @NotBlank(message = "شهر نمی‌تواند خالی باشد")
    @Size(min = 2, max = 50, message = "نام شهر باید بین 2 تا 50 کاراکتر باشد")
    private String city;

    @NotBlank(message = "شغل نمی‌تواند خالی باشد")
    @Size(min = 2, max = 50, message = "نام شغل باید بین 2 تا 50 کاراکتر باشد")
    private String job;

    public UserRequest() {}

    public UserRequest(String name, Integer age, String city, String job) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.job = job;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
}
