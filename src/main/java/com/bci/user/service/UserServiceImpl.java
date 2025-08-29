package com.bci.user.service;

import com.bci.user.dto.UserRequest;
import com.bci.user.dto.UserResponse;
import com.bci.user.exception.UserAlreadyExistsException;
import com.bci.user.model.User;
import com.bci.user.repository.UserRepository;
import com.bci.user.security.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public UserServiceImpl(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  @Transactional
  public UserResponse createUser(UserRequest userRequest) {
    // Check if user already exists
    if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException(
          "User with email " + userRequest.getEmail() + " already exists");
    }

    // Create new user
    User user = new User();
    user.setName(userRequest.getName());
    user.setEmail(userRequest.getEmail());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

    // Set phones if provided
    if (userRequest.getPhones() != null) {
      user.setPhones(PhoneMapper.toEntities(userRequest.getPhones()));
    }

    // Generate token
    String token = jwtTokenProvider.generateToken(userRequest.getEmail());
    user.setToken(token);
    user.setLastLogin(LocalDateTime.now());

    // Save user
    User savedUser = userRepository.save(user);

    return UserMapper.toResponse(savedUser);
  }

  @Override
  @Transactional
  public Optional<UserResponse> getUserByToken(String token) {
    if (jwtTokenProvider.validateToken(token)) {
      String email = jwtTokenProvider.getUsernameFromToken(token);
      Optional<User> user = userRepository.findByEmail(email);

      if (user.isPresent()) {
        // Update last login and token
        User foundUser = user.get();
        String newToken = jwtTokenProvider.generateToken(email);
        foundUser.setToken(newToken);
        foundUser.setLastLogin(LocalDateTime.now());
        userRepository.save(foundUser);

        return Optional.of(UserMapper.toResponse(foundUser));
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<UserResponse> getUserByEmail(String email) {
    return userRepository.findByEmail(email).map(UserMapper::toResponse);
  }
}
