package com.bci.user.service;

import com.bci.user.dto.UserResponse;
import com.bci.user.model.User;

/**
 * Utility class for mapping between {@link User} entities and {@link UserResponse} DTOs.
 * <p>
 * Provides static methods to convert domain objects into API response objects.
 * This class is stateless and should be used only for object transformation.
 * </p>
 */
public class UserMapper {

  /**
   * Converts a {@link User} entity into a {@link UserResponse} DTO.
   * <p>
   * The method maps basic user attributes (ID, timestamps, authentication data, and personal details)
   * as well as the list of associated phones using {@link PhoneMapper}.
   * </p>
   *
   * @param user the {@link User} entity to convert
   * @return a {@link UserResponse} containing the mapped user data
   */
  public static UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getCreated(),
        user.getLastLogin(),
        user.getToken(),
        user.getIsActive(),
        user.getName(),
        user.getEmail(),
        user.getPassword(),
        PhoneMapper.toResponses(user.getPhones()));
  }
}
