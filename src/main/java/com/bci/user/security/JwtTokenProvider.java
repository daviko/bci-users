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

  public String getUsernameFromToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      log.error("An exception has occurred validating token", ex);
      return false;
    }
  }

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
