package com.bci.user.dto;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  private String name;

  @NotBlank(message = "Email is mandatory")
  @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email format is invalid")
  private String email;

  @NotBlank(message = "Password is mandatory")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=(?:.*\\d){2})[a-zA-Z\\d]{8,12}$",
      message =
          "Password must have exactly one uppercase letter, exactly two numbers, and be 8-12 characters long")
  private String password;

  private List<PhoneRequest> phones;
}
