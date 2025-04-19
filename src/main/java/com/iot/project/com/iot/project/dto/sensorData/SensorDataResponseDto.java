package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import lombok.Getter;

@Getter
public class SensorDataResponseDto {
    private ActionType action;
    private Long sensorId;
    private Instant timestamp;
    private List<SensorDataDetailDTO> details;

    public SensorDataResponseDto(SensorDataHeader header, ActionType actionType) {
        this.action = actionType;
        this.sensorId = header.getSensorId();
        this.timestamp = header.getTimestamp();
        this.details = header.getDetails().stream()
                .map(detail -> SensorDataDetailDTO.builder()
                        .metricName(detail.getMetric().getMetricName())
                        .value(detail.getValue())
                        .timestamp(detail.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }
}
