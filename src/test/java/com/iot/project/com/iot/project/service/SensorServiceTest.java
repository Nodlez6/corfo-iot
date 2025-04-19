package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
import com.iot.project.com.iot.project.dto.sensor.UpdateSensorRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.LocationRepository;
import com.iot.project.com.iot.project.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private SensorService sensorService;

    @Test
    void getAllSensorsReturnsListFromRepository() {
        Long companyId = 1L;
        List<Sensor> sensors = List.of(
                Sensor.builder().sensorId(1L).build(),
                Sensor.builder().sensorId(2L).build()
        );
        when(sensorRepository.findAllByCompanyIdOrderBySensorIdAsc(companyId)).thenReturn(sensors);
        assertEquals(sensors, sensorService.getAllSensors(companyId));
    }

    @Test
    void getSensorByIdReturnsSensorWhenExists() {
        Long companyId = 2L;
        Sensor sensor = Sensor.builder().sensorId(5L).build();
        when(sensorRepository.findBySensorIdAndCompanyId(5L, companyId))
                .thenReturn(Optional.of(sensor));
        assertEquals(sensor, sensorService.getSensorById(5L, companyId));
    }

    @Test
    void getSensorByIdThrowsNotFoundWhenNotExists() {
        when(sensorRepository.findBySensorIdAndCompanyId(1L, 1L))
                .thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> sensorService.getSensorById(1L, 1L)
        );
        assertEquals(SENSOR_NOT_FOUND_BY_COMPANY, ex.getMessage());
    }

    @Test
    void createSensorSavesAndReturnsEntityWhenLocationExists() {
        Long companyId = 3L;
        UUID apiKey = UUID.randomUUID();
        Location loc = Location.builder().locationId(10L).companyId(companyId).build();
        CreateSensorRequest req = CreateSensorRequest.builder()
                .sensorName("S1")
                .sensorCategory("C1")
                .sensorMeta("M1")
                .locationId(loc.getLocationId())
                .build();
        when(locationRepository.findByLocationIdAndCompanyId(loc.getLocationId(), companyId))
                .thenReturn(Optional.of(loc));
        Sensor toSave = Sensor.builder()
                .sensorName("S1")
                .sensorCategory("C1")
                .sensorMeta("M1")
                .locationId(loc.getLocationId())
                .build();
        when(sensorRepository.save(any(Sensor.class))).thenReturn(toSave);
        assertEquals(toSave, sensorService.createSensor(req, companyId));
    }

    @Test
    void createSensorThrowsNotFoundWhenLocationNotExists() {
        when(locationRepository.findByLocationIdAndCompanyId(5L, 4L))
                .thenReturn(Optional.empty());
        CreateSensorRequest req = CreateSensorRequest.builder()
                .locationId(5L).build();
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> sensorService.createSensor(req, 4L)
        );
        assertEquals(LOCATION_ID_NOT_FOUND_BY_COMPANY, ex.getMessage());
    }

    @Test
    void updateSensorReturnsUpdatedWhenExists() {
        Long companyId = 5L;
        Sensor existing = Sensor.builder()
                .sensorId(20L)
                .sensorName("Old")
                .sensorCategory("OC")
                .sensorMeta("OM")
                .sensorApiKey(UUID.randomUUID())
                .build();
        when(sensorRepository.findBySensorIdAndCompanyId(20L, companyId))
                .thenReturn(Optional.of(existing));
        UpdateSensorRequest req = UpdateSensorRequest.builder()
                .sensorName("New")
                .sensorCategory("NC")
                .sensorMeta("NM")
                .sensorApiKey(UUID.randomUUID())
                .build();
        when(sensorRepository.save(existing)).thenReturn(existing);
        Sensor result = sensorService.updateSensor(20L, req, companyId);
        assertEquals("New", result.getSensorName());
        assertEquals("NC", result.getSensorCategory());
        assertEquals("NM", result.getSensorMeta());
        assertEquals(req.getSensorApiKey(), result.getSensorApiKey());
    }

    @Test
    void updateSensorThrowsNotFoundWhenNotExists() {
        when(sensorRepository.findBySensorIdAndCompanyId(1L, 2L))
                .thenReturn(Optional.empty());
        UpdateSensorRequest req = UpdateSensorRequest.builder().build();
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> sensorService.updateSensor(1L, req, 2L)
        );
        assertEquals(SENSOR_NOT_FOUND_BY_COMPANY, ex.getMessage());
    }

    @Test
    void deleteSensorDeletesAndReturnsEntityWhenExists() {
        Long companyId = 6L;
        Sensor sensor = Sensor.builder().sensorId(30L).build();
        when(sensorRepository.findBySensorIdAndCompanyId(30L, companyId))
                .thenReturn(Optional.of(sensor));
        Sensor result = sensorService.deleteSensor(30L, companyId);
        verify(sensorRepository).delete(sensor);
        assertEquals(sensor, result);
    }

    @Test
    void deleteSensorThrowsNotFoundWhenNotExists() {
        when(sensorRepository.findBySensorIdAndCompanyId(2L, 3L))
                .thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> sensorService.deleteSensor(2L, 3L)
        );
        assertEquals(SENSOR_NOT_FOUND_BY_COMPANY, ex.getMessage());
    }

    @Test
    void getSensorBySensorApiKeyReturnsSensorWhenExists() {
        UUID key = UUID.randomUUID();
        Sensor sensor = Sensor.builder().sensorApiKey(key).build();
        when(sensorRepository.findBySensorApiKey(key))
                .thenReturn(Optional.of(sensor));
        assertEquals(sensor, sensorService.getSensorBySensorApiKey(key));
    }

    @Test
    void getSensorBySensorApiKeyThrowsNotFoundWhenNotExists() {
        UUID key = UUID.randomUUID();
        when(sensorRepository.findBySensorApiKey(key))
                .thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> sensorService.getSensorBySensorApiKey(key)
        );
        assertEquals(SENSOR_APIKEY_NOT_FOUND, ex.getMessage());
    }
}
