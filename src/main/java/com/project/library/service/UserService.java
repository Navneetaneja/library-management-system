package com.project.library.service;

import com.project.library.entity.User;
import com.project.library.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private static final String USER_CREATED = "User Created Successfully";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(User user) {
        user.setId(UUID.randomUUID());
        String email = user.getEmail().trim().toLowerCase();
        String name = user.getName().trim();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email Already Exists");
        }
        user.setEmail(email);
        user.setName(name);
        UUID userId = userRepository.save(user).getId();
        log.info("user created with id : {}", userId);
        return USER_CREATED;
    }
}
