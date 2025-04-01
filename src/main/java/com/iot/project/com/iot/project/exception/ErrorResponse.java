package com.iot.project.com.iot.project.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private String code;
    private String message;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
