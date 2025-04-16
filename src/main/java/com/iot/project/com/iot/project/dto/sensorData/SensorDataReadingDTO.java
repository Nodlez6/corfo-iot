package com.iot.project.com.iot.project.dto.sensorData;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataReadingDTO {
    @JsonProperty("datetime")
    private String datetime;

    private Map<String, Double> metrics;

    @JsonAnySetter
    public void setMetric(String key, Object value) {
        if( metrics == null ){
            metrics = new HashMap<>();
        }

        if (!"datetime".equals(key)) {
            metrics.put(key, Double.valueOf(value.toString()));
        }
    }
}
