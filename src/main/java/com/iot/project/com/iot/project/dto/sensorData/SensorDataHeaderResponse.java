package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataHeaderResponse {
    private Long sensorId;
    private Long sensorDataHeaderId;
    private Instant timestamp;
    private List<SensorDataDetailDTO> details;
}
