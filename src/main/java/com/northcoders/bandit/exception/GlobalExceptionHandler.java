package com.northcoders.bandit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}