package com.iot.project.com.iot.project.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
    public final String code = "UNAUTHORIZED";
    public UnauthorizedException(String message) {
        super(message);
    }
}
