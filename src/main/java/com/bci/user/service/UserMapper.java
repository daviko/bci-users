package com.bci.user.service;

import com.bci.user.dto.UserResponse;
import com.bci.user.model.User;

public class UserMapper {

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
