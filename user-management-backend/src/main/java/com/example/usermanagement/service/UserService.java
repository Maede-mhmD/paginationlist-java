package com.example.usermanagement.service;

import com.example.usermanagement.dto.PageResponse;
import com.example.usermanagement.dto.UserRequest;
import com.example.usermanagement.dto.UserStatusRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        User user = new User(
            userRequest.getName(),
            userRequest.getAge(),
            userRequest.getCity(),
            userRequest.getJob()
        );
        return userRepository.save(user);
    }

    public PageResponse<User> getUsers(int page, int perPage, String name, String city, String job, Integer age) {
        // Convert empty strings to null for query
        String nameFilter = (name != null && name.trim().isEmpty()) ? null : name;
        String cityFilter = (city != null && city.trim().isEmpty()) ? null : city;
        String jobFilter = (job != null && job.trim().isEmpty()) ? null : job;
        
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> userPage = userRepository.findUsersWithFilters(nameFilter, cityFilter, jobFilter, age, pageable);
        
        return new PageResponse<>(
            userPage.getContent(),
            userPage.getTotalElements(),
            userPage.getTotalPages(),
            page,
            perPage
        );
    }

    public User updateUserStatus(Long id, UserStatusRequest statusRequest) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("کاربر با شناسه " + id + " یافت نشد"));
        
        user.setIsActive(statusRequest.getIsActive());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("کاربر با شناسه " + id + " یافت نشد"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("کاربر با شناسه " + id + " یافت نشد");
        }
        userRepository.deleteById(id);
    }
}
