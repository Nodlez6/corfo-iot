package com.iot.project.com.iot.project.exception;

import lombok.Getter;

@Getter
public class ApiKeyAlreadyExistsException extends RuntimeException{
    public final String status = "API_KEY_ALREADY_EXISTS";

    public ApiKeyAlreadyExistsException(String message) {
        super(message);
    }
}
