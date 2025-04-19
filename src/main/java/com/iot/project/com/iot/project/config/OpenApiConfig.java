package com.iot.project.com.iot.project.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(
    name = "ApiKeyAuth", // Internal name used in @SecurityRequirement
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization",
    description = "Company API key. Must be provided in the 'Authorization' header."
)
public class OpenApiConfig {
}