package com.example.demo.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleSecurityViolation(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, HttpHeaders.EMPTY, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = BusinessLogicException.class)
    public ResponseEntity<Object> handleBusiness(RuntimeException ex, WebRequest request) {
        String body = null;
        if (ex.getMessage() != null) {
            body = new ObjectMapper().createObjectNode().put("errorCode", ex.getMessage()).toString();
        }

        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handle() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMultipartException(MultipartException e, WebRequest request) {
        return handleExceptionInternal(e, e.getCause().getMessage(), HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
    }
}
