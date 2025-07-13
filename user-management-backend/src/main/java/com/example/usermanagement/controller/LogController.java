package com.example.usermanagement.controller;

import com.example.usermanagement.entity.ActionLog;
import com.example.usermanagement.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LogController {

    @Autowired
    private ActionLogService actionLogService;

    @GetMapping("/logs")
    public ResponseEntity<List<ActionLog>> getLogs() {
        List<ActionLog> logs = actionLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}