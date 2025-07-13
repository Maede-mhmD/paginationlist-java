package com.example.usermanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(name = "affected_id")
    private Long affectedId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    
    public ActionLog() {
        this.timestamp = LocalDateTime.now();
    }

    public ActionLog(String action, Long affectedId, String details, Long userId, String username) {
        this.action = action;
        this.affectedId = affectedId;
        this.details = details;
        this.userId = userId;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Long getAffectedId() { return affectedId; }
    public void setAffectedId(Long affectedId) { this.affectedId = affectedId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}