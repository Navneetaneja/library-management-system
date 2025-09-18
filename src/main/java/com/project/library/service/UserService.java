package com.project.library.service;

import com.project.library.entity.User;
import com.project.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(User user) {
        if (isUserDataInValid(user)) {
            throw new RuntimeException("Invalid User Data");
        }
        String email = user.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email Already Exists");
        }
        return userRepository.save(user).getId();
    }

    private boolean isUserDataInValid(User user) {
        return null == user.getEmail() || user.getEmail().isEmpty()
                || null == user.getName() || user.getName().isEmpty();
    }
}
