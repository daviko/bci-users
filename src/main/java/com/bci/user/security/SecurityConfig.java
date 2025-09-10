package com.bci.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class.
 * <p>
 * Defines authentication and authorization rules for the application, including
 * which endpoints are publicly accessible and which require authentication.
 * This configuration also integrates JWT-based authentication by adding
 * a {@link JwtTokenFilter} before the {@link UsernamePasswordAuthenticationFilter}.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  /**
   * Defines the password encoder bean to be used by Spring Security.
   * <p>
   * The application uses {@link BCryptPasswordEncoder}, which provides a secure
   * hashing algorithm for storing user passwords.
   * </p>
   *
   * @return a {@link PasswordEncoder} based on BCrypt
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures HTTP security rules for the application.
   * <ul>
   *   <li>Disables CORS and CSRF protection for simplicity in APIs.</li>
   *   <li>Allows public access to {@code /api/v1/sign-up} for user registration.</li>
   *   <li>Allows access to {@code /h2-console/**} for database console usage.</li>
   *   <li>Requires authentication for all other endpoints.</li>
   *   <li>Registers a {@link JwtTokenFilter} before {@link UsernamePasswordAuthenticationFilter} to
   *   enable JWT-based authentication.</li>
   *   <li>Disables frame options to support the H2 console UI.</li>
   * </ul>
   *
   * @param http the {@link HttpSecurity} object used to configure security rules
   * @throws Exception if an error occurs during configuration
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api/v1/sign-up")
        .permitAll() // Allow sign-up without authentication
        .antMatchers("/h2-console/**")
        .permitAll() // Allow H2 console
        .anyRequest()
        .authenticated() // All other endpoints require authentication
        .and()
        .addFilterBefore(
            new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        .headers()
        .frameOptions()
        .disable(); // For H2 console
  }
}
