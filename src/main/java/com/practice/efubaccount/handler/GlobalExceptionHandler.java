package com.practice.efubaccount.handler;

import com.practice.efubaccount.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
//        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
//        return ResponseEntity.badRequest().body(error);
//    }

}