package com.iot.project.com.iot.project.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
