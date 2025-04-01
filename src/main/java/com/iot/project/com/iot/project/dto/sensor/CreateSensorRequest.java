package com.iot.project.com.iot.project.dto.sensor;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateSensorRequest {
    @NotBlank
    private String sensorName;

    @NotBlank
    private String sensorCategory;

    private String sensorMeta;
}
