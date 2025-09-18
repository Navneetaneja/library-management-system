package com.project.library.service;

import com.project.library.entity.User;
import com.project.library.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(User user) {
        String email = null == user.getEmail()
                ? null : user.getEmail().trim().toLowerCase();
        String name = null == user.getName() ? null : user.getName().trim();
        if (isUserDataInValid(email, name)) {
            throw new RuntimeException("Invalid User Data");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("Invalid Email Format");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email Already Exists");
        }
        user.setEmail(email);
        user.setName(name);
        UUID userId = userRepository.save(user).getId();
        log.info("user created with id : {}", userId);
        return userId;
    }

    private boolean isUserDataInValid(String email, String name) {
        return null == email || email.isEmpty() || null == name || name.isEmpty();
    }
}
