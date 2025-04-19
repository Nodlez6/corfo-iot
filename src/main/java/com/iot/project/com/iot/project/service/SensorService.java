package com.iot.project.com.iot.project.service;

import java.util.HashSet;
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

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.SENSOR_APIKEY_NOT_FOUND;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.LOCATION_ID_NOT_FOUND_BY_COMPANY;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.SENSOR_NOT_FOUND_BY_COMPANY;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final LocationRepository locationRepository;



    // public List<Sensor> getAllSensors(Long companyId) {
    //     return sensorRepository.findAllByCompanyId(companyId);
    // }


    public List<Sensor> getAllSensors(Long companyId) {
        return sensorRepository.findAllByCompanyIdOrderBySensorIdAsc(companyId);
    }


    public Sensor getSensorById(Long sensorId, Long companyId) {
        return sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId)
                .orElseThrow(() -> new NotFoundException(SENSOR_NOT_FOUND_BY_COMPANY));
    }


    public Sensor createSensor(CreateSensorRequest request, Long companyId) {
        Location location = locationRepository.findByLocationIdAndCompanyId(request.getLocationId(), companyId)
                .orElseThrow(() -> new NotFoundException(LOCATION_ID_NOT_FOUND_BY_COMPANY));

        Sensor sensor = Sensor.builder()
                .sensorName(request.getSensorName())
                .sensorCategory(request.getSensorCategory())
                .sensorMeta(request.getSensorMeta())
                .locationId(location.getLocationId())
                .sensorData(new HashSet<>())
                .build();
        return sensorRepository.save(sensor);
    }


    public Sensor updateSensor(Long id, UpdateSensorRequest request, Long companyId) {
        System.out.println("MENU - 1");

        Sensor sensor = sensorRepository.findBySensorIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException(SENSOR_NOT_FOUND_BY_COMPANY));

        sensor.setSensorName(request.getSensorName() != null ? request.getSensorName() : sensor.getSensorName());
        sensor.setSensorCategory(request.getSensorCategory() != null ? request.getSensorCategory() : sensor.getSensorCategory());
        sensor.setSensorMeta(request.getSensorMeta() != null ? request.getSensorMeta() : sensor.getSensorMeta());
        // sensor.setLocationId(request.getLocationId() != null ? request.getLocationId() : sensor.getLocationId());
        sensor.setSensorApiKey(request.getSensorApiKey() != null ? request.getSensorApiKey() : sensor.getSensorApiKey());

        return sensorRepository.save(sensor);
    }


    public Sensor deleteSensor(Long sensorId, Long companyId) {
        Sensor sensor = sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId)
                .orElseThrow(() -> new NotFoundException(SENSOR_NOT_FOUND_BY_COMPANY));
        sensorRepository.delete(sensor);
        return sensor;
    }
   

    public Sensor getSensorBySensorApiKey(UUID apiKey) {
        return sensorRepository.findBySensorApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException(SENSOR_APIKEY_NOT_FOUND));
    }
}
