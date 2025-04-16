package com.iot.project.com.iot.project.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCompanyRequest {
    @NotBlank
    private String companyName;
    @NotBlank
    private String adminUsername;
    @NotBlank
    private String adminPassword;
}
