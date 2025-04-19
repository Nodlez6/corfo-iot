package com.iot.project.com.iot.project.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    public final String status = "NOT_FOUND";
    public NotFoundException(String message) {
        super(message);
    }
}
