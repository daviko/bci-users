package com.bci.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import com.bci.user.exception.UserAlreadyExistsException;
import com.bci.user.model.User;
import com.bci.user.repository.UserRepository;
import com.bci.user.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private JwtTokenProvider jwtTokenProvider;

  @InjectMocks private UserServiceImpl userService;

  @Test
  @DisplayName("createUser should succeed when email is not registered")
  void testCreateUser_Success() {
    UserRequest userRequest = new UserRequest();
    userRequest.setName("Test User");
    userRequest.setEmail("test@example.com");
    userRequest.setPassword("a1Bcdefg23");

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
    when(jwtTokenProvider.generateToken(any())).thenReturn("jwtToken");
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.createUser(userRequest));

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName(
      "createUser should throw UserAlreadyExistsException when email is already registered")
  void testCreateUser_UserAlreadyExists() {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("existing@example.com");
    userRequest.setPassword("a1Bcdefg23");

    User existingUser = new User();
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(existingUser));

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
  }

  @Test
  @DisplayName("getUserByToken should return UserResponse when token is valid and user exists")
  void testGetUserByToken_ValidToken_UserFound() {
    String token = "validToken";
    String email = "john@example.com";

    User user = new User();
    user.setEmail(email);
    user.setName("John Doe");
    user.setPassword("encodedPass");
    user.setToken(token);

    when(jwtTokenProvider.validateToken(token)).thenReturn(true);
    when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(email);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(jwtTokenProvider.generateToken(email)).thenReturn("newToken");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Optional<UserResponse> result = userService.getUserByToken(token);

    assertTrue(result.isPresent());
    UserResponse response = result.get();
    assertEquals("John Doe", response.getName());
    assertEquals(email, response.getEmail());
    assertNotNull(user.getLastLogin());
    assertEquals("newToken", user.getToken());

    verify(userRepository).save(user);
  }

  @Test
  @DisplayName("getUserByToken should return empty when token is valid but user not found")
  void testGetUserByToken_ValidToken_UserNotFound() {
    String token = "validToken";
    String email = "notfound@example.com";

    when(jwtTokenProvider.validateToken(token)).thenReturn(true);
    when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(email);
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    Optional<UserResponse> result = userService.getUserByToken(token);

    assertTrue(result.isEmpty());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("getUserByToken should return empty when token is invalid")
  void testGetUserByToken_InvalidToken() {
    String token = "invalidToken";
    when(jwtTokenProvider.validateToken(token)).thenReturn(false);

    Optional<UserResponse> result = userService.getUserByToken(token);

    assertTrue(result.isEmpty());
    verify(userRepository, never()).findByEmail(any());
  }

  @Test
  @DisplayName("getUserByEmail should return UserResponse when user exists")
  void testGetUserByEmail_UserFound() {
    String email = "alice@example.com";
    User user = new User();
    user.setEmail(email);
    user.setName("Alice");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    Optional<UserResponse> result = userService.getUserByEmail(email);

    assertTrue(result.isPresent());
    assertEquals("Alice", result.get().getName());
    assertEquals(email, result.get().getEmail());
  }

  @Test
  @DisplayName("getUserByEmail should return empty when user not found")
  void testGetUserByEmail_UserNotFound() {
    String email = "ghost@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    Optional<UserResponse> result = userService.getUserByEmail(email);

    assertTrue(result.isEmpty());
  }
}
