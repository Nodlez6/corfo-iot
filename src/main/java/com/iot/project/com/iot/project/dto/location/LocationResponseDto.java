package com.iot.project.com.iot.project.dto.location;

import java.util.Set;

import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.entity.Sensor;

import lombok.Getter;

@Getter
public class LocationResponseDto {
    private ActionType action;
    private Long locationId;
    private Long companyId;
    private String name;
    private String country;
    private String city;
    private String address;
    private String meta;
    private Set<Sensor> sensors;
    

    public LocationResponseDto(Location location, ActionType actionType) {
        this.action = actionType;
        this.locationId = location.getLocationId();
        this.companyId = location.getCompanyId();
        this.name = location.getLocationName();
        this.country = location.getLocationCountry();
        this.city = location.getLocationCity();
        this.address = location.getLocationAddress();
        this.meta = location.getLocationMeta();
        this.sensors = location.getSensors();
    }
}
