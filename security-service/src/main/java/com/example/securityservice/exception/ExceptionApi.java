package com.example.securityservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionApi {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(getErrorMap(errors));
    }

    private Map<String, List<String>> getErrorMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("Errors in the provided data: ", errors);
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionData> handleException(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errorMessages = new ArrayList<>();

        if (exception instanceof AuthException) {
            status = HttpStatus.FORBIDDEN;
            errorMessages.add("Authentication error: " + exception.getMessage());
        }

        if (exception instanceof EmailValidationException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessages.add(exception.getMessage());
        }

        if (exception instanceof PasswordValidationException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessages.add(exception.getMessage());
        }
        return ResponseEntity.status(status)
                .body(new ExceptionData(String.join(", ", errorMessages)));
    }

}
