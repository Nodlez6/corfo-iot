package com.iot.project.com.iot.project.dto.sensor;

import java.util.Set;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorDataHeader;

import lombok.Getter;

@Getter
public class SensorResponseDto {

    private ActionType action;
    private Long sensorId;
    private Long locationId;
    private String name;
    private String category;
    private String meta;
    private UUID apiKey;
    private Set<SensorDataHeader> sensorData;

    public SensorResponseDto(Sensor sensor, ActionType actionType) {
        this.action = actionType;
        this.sensorId = sensor.getSensorId();
        this.locationId = sensor.getLocationId();
        this.name = sensor.getSensorName();
        this.category = sensor.getSensorCategory();
        this.meta = sensor.getSensorMeta();
        this.apiKey = sensor.getSensorApiKey();
        this.sensorData = sensor.getSensorData(); 
    }
}
