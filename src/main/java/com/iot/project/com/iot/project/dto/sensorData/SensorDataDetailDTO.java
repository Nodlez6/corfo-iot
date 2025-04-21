package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataDetailDTO {
    private String metricName;
    private Object value;
    private Instant timestamp;
}
