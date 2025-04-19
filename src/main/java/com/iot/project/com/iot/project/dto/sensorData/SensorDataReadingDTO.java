package com.iot.project.com.iot.project.dto.sensorData;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataReadingDTO {
    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty
    private Map<String, Object> metrics;

    @JsonAnySetter
    public void setMetric(String key, Object value) {
        if( metrics == null ){
            metrics = new HashMap<>();
        }

        if (!"datetime".equals(key)) {
            // metrics.put(key, Double.valueOf(value.toString()));
            // metrics.put(key, value.toString());
            // String valueToStore;

            try {
                metrics.put(key, Double.valueOf(value.toString()));
            } catch (Exception e) {
                metrics.put(key, value.toString());
            }
        }
    }
}