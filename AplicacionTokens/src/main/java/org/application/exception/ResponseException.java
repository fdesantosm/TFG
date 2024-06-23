package org.application.exception;

import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException{
    private final HttpStatus status;
    protected final String description;

    public ResponseException(String description, HttpStatus status) {
        super(status.name());
        this.description = description;
        this.status = status;
    }

    public ResponseException(String description, String message, HttpStatus status) {
        super(message);
        this.description = description;
        this.status = status;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
