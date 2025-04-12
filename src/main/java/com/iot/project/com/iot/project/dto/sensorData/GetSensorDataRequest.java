package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetSensorDataRequest {
    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private List<Long> sensorIds;
}
