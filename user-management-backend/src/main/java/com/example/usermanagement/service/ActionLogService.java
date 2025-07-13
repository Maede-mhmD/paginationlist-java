package com.example.usermanagement.service;

import com.example.usermanagement.entity.ActionLog;
import com.example.usermanagement.repository.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionLogService {

    @Autowired
    private ActionLogRepository actionLogRepository;

    public void logAction(String action, Long affectedId, String details) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Long userId = null;
        String username = "anonymous";
        
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            username = authentication.getName();
        }
        
        ActionLog log = new ActionLog(action, affectedId, details, userId, username);
        actionLogRepository.save(log);
    }

    public List<ActionLog> getAllLogs() {
        return actionLogRepository.findAllOrderByTimestampDesc();
    }
}