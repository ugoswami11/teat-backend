package com.teat.teat_backend.exception;

import com.teat.teat_backend.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(ResourceNotFoundException ex,
                                                                    HttpServletRequest request){
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    public ResponseEntity<ApiErrorResponse> handleBusinessValidationException(BusinessValidationException ex,
                                                                              HttpServletRequest request){
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Business Validation Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                   HttpServletRequest request){
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                errorMessage,
                request.getRequestURI()
        );
    }

    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex,
                                                                   HttpServletRequest request){
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path
    ){
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        return ResponseEntity.status(status).body(response);
    }

}
