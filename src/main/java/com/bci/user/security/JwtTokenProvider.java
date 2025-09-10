package com.bci.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility component responsible for generating, parsing, and validating JWT tokens.
 * <p>
 * This provider is used by authentication filters and services to securely manage
 * tokens for user sessions. It also generates {@link Authentication} objects
 * that can be stored in the Spring Security context.
 * </p>
 */
@Component
@Slf4j
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long jwtExpiration;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long jwtExpiration) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.jwtExpiration = jwtExpiration;
  }

  /**
   * Generates a JWT token for the given user email.
   *
   * @param email the email address of the authenticated user
   * @return a signed JWT token string containing the user email as its subject
   */
  public String generateToken(String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Extracts the username (email) from the provided JWT token.
   *
   * @param token the JWT token to parse
   * @return the subject (username/email) embedded in the token
   */
  public String getUsernameFromToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  /**
   * Validates the provided JWT token.
   * <p>
   * Checks that the token is correctly signed and not expired.
   * </p>
   *
   * @param token the JWT token string
   * @return {@code true} if the token is valid, {@code false} otherwise
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      log.error("An exception has occurred validating token", ex);
      return false;
    }
  }

  /**
   * Builds an {@link Authentication} object from the provided JWT token.
   * <p>
   * The username is extracted from the token, and a {@link UserDetails} instance is created
   * with no password and no authorities. This is sufficient to represent an authenticated user
   * for stateless JWT-based authentication.
   * </p>
   *
   * @param token the JWT token to extract user information from
   * @return an {@link Authentication} object populated with the token's subject
   */
  public Authentication getAuthentication(String token) {
    String username = getUsernameFromToken(token);

    UserDetails userDetails =
        User.builder()
            .username(username)
            .password("") // Password is not needed for authentication here
            .authorities(Collections.emptyList())
            .build();

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}
