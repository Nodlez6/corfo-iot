package com.iot.project.com.iot.project.dto.sensorData;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetSensorDataRequest {
    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private List<Long> sensorIds;
}
