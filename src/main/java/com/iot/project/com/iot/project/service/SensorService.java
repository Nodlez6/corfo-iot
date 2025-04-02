package com.iot.project.com.iot.project.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return sensorRepository.findAllByCompanyId(companyId);
    }

    public Sensor getSensorById(Long sensorId, Long companyId) {
        return sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId)
                .orElseThrow(() -> new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY));
    }

    public Sensor createSensor(CreateSensorRequest request, Long companyId) {
        Location location = locationRepository.findByLocationIdAndCompanyId(request.getLocationId(), companyId)
                .orElseThrow(() -> new NotFoundException("Location id " + ENTITY_NOT_FOUND_BY_COMPANY));

        Sensor sensor = Sensor.builder()
                .sensorName(request.getSensorName())
                .sensorCategory(request.getSensorCategory())
                .sensorMeta(request.getSensorMeta())
                .locationId(location.getLocationId())
                .build();
        return sensorRepository.save(sensor);
    }

    public Sensor updateSensor(Long id, UpdateSensorRequest request, Long companyId) {
        Sensor sensor = sensorRepository.findBySensorIdAndLocationIdAndCompanyId(
                        id, request.getLocationId(), companyId)
                .orElseThrow(() -> new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY));

        sensor.setSensorApiKey(request.getSensorApiKey());
        sensor.setSensorCategory(request.getSensorCategory());
        sensor.setSensorName(request.getSensorName());
        sensor.setSensorMeta(request.getSensorMeta());

        return sensorRepository.save(sensor);
    }

    public void deleteSensor(Long sensorId, Long companyId) {
        Sensor sensor = sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId)
                .orElseThrow(() -> new NotFoundException("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY));
        sensorRepository.delete(sensor);
    }

    public Optional<Sensor> getSensorBySensorApiKey(UUID apiKey) {
        return sensorRepository.findBySensorApiKey(apiKey);
    }
}
