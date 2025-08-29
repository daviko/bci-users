package com.bci.user.controller;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import com.bci.user.service.UserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

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
