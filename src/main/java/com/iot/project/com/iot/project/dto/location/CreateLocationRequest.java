package com.iot.project.com.iot.project.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateLocationRequest {
    @NotBlank
    private String locationName;
    @NotBlank
    private String locationAddress;
    @NotBlank
    private String locationCountry;
    @NotBlank
    private String locationCity;
    private String locationMeta;
}
