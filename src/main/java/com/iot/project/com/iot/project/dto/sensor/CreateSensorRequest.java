package com.iot.project.com.iot.project.dto.sensor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateSensorRequest {
    @NotBlank
    private String sensorName;
    @NotBlank
    private String sensorCategory;
    @NotBlank
    private String sensorMeta;
    @NotNull
    private Long locationId;
}
