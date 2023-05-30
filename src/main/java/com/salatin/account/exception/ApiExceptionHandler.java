package com.salatin.account.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleBindingBadRequestException(
        MethodArgumentNotValidException e
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessages = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return new ResponseEntity<>(getApiExceptionObject(errorMessages, status), status);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(
        IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
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
