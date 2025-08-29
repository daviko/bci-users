package com.bci.user.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(
      UserAlreadyExistsException ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.CONFLICT.value());
    errorResponse.put("detail", ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.NOT_FOUND.value());
    errorResponse.put("detail", ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("detail", "An unexpected error occurred");

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
