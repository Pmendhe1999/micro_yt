package com.quiz.exception;

public class InvalidPayloadException extends RuntimeException{
    public InvalidPayloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
