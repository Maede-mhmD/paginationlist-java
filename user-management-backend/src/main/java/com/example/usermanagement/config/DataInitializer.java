// src/main/java/com/example/usermanagement/config/DataInitializer.java
package com.example.usermanagement.config;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // داده‌های تستی
        if (userRepository.count() == 0) {
            userRepository.save(new User("علی احمدی", 25, "تهران", "مهندس نرم‌افزار"));
            userRepository.save(new User("فاطمه محمدی", 30, "اصفهان", "معلم"));
            userRepository.save(new User("حسن رضایی", 28, "شیراز", "پزشک"));
            userRepository.save(new User("مریم صادقی", 35, "تبریز", "وکیل"));
            userRepository.save(new User("محمد کریمی", 22, "مشهد", "دانشجو"));
            userRepository.save(new User("زهرا نوری", 29, "کرج", "طراح"));
            userRepository.save(new User("رضا موسوی", 32, "اهواز", "مدیر"));
            userRepository.save(new User("سارا حسینی", 27, "رشت", "پرستار"));
            userRepository.save(new User("امیر علیزاده", 31, "یزد", "مکانیک"));
            userRepository.save(new User("نرگس قاسمی", 26, "قم", "حسابدار"));
            userRepository.save(new User("علیرضا طاهری", 33, "کرمان", "آشپز"));
            userRepository.save(new User("مینا باقری", 24, "همدان", "مترجم"));
            userRepository.save(new User("سعید جعفری", 36, "زنجان", "راننده"));
            userRepository.save(new User("لیلا محمودی", 23, "ساری", "فروشنده"));
            userRepository.save(new User("داود رستمی", 34, "بندرعباس", "نجار"));
        }
    }
}