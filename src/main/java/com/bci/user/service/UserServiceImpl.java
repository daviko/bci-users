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

/**
 * Implementation of the {@link UserService} interface.
 * <p>
 * Provides business logic for user management, including:
 * <ul>
 *   <li>Creating new users with encoded passwords and JWT tokens.</li>
 *   <li>Fetching users by JWT token with token refresh and login tracking.</li>
 *   <li>Retrieving users by email.</li>
 * </ul>
 * </p>
 */
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

  /**
   * Creates a new user account.
   * <p>
   * Steps performed:
   * <ol>
   *   <li>Checks if a user with the given email already exists, throwing
   *   {@link UserAlreadyExistsException} if found.</li>
   *   <li>Encodes the password using {@link PasswordEncoder}.</li>
   *   <li>Maps any provided phones using {@link PhoneMapper}.</li>
   *   <li>Generates a JWT token for the user.</li>
   *   <li>Saves the new user in the database and returns a {@link UserResponse}.</li>
   * </ol>
   * </p>
   *
   * @param userRequest the user registration details
   * @return a {@link UserResponse} with the saved user information
   * @throws UserAlreadyExistsException if a user with the given email already exists
   */
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

  /**
   * Retrieves a user by validating and parsing a JWT token.
   * <p>
   * Steps performed:
   * <ol>
   *   <li>Validates the token using {@link JwtTokenProvider}.</li>
   *   <li>Extracts the email and retrieves the corresponding user.</li>
   *   <li>If found, generates a new token, updates the user's last login, and saves changes.</li>
   *   <li>Returns the updated user as a {@link UserResponse} wrapped in {@link Optional}.</li>
   * </ol>
   * </p>
   *
   * @param token the JWT token string
   * @return an {@link Optional} containing the {@link UserResponse} if the token is valid and user is found,
   *         or empty otherwise
   */
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

  /**
   * Retrieves a user by email address.
   *
   * @param email the email of the user to search for
   * @return an {@link Optional} containing the {@link UserResponse} if found, or empty otherwise
   */
  @Override
  public Optional<UserResponse> getUserByEmail(String email) {
    return userRepository.findByEmail(email).map(UserMapper::toResponse);
  }
}
