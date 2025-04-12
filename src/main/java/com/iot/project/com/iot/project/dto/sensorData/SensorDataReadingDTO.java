package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class SensorDataReadingDTO {
    @JsonProperty("datetime")
    private String datetime;

    private Map<String, Double> metrics = new HashMap<>();

    @JsonAnySetter
    public void setMetric(String key, Object value) {
        if (!"datetime".equals(key)) {
            metrics.put(key, Double.valueOf(value.toString()));
        }
    }
}
