package com.credit.credit.controller.error;

import com.credit.credit.dto.error.ErrorResponseDto;
import com.credit.credit.exception.ClientAlreadyExistsException;
import com.credit.credit.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException e){
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getStatusCode().value(), e.getReason(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleClientExists(ClientAlreadyExistsException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "Client already exists",
                ex.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage()
        );
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(
            Exception ex,
            HttpServletRequest request) {

        log.error("Непредвиденная ошибка произошла по адресу {}: {}",
                request.getRequestURI(),
                ex.getMessage(),
                ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred"
        );
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto> handleRuntimeExceptions(RuntimeException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                ex.getMessage()
        );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            HttpStatus status,
            String error,
            String message) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                status.value(),
                error,
                message
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
