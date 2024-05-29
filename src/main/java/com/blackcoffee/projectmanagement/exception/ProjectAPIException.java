package com.blackcoffee.projectmanagement.exception;

import org.springframework.http.HttpStatus;

public class ProjectAPIException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;

    public ProjectAPIException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ProjectAPIException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
