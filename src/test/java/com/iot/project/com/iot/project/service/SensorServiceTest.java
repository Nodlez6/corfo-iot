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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.ENTITY_NOT_FOUND_BY_COMPANY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private SensorService sensorService;

    @Test
    public void testGetAllSensors() {
        Long companyId = 1L;
        List<Sensor> sensors = Arrays.asList(
                Sensor.builder().sensorName("sensorName1").locationId(10L).build(),
                Sensor.builder().sensorName("sensorName2").locationId(10L).build()
        );
        when(sensorRepository.findAllByCompanyId(companyId)).thenReturn(sensors);

        List<Sensor> result = sensorService.getAllSensors(companyId);

        assertEquals(2, result.size());
        verify(sensorRepository).findAllByCompanyId(companyId);
    }

    @Test
    public void testGetSensorByIdFound() {
        Long companyId = 1L;
        Long sensorId = 100L;
        Sensor sensor = Sensor.builder()
                .sensorName("sensorName")
                .sensorCategory("sensorCategory")
                .sensorMeta("sensorMeta")
                .locationId(20L)
                .build();
        when(sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId))
                .thenReturn(Optional.of(sensor));

        Sensor result = sensorService.getSensorById(sensorId, companyId);

        assertNotNull(result);
        assertEquals("sensorName", result.getSensorName());
        verify(sensorRepository).findBySensorIdAndCompanyId(sensorId, companyId);
    }

    @Test
    public void testGetSensorByIdNotFound() {
        Long companyId = 1L;
        Long sensorId = 100L;
        when(sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                sensorService.getSensorById(sensorId, companyId)
        );
        assertEquals("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY, exception.getMessage());
        verify(sensorRepository).findBySensorIdAndCompanyId(sensorId, companyId);
    }

    @Test
    public void testCreateSensor() {
        Long companyId = 1L;
        Long locationId = 10L;
        CreateSensorRequest request = CreateSensorRequest.builder()
                .sensorName("sensorName")
                .sensorCategory("sensorCategory")
                .sensorMeta("sensorMeta")
                .locationId(locationId)
                .build();

        Location location = Location.builder()
                .locationId(locationId)
                .build();
        when(locationRepository.findByLocationIdAndCompanyId(locationId, companyId))
                .thenReturn(Optional.of(location));

        Sensor sensorToSave = Sensor.builder()
                .sensorName("sensorName")
                .sensorCategory("sensorCategory")
                .sensorMeta("sensorMeta")
                .locationId(location.getLocationId())
                .build();
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensorToSave);

        Sensor result = sensorService.createSensor(request, companyId);

        assertNotNull(result);
        assertEquals("sensorName", result.getSensorName());
        assertEquals(locationId, result.getLocationId());
        verify(locationRepository).findByLocationIdAndCompanyId(locationId, companyId);
        verify(sensorRepository).save(any(Sensor.class));
    }

    @Test
    public void testUpdateSensorValid() {
        Long companyId = 1L;
        Long sensorId = 100L;
        Long locationId = 10L;
        UpdateSensorRequest request = UpdateSensorRequest.builder()
                .sensorName("updatedSensorName")
                .sensorCategory("updatedSensorCategory")
                .sensorMeta("updatedSensorMeta")
                .sensorApiKey(UUID.randomUUID())
                .locationId(locationId)
                .build();

        Sensor existingSensor = Sensor.builder()
                .sensorName("oldSensorName")
                .sensorCategory("oldSensorCategory")
                .sensorMeta("oldSensorMeta")
                .locationId(locationId)
                .build();
        when(sensorRepository.findBySensorIdAndLocationIdAndCompanyId(sensorId, locationId, companyId))
                .thenReturn(Optional.of(existingSensor));
        when(sensorRepository.save(existingSensor)).thenReturn(existingSensor);

        Sensor result = sensorService.updateSensor(sensorId, request, companyId);

        assertNotNull(result);
        assertEquals("updatedSensorName", result.getSensorName());
        assertEquals("updatedSensorCategory", result.getSensorCategory());
        assertEquals("updatedSensorMeta", result.getSensorMeta());
        assertEquals(request.getSensorApiKey(), result.getSensorApiKey());
        verify(sensorRepository).findBySensorIdAndLocationIdAndCompanyId(sensorId, locationId, companyId);
        verify(sensorRepository).save(existingSensor);
    }

    @Test
    public void testUpdateSensorNotFound() {
        Long companyId = 1L;
        Long sensorId = 100L;
        Long locationId = 10L;
        UpdateSensorRequest request = UpdateSensorRequest.builder()
                .sensorName("updatedSensorName")
                .sensorCategory("updatedSensorCategory")
                .sensorMeta("updatedSensorMeta")
                .sensorApiKey(UUID.randomUUID())
                .locationId(locationId)
                .build();

        when(sensorRepository.findBySensorIdAndLocationIdAndCompanyId(sensorId, locationId, companyId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                sensorService.updateSensor(sensorId, request, companyId)
        );
        assertEquals("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY, exception.getMessage());
        verify(sensorRepository).findBySensorIdAndLocationIdAndCompanyId(sensorId, locationId, companyId);
    }

    @Test
    public void testDeleteSensorValid() {
        Long companyId = 1L;
        Long sensorId = 100L;
        Sensor sensor = Sensor.builder()
                .sensorName("sensorName")
                .locationId(20L)
                .build();
        when(sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId))
                .thenReturn(Optional.of(sensor));

        assertDoesNotThrow(() -> sensorService.deleteSensor(sensorId, companyId));
        verify(sensorRepository).findBySensorIdAndCompanyId(sensorId, companyId);
        verify(sensorRepository).delete(sensor);
    }

    @Test
    public void testDeleteSensorNotFound() {
        Long companyId = 1L;
        Long sensorId = 100L;
        when(sensorRepository.findBySensorIdAndCompanyId(sensorId, companyId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                sensorService.deleteSensor(sensorId, companyId)
        );
        assertEquals("Sensor " + ENTITY_NOT_FOUND_BY_COMPANY, exception.getMessage());
        verify(sensorRepository).findBySensorIdAndCompanyId(sensorId, companyId);
    }

    @Test
    public void testGetSensorBySensorApiKey() {
        UUID apiKey = UUID.randomUUID();
        Sensor sensor = Sensor.builder()
                .sensorName("sensorName")
                .sensorApiKey(apiKey)
                .build();
        when(sensorRepository.findBySensorApiKey(apiKey)).thenReturn(Optional.of(sensor));

        Optional<Sensor> result = sensorService.getSensorBySensorApiKey(apiKey);

        assertTrue(result.isPresent());
        assertEquals(apiKey, result.get().getSensorApiKey());
        verify(sensorRepository).findBySensorApiKey(apiKey);
    }
}
