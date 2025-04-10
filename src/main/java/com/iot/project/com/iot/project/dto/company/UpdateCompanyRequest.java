package com.iot.project.com.iot.project.dto.company;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateCompanyRequest {
    @NotBlank
    private String companyName;
    @NotNull
    private UUID apiKey;
    @NotBlank
    private String adminUsername;
    @NotBlank
    private String adminPassword;
}
