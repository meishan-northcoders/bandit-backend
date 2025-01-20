package com.northcoders.bandit.exception;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Exception interceptor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDTOException.class)
    public ResponseEntity<ApiException> handleInvalidDTOException(InvalidDTOException e){
        String title = "Invalid DTO";
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        ApiException apiException = new ApiException(httpStatus, title, e);

        return new ResponseEntity<ApiException>(apiException, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ApiException apiException = new ApiException();
        apiException.setTitle("Invalid Request");
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        apiException.setDetail(errors.toString());
        apiException.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ApiException>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiException> handleDuplicateProfileException(EntityExistsException e){
        String title = "Existing Profile";
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(httpStatus, title, e);
        return new ResponseEntity<ApiException>(apiException, HttpStatus.CONFLICT);
    }

}