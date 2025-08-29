package com.bci.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import com.bci.user.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  @Test
  @DisplayName("signUp should return 201 CREATED with UserResponse when user is created")
  void testSignUpSuccess() {
    UserRequest request = new UserRequest();
    request.setName("John Doe");
    request.setEmail("john@example.com");
    request.setPassword("password123");

    UserResponse expectedResponse =
        UserResponse.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .email("john@example.com")
            .password("password123")
            .build();

    when(userService.createUser(request)).thenReturn(expectedResponse);

    ResponseEntity<UserResponse> response = userController.signUp(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
    verify(userService, times(1)).createUser(request);
  }

  @Test
  @DisplayName("login should return 200 OK when token is valid")
  void testLoginValidToken() {
    String token = "Bearer validToken123";

    UserResponse expectedResponse =
        UserResponse.builder()
            .id(UUID.randomUUID())
            .name("Jane Doe")
            .email("jane@example.com")
            .password("password123")
            .token("validToken123")
            .build();

    when(userService.getUserByToken("validToken123")).thenReturn(Optional.of(expectedResponse));

    ResponseEntity<UserResponse> response = userController.login(token);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
    verify(userService, times(1)).getUserByToken("validToken123");
  }

  @Test
  @DisplayName("login should return 200 OK when token without 'Bearer ' prefix is valid")
  void testLoginWithoutBearerPrefix() {
    String token = "validToken123";

    UserResponse expectedResponse =
        UserResponse.builder()
            .id(UUID.randomUUID())
            .name("Alice")
            .email("alice@example.com")
            .password("password123")
            .build();

    when(userService.getUserByToken("validToken123")).thenReturn(Optional.of(expectedResponse));

    ResponseEntity<UserResponse> response = userController.login(token);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
    verify(userService, times(1)).getUserByToken("validToken123");
  }

  @Test
  @DisplayName("login should return 401 UNAUTHORIZED when token is invalid")
  void testLoginInvalidToken() {
    String token = "Bearer invalidToken";
    when(userService.getUserByToken("invalidToken")).thenReturn(Optional.empty());

    ResponseEntity<UserResponse> response = userController.login(token);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    verify(userService, times(1)).getUserByToken("invalidToken");
  }
}
