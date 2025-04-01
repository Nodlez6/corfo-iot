package com.iot.project.com.iot.project.service;

import java.util.List;
import java.util.Optional;

import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
import com.iot.project.com.iot.project.dto.sensor.UpdateSensorRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.LocationRepository;
import com.iot.project.com.iot.project.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.ENTITY_NOT_FOUND_BY_COMPANY;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final LocationRepository locationRepository;

    public List<Sensor> getAllSensors(Long companyId) {
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        List<Long> locationIds = locations.stream().map(Location::getLocationId).toList();
        return sensorRepository.findAllByLocationIdIn(locationIds);
    }

    public Sensor getSensorById(Long id, Long companyId) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        boolean anyMatch = locations.stream().anyMatch(location -> location.getLocationId().equals(sensor.getLocationId()));
        if (!anyMatch) {
            throw new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY);
        }
        return sensor;
    }

    public Sensor createSensor(CreateSensorRequest request, Long companyId) {
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        boolean anyMatch = locations.stream().anyMatch(location -> location.getLocationId().equals(request.getLocationId()));
        if (!anyMatch) {
            throw new NotFoundException("Location id " + ENTITY_NOT_FOUND_BY_COMPANY);
        }
        Sensor sensor = Sensor.builder()
                .sensorName(request.getSensorName())
                .sensorCategory(request.getSensorCategory())
                .sensorMeta(request.getSensorMeta())
                .locationId(request.getLocationId())
                .build();
        return sensorRepository.save(sensor);
    }

    public Sensor updateSensor(Long id, UpdateSensorRequest request, Long companyId) {
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        List<Long> locationIds = locations.stream().map(Location::getLocationId).toList();
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        if (!locationIds.contains(sensor.getLocationId())){
            throw new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY);
        }
        sensor.setLocationId(request.getLocationId());
        sensor.setSensorApiKey(request.getSensorApiKey());
        sensor.setSensorCategory(request.getSensorCategory());
        sensor.setSensorName(request.getSensorName());
        sensor.setSensorMeta(request.getSensorMeta());

        return sensorRepository.save(sensor);
    }

    public void deleteSensor(Long id, Long companyId) {
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        List<Long> locationIds = locations.stream().map(Location::getLocationId).toList();
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
        if (!locationIds.contains(sensor.getLocationId())){
            throw new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY);
        }
        sensorRepository.delete(sensor);
    }
}
