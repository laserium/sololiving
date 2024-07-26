package com.sololiving.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.error.ErrorResponse;
import com.sololiving.global.exception.success.SuccessException;
import com.sololiving.global.exception.success.SuccessResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponse> handleErrorException(ErrorException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().code(ex.getErrorCode().getCode()).message(ex.getErrorCode().getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<SuccessResponse> handleSuccessException(SuccessException ex) {
        SuccessResponse successResponse = SuccessResponse.builder().code(ex.getSuccessCode().getCode()).message(ex.getSuccessCode().getMessage()).build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
