package com.bci.user.service;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import java.util.Optional;

public interface UserService {

  UserResponse createUser(UserRequest userRequest);

  Optional<UserResponse> getUserByToken(String token);

  Optional<UserResponse> getUserByEmail(String email);
}
