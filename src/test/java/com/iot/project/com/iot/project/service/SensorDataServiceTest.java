package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataReadingDTO;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.entity.SensorMetric;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.SensorDataDetailRepository;
import com.iot.project.com.iot.project.repository.SensorDataHeaderRepository;
import com.iot.project.com.iot.project.repository.SensorMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorDataServiceTest {

    @Mock
    private SensorDataHeaderRepository sensorDataHeaderRepository;

    @Mock
    private SensorDataDetailRepository sensorDataDetailRepository;

    @Mock
    private SensorMetricRepository sensorMetricRepository;

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorDataService sensorDataService;

    @Test
    public void testGetAllSensorData() {
        GetSensorDataRequest request = GetSensorDataRequest.builder()
                .sensorIds(Arrays.asList(10L, 11L))
                .from("1672531200")
                .to("1704067199")
                .build();
        Long companyId = 1L;

        SensorDataHeader header1 = SensorDataHeader.builder().id(1L).sensorId(10L).build();
        SensorDataHeader header2 = SensorDataHeader.builder().id(2L).sensorId(11L).build();

        when(sensorDataHeaderRepository.findBySensorIdsAndDateRangeAndCompanyApiKey(
                eq(companyId),
                eq(request.getSensorIds()),
                eq(Instant.ofEpochSecond(Long.parseLong(request.getFrom()))),
                eq(Instant.ofEpochSecond(Long.parseLong(request.getTo())))
        )).thenReturn(Arrays.asList(header1, header2));

        List<SensorDataHeader> result = sensorDataService.getAllSensorData(request, companyId);
        assertEquals(2, result.size());
        verify(sensorDataHeaderRepository).findBySensorIdsAndDateRangeAndCompanyApiKey(
                eq(companyId),
                eq(request.getSensorIds()),
                eq(Instant.ofEpochSecond(Long.parseLong(request.getFrom()))),
                eq(Instant.ofEpochSecond(Long.parseLong(request.getTo())))
        );
    }

    @Test
    public void testCreateSensorData() {
        UUID apiKey = UUID.randomUUID();

        SensorDataReadingDTO reading = SensorDataReadingDTO.builder()
                .datetime("1672531200")
                .metrics(new HashMap<>())
                .build();
        reading.getMetrics().put("temperature", 20.0);
        reading.getMetrics().put("humidity", 80.0);

        CreateSensorDataRequest request = CreateSensorDataRequest.builder()
                .apiKey(apiKey)
                .jsonData(Arrays.asList(reading))
                .build();

        Sensor sensor = Sensor.builder().sensorId(10L).build();
        when(sensorService.getSensorBySensorApiKey(apiKey)).thenReturn(Optional.of(sensor));

        SensorDataHeader savedHeader = SensorDataHeader.builder().id(1L).sensorId(sensor.getSensorId()).build();
        when(sensorDataHeaderRepository.save(any(SensorDataHeader.class))).thenReturn(savedHeader);

        when(sensorMetricRepository.findByMetricName("temperature")).thenReturn(Optional.empty());

        SensorMetric humidityMetric = SensorMetric.builder().metricName("humidity").build();
        when(sensorMetricRepository.findByMetricName("humidity")).thenReturn(Optional.empty());
        when(sensorMetricRepository.save(any(SensorMetric.class))).thenReturn(humidityMetric);

        SensorDataHeader result = sensorDataService.createSensorData(request);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getDetails());
        assertEquals(2, result.getDetails().size());

        verify(sensorService).getSensorBySensorApiKey(apiKey);
        verify(sensorDataHeaderRepository).save(any(SensorDataHeader.class));
    }

    @Test
    public void testUpdateSensorData() {
        Long sensorDataId = 1L;
        Long companyId = 1L;

        SensorDataHeader existingHeader = SensorDataHeader.builder()
                .id(sensorDataId)
                .sensorId(10L)
                .build();
        existingHeader.setDetails(new HashSet<>());
        when(sensorDataHeaderRepository.findById(sensorDataId)).thenReturn(Optional.of(existingHeader));

        when(sensorService.getSensorById(existingHeader.getSensorId(), companyId))
                .thenReturn(Sensor.builder().sensorId(10L).build());

        SensorDataReadingDTO reading = SensorDataReadingDTO.builder()
                .datetime("1672531200")
                .metrics(new HashMap<>())
                .build();
        reading.getMetrics().put("temperature", 20.0);
        CreateSensorDataRequest updateRequest = CreateSensorDataRequest.builder()
                .apiKey(null)
                .jsonData(Arrays.asList(reading))
                .build();

        SensorMetric tempMetric = SensorMetric.builder().metricName("temperature").build();
        when(sensorMetricRepository.findByMetricName("temperature")).thenReturn(Optional.empty());
        when(sensorMetricRepository.save(any(SensorMetric.class))).thenReturn(tempMetric);

        when(sensorDataHeaderRepository.save(existingHeader)).thenReturn(existingHeader);

        SensorDataHeader updatedHeader = sensorDataService.updateSensorData(sensorDataId, updateRequest, companyId);
        assertNotNull(updatedHeader);
        assertFalse(updatedHeader.getDetails().isEmpty());
        assertEquals(1, updatedHeader.getDetails().size());

        verify(sensorDataHeaderRepository).findById(sensorDataId);
        verify(sensorService).getSensorById(existingHeader.getSensorId(), companyId);
        verify(sensorDataDetailRepository).deleteAll(anySet());
        verify(sensorDataDetailRepository).saveAll(anySet());
        verify(sensorDataHeaderRepository).save(existingHeader);
    }

    @Test
    public void testDeleteSensorData() {
        Long sensorDataId = 1L;
        Long companyId = 1L;
        SensorDataHeader header = SensorDataHeader.builder()
                .id(sensorDataId)
                .sensorId(10L)
                .build();
        when(sensorDataHeaderRepository.findById(sensorDataId)).thenReturn(Optional.of(header));

        Sensor sensor = Sensor.builder().sensorId(10L).build();
        when(sensorService.getSensorById(header.getSensorId(), companyId)).thenReturn(sensor);

        assertDoesNotThrow(() -> sensorDataService.deleteSensorData(sensorDataId, companyId));
        verify(sensorDataHeaderRepository).deleteById(sensorDataId);
    }

    @Test
    public void testDeleteSensorDataHeaderNotFound() {
        Long sensorDataId = 1L;
        Long companyId = 1L;
        when(sensorDataHeaderRepository.findById(sensorDataId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                sensorDataService.deleteSensorData(sensorDataId, companyId)
        );
        assertEquals(RESOURCE_NOT_FOUND, exception.getMessage());
    }
}
