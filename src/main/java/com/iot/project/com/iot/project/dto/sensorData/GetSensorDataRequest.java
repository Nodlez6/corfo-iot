package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class GetSensorDataRequest {
    @NotNull
    private Instant from;
    @NotNull
    private Instant to;
    @NotNull
    private List<Long> sensorIds;
}
