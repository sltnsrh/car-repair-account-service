package com.salatin.account.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse (String message, String timeStamp, HttpStatus status) {}
