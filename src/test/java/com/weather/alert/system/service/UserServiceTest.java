package com.weather.alert.system.service;

import com.weather.alert.system.dto.UserRegistrationDto;
import com.weather.alert.system.model.AlertType;
import com.weather.alert.system.model.User;
import com.weather.alert.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto userRegistrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRegistrationDto = UserRegistrationDto.builder()
                .email("test@example.com")
                .name("Test User")
                .phoneNumber("+1234567890")
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .alertPreferences(Set.of(AlertType.SEVERE_WEATHER, AlertType.STORM))
                .locations(List.of("New York", "London"))
                .build();

        user = User.builder()
                .id("1")
                .email("test@example.com")
                .name("Test User")
                .phoneNumber("+1234567890")
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .alertPreferences(Set.of(AlertType.SEVERE_WEATHER, AlertType.STORM))
                .locations(List.of("New York", "London"))
                .build();
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.registerUser(userRegistrationDto);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getName());
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> userService.registerUser(userRegistrationDto));
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByEmail_UserExists_ReturnsUser() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userService.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_UserNotExists_ReturnsEmpty() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void updateUser_Success() {
        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserRegistrationDto updateDto = UserRegistrationDto.builder()
                .name("Updated Name")
                .phoneNumber("+9876543210")
                .emailNotificationsEnabled(false)
                .smsNotificationsEnabled(false)
                .alertPreferences(Set.of(AlertType.HEATWAVE))
                .locations(List.of("Paris"))
                .build();

        // When
        User result = userService.updateUser("1", updateDto);

        // Then
        assertNotNull(result);
        verify(userRepository).findById("1");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> userService.updateUser("999", userRegistrationDto));
        verify(userRepository).findById("999");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        // Given
        when(userRepository.existsById(anyString())).thenReturn(true);

        // When
        userService.deleteUser("1");

        // Then
        verify(userRepository).existsById("1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.existsById(anyString())).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> userService.deleteUser("999"));
        verify(userRepository).existsById("999");
        verify(userRepository, never()).deleteById(anyString());
    }
}
