package com.bci.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

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
