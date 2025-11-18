package com.qc.services;

import com.qc.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    /**
     * Success response with data
     */
    public <T> ResponseEntity<Response<T>> success(int statusCode, String message, T data, long count) {
        return ResponseEntity.status(statusCode).body(new Response<>("Success", statusCode, message, data, count));
    }

    /**
     * Failure response with message and optional data
     */
    public <T> ResponseEntity<Response<T>> failure(int statusCode, String message, T data) {
        return ResponseEntity.status(statusCode).body(new Response<>("Failure", statusCode, message, data, 0));
    }

    public <T> ResponseEntity<Response<T>> exceptionFailure(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new Response<>("Failure", status.value(), message, data, 0));
    }
}
