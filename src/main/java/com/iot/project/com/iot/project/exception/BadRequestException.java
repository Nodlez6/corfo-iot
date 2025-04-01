package com.iot.project.com.iot.project.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    public final String code = "BAD_REQUEST";
    public BadRequestException(String message) {
        super(message);
    }
}
