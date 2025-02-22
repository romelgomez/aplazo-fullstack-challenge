package com.aplazo.bnpl.exception;

import com.aplazo.bnpl.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(
      CustomerNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.CUSTOMER_NOT_FOUND,
        ex.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(LoanNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLoanNotFoundException(
      LoanNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.LOAN_NOT_FOUND,
        ex.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(InvalidCustomerRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCustomerRequestException(
      InvalidCustomerRequestException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.INVALID_CUSTOMER_REQUEST,
        ex.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(InvalidLoanRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidLoanRequestException(
      InvalidLoanRequestException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.INVALID_LOAN_REQUEST,
        ex.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.UNAUTHORIZED,
        "Unauthorized access",
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining(", "));

    ErrorResponse error = buildErrorResponse(
        ErrorCode.INVALID_REQUEST,
        message,
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.INVALID_REQUEST,
        ex.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      Exception ex,
      HttpServletRequest request) {
    ErrorResponse error = buildErrorResponse(
        ErrorCode.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred",
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  private ErrorResponse buildErrorResponse(ErrorCode errorCode, String message, String path) {
    return ErrorResponse.builder()
        .code(errorCode.getCode())
        .error(errorCode.getError())
        .timestamp(Instant.now().getEpochSecond())
        .message(message)
        .path(path)
        .build();
  }
}