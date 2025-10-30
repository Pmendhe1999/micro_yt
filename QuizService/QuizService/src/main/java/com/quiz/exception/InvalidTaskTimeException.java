package com.quiz.exception;

public class InvalidTaskTimeException extends RuntimeException{
    public InvalidTaskTimeException(String message) {
        super(message);
    }
}
