package com.weather.alert.system.service;

import com.weather.alert.system.dto.UserRegistrationDto;
import com.weather.alert.system.model.User;
import com.weather.alert.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + registrationDto.getEmail() + " already exists");
        }

        User user = User.builder()
                .email(registrationDto.getEmail())
                .name(registrationDto.getName())
                .phoneNumber(registrationDto.getPhoneNumber())
                .emailNotificationsEnabled(registrationDto.isEmailNotificationsEnabled())
                .smsNotificationsEnabled(registrationDto.isSmsNotificationsEnabled())
                .alertPreferences(registrationDto.getAlertPreferences())
                .locations(registrationDto.getLocations())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public User updateUser(String id, UserRegistrationDto updateDto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        User user = existingUser.get();
        user.setName(updateDto.getName());
        user.setPhoneNumber(updateDto.getPhoneNumber());
        user.setEmailNotificationsEnabled(updateDto.isEmailNotificationsEnabled());
        user.setSmsNotificationsEnabled(updateDto.isSmsNotificationsEnabled());
        user.setAlertPreferences(updateDto.getAlertPreferences());
        user.setLocations(updateDto.getLocations());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getEmail());
        return updatedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }
}
