package com.valuelabs.exam.exception;


import com.valuelabs.exam.dto.TrackingNumberResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<TrackingNumberResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Validation failed");
        
        TrackingNumberResponse errorResponse = new TrackingNumberResponse();
        errorResponse.setTrackingNumber("Error: " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<TrackingNumberResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        String errorMessage = "Missing required parameter: " + ex.getParameterName();
        
        TrackingNumberResponse errorResponse = new TrackingNumberResponse();
        errorResponse.setTrackingNumber("Error: " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}