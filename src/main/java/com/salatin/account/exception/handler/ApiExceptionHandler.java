package com.salatin.account.exception.handler;

import com.salatin.account.exception.UserAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ExceptionResponse> conflictHandle(RuntimeException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(getApiExceptionObject(e.getMessage(), status), status);
    }

    private ExceptionResponse getApiExceptionObject(String message, HttpStatus status) {

        return new ExceptionResponse(
            message,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)),
            status
        );
    }
}
