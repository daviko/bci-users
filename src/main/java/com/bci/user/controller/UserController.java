package com.bci.user.controller;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import com.bci.user.service.UserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that manages user-related operations such as registration and login.
 * <p>
 * Provides endpoints under the base path {@code /api/v1}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Registers a new user in the system.
   * <p>
   * The request body must contain valid user data. On success, the newly created user
   * is returned with status {@code 201 Created}.
   * </p>
   *
   * @param userRequest the user information required to create a new account, validated with {@link Valid}
   * @return a {@link ResponseEntity} containing the created {@link UserResponse} and {@code HttpStatus.CREATED}
   */
  @PostMapping("/sign-up")
  public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

  /**
   * Authenticates a user based on a JWT token provided in the {@code Authorization} header.
   * <p>
   * The token may include the prefix {@code "Bearer "}, which will be stripped before validation.
   * If the token is valid, the corresponding user information is returned with {@code 200 OK}.
   * Otherwise, the response is {@code 401 Unauthorized}.
   * </p>
   *
   * @param token the JWT token from the {@code Authorization} header, optionally prefixed with {@code "Bearer "}
   * @return a {@link ResponseEntity} containing the authenticated {@link UserResponse} with {@code HttpStatus.OK},
   *         or an empty response with {@code HttpStatus.UNAUTHORIZED} if authentication fails
   */
  @GetMapping("/login")
  public ResponseEntity<UserResponse> login(@RequestHeader("Authorization") String token) {
    // Remove "Bearer " prefix if present
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }

    Optional<UserResponse> optionalUserResponse = userService.getUserByToken(token);
    return optionalUserResponse
        .map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
  }
}
