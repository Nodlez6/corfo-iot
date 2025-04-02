package com.iot.project.com.iot.project.dto.sensorData;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateSensorDataRequest {
    @NotNull
    @JsonProperty("api_key")
    private UUID apiKey;
    @NotNull
    @JsonProperty("json_data")
    private List<SensorDataReadingDTO> jsonData;
}
