package com.example.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Size(min = 2, max = 100, message = "نام باید بین 2 تا 100 کاراکتر باشد")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "سن نمی‌تواند خالی باشد")
    @Min(value = 1, message = "سن باید بیشتر از 0 باشد")
    @Max(value = 150, message = "سن باید کمتر از 150 باشد")
    @Column(nullable = false)
    private Integer age;

    @NotBlank(message = "شهر نمی‌تواند خالی باشد")
    @Size(min = 2, max = 50, message = "نام شهر باید بین 2 تا 50 کاراکتر باشد")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "شغل نمی‌تواند خالی باشد")
    @Size(min = 2, max = 50, message = "نام شغل باید بین 2 تا 50 کاراکتر باشد")
    @Column(nullable = false)
    private String job;

    @JsonProperty("isActive")
    @Column(nullable = false)
    private Boolean isActive = true;

    public User() {}

    public User(String name, Integer age, String city, String job) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.job = job;
        this.isActive = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", job='" + job + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}