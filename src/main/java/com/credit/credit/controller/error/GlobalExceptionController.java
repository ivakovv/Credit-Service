package com.credit.credit.controller.error;

import com.credit.credit.dto.error.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException e){
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getStatusCode().value(), e.getReason(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }
}
