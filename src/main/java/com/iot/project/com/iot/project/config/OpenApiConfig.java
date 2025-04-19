package com.iot.project.com.iot.project.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "IoT Service",
        version = "1.0",
        description = "This API is part of an IoT Device Management system that allows companies to securely register and manage their sensors, locations, and collected data.\n\n" +
                      "Key features include:\n" +
                      "- Company registration and API key-based authentication\n" +
                      "- Admin role-based access control\n" +
                      "- Sensor and location management\n" +
                      "- Ingestion and retrieval of sensor data, with filtering by time range\n" +
                      "- Password encoding and authentication utilities for secure admin login\n"
    )
)
@SecurityScheme(
    name = "ApiKeyAuth", // Internal name used in @SecurityRequirement
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization",
    description = "Company API key. Must be provided in the 'Authorization' header."
)
public class OpenApiConfig {
}