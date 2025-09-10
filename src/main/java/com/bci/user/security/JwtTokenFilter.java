package com.bci.user.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A servlet filter that processes incoming HTTP requests to extract and validate JWT tokens.
 * <p>
 * This filter ensures that authentication is performed once per request by extending
 * {@link OncePerRequestFilter}. If a valid token is found, the corresponding
 * {@link Authentication} object is stored in the {@link SecurityContextHolder}.
 * </p>
 */
public class JwtTokenFilter extends OncePerRequestFilter {

  private static final int BEGIN_INDEX = 7;

  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  /**
   * Filters each request to check for the presence of a JWT token in the {@code Authorization} header.
   * <p>
   * If a valid token is found, authentication information is retrieved from the token
   * and set into the {@link SecurityContextHolder}.
   * </p>
   *
   * @param request     the HTTP request being processed
   * @param response    the HTTP response being generated
   * @param filterChain the filter chain to pass the request/response to the next filter
   * @throws ServletException if an error occurs during filtering
   * @throws IOException      if an input/output error occurs
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = resolveToken(request);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication auth = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the JWT token from the {@code Authorization} header of the request.
   * <p>
   * If the header is missing or does not start with {@code "Bearer "}, this method returns {@code null}.
   * </p>
   *
   * @param request the HTTP request from which to extract the token
   * @return the JWT token string without the {@code "Bearer "} prefix, or {@code null} if not present
   */
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(BEGIN_INDEX);
    }
    return null;
  }
}
