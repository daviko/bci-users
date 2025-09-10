package com.bci.user.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * <p>
 * Centralizes the handling of exceptions across the application to provide
 * consistent error response structures. Each method handles a specific type of exception
 * and returns a {@link ResponseEntity} with an error payload containing:
 * <ul>
 *   <li><b>timestamp</b> – the time the error occurred</li>
 *   <li><b>codigo</b> – the corresponding HTTP status code</li>
 *   <li><b>detail</b> – details of the error (validation errors, messages, etc.)</li>
 * </ul>
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors thrown when request body validation fails.
   * <p>
   * Captures all field-specific validation messages and includes them in the response.
   * </p>
   *
   * @param ex the {@link MethodArgumentNotValidException} thrown when validation fails
   * @return a {@link ResponseEntity} with HTTP 400 (Bad Request) and details of validation errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("detail", errors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles cases where an attempt is made to create a user that already exists.
   *
   * @param ex the {@link UserAlreadyExistsException} thrown when a user with the same email exists
   * @return a {@link ResponseEntity} with HTTP 409 (Conflict) and error message
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(
      UserAlreadyExistsException ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.CONFLICT.value());
    errorResponse.put("detail", ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  /**
   * Handles cases where a requested user is not found.
   *
   * @param ex the {@link UserNotFoundException} thrown when a user is not found
   * @return a {@link ResponseEntity} with HTTP 404 (Not Found) and error message
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.NOT_FOUND.value());
    errorResponse.put("detail", ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles all uncaught or generic exceptions not specifically handled by other methods.
   *
   * @param ex the {@link Exception} thrown unexpectedly
   * @return a {@link ResponseEntity} with HTTP 500 (Internal Server Error) and generic error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("detail", "An unexpected error occurred");

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
