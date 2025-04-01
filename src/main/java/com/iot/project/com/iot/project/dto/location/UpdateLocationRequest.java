package com.iot.project.com.iot.project.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateLocationRequest {
    @NotBlank
    private String locationName;
    @NotBlank
    private String locationAddress;
    @NotBlank
    private String locationCountry;
    @NotBlank
    private String locationCity;
    @NotBlank
    private String locationMeta;

}
