package com.example.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") 
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Java!";
    }
}