package com.salatin.account.exception.handler;

import org.springframework.http.HttpStatus;

public record ExceptionResponse (String message, String timeStamp, HttpStatus status) {}
