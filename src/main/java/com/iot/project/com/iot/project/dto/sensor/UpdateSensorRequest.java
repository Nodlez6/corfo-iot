package com.iot.project.com.iot.project.dto.sensor;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateSensorRequest {
    @NotBlank
    private String sensorName;
    @NotBlank
    private String sensorCategory;
    @NotBlank
    private String sensorMeta;
    @NotBlank
    private Long locationId;
    @NotBlank
    private UUID sensorApiKey;
}
