package com.example.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UserManagementApplication {

    public static void main(String[] args) {
        System.out.println("🚀 در حال راه‌اندازی User Management System...");
        SpringApplication.run(UserManagementApplication.class, args);
        System.out.println("✅ سرور با موفقیت راه‌اندازی شد!");
        System.out.println("📍 سرور در آدرس http://localhost:5000 در حال اجرا است");
        System.out.println("🗃️ H2 Console: http://localhost:5000/h2-console");
        System.out.println("👤 Admin پیش‌فرض: admin / admin123");
    }
}