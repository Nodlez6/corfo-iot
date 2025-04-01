package com.iot.project.com.iot.project.service;

import java.util.List;
import java.util.Optional;

import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
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

//    public Sensor updateSensor(Long id, UpdateSensorRequest request, Long companyId) {
//
//    }

    public void deleteSensor(Long id, Long companyId) {
        List<Location> locations = locationRepository.findAllByCompanyId(companyId);
        List<Long> locationIds = locations.stream().map(Location::getLocationId).toList();
        List<Sensor> sensors = sensorRepository.findAllByLocationIdIn(locationIds);
        Optional<Sensor> sensorOpt = sensors.stream().filter(sensor -> sensor.getSensorId().equals(id)).findFirst();

        if (sensorOpt.isPresent()) {
            sensorRepository.delete(sensorOpt.get());
        } else {
            throw new NotFoundException("Sensor with id " + id + ENTITY_NOT_FOUND_BY_COMPANY);
        }

    }
}
