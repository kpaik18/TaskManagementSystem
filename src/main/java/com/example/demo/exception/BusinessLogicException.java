package com.example.demo.exception;

public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException() {
    }

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(Throwable cause) {
        super(cause);
    }
}
