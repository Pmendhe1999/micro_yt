package com.qc.exception;

public class JsonProcessingException extends RuntimeException{
    public JsonProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
