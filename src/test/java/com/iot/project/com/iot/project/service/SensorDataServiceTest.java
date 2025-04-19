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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.SENSORDATA_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorDataServiceTest {

    @Mock
    private SensorDataHeaderRepository headerRepo;

    @Mock
    private SensorService sensorService;

    @Mock
    private SensorDataDetailRepository detailRepo;

    @Mock
    private SensorMetricRepository metricRepo;

    @InjectMocks
    private SensorDataService service;

    @Test
    void getAllSensorDataReturnsFromRepo() {
        Long companyId = 1L;
        List<SensorDataHeader> list = List.of(new SensorDataHeader(), new SensorDataHeader());
        when(headerRepo.findAllByCompanyId(companyId)).thenReturn(list);
        assertEquals(list, service.getAllSensorData(companyId));
    }

    @Test
    void getAllSensorDataWithRequestParsesEpochAndCallsRepo() {
        Long companyId = 2L;
        GetSensorDataRequest req = new GetSensorDataRequest("10", "20", List.of(5L));
        List<SensorDataHeader> list = List.of(new SensorDataHeader());
        when(headerRepo.findBySensorIdsAndDateRangeAndCompanyApiKey(
                eq(companyId), anyList(), eq(Instant.ofEpochSecond(10)), eq(Instant.ofEpochSecond(20))))
                .thenReturn(list);
        assertEquals(list, service.getAllSensorData(req, companyId));
    }

    @Test
    void getAllSensorDataByDateTimeRangeParsesIsoAndCallsRepo() {
        Long companyId = 3L;
        String from = "2025-04-19T18:00:00";
        String to   = "2025-04-19T19:00:00";
        GetSensorDataRequest req = new GetSensorDataRequest(from, to, List.of(7L,8L));
        Instant fromInst = LocalDateTime.parse(from).toInstant(ZoneOffset.UTC);
        Instant toInst   = LocalDateTime.parse(to).toInstant(ZoneOffset.UTC);
        List<SensorDataHeader> list = List.of(new SensorDataHeader());
        when(headerRepo.findBySensorIdsAndDateRangeAndCompanyApiKey(companyId, req.getSensorIds(), fromInst, toInst))
                .thenReturn(list);
        assertEquals(list, service.getAllSensorDataByDateTimeRange(req, companyId));
    }

    @Test
    void createSensorDataSavesHeaderMetricsAndDetails() {
        UUID apiKey = UUID.randomUUID();
        SensorDataReadingDTO reading = SensorDataReadingDTO.builder()
                .datetime("100")
                .metrics(Map.of("m", 1.0))
                .build();
        CreateSensorDataRequest req = new CreateSensorDataRequest(apiKey, List.of(reading));

        Sensor sensor = new Sensor();
        sensor.setSensorId(42L);
        when(sensorService.getSensorBySensorApiKey(apiKey)).thenReturn(sensor);

        SensorDataHeader savedHeader = new SensorDataHeader();
        savedHeader.setId(5L);
        when(headerRepo.save(any())).thenReturn(savedHeader);

        when(metricRepo.findByMetricName("m")).thenReturn(Optional.empty());
        SensorMetric metric = new SensorMetric();
        when(metricRepo.save(any())).thenReturn(metric);

        SensorDataHeader result = service.createSensorData(req);

        assertEquals(savedHeader, result);
        assertTrue(result.getDetails().stream()
                .anyMatch(d -> "1.0".equals(d.getValue())));
    }

    @Test
    void deleteSensorDataByDateTimeRangeReturnsZeroIfNoIds() {
        Long companyId = 4L;
        String from = "2025-04-19T00:00:00";
        String to   = "2025-04-19T01:00:00";
        GetSensorDataRequest req = new GetSensorDataRequest(from,to, List.of());
        when(headerRepo.findIdsBySensorIdsAndDateRangeAndCompanyApiKey(anyLong(), anyList(), any(), any()))
                .thenReturn(Collections.emptyList());
        assertEquals(0, service.deleteSensorDataByDateTimeRange(req, companyId));
    }

    @Test
    void deleteSensorDataByDateTimeRangeDeletesAndReturnsCount() {
        Long companyId = 5L;
        List<Long> ids = List.of(1L,2L);
        when(headerRepo.findAllByTimestampBetween(any(), any())).thenReturn(List.of(new SensorDataHeader(), new SensorDataHeader()));
        when(headerRepo.findIdsBySensorIdsAndDateRangeAndCompanyApiKey(anyLong(), anyList(), any(), any()))
                .thenReturn(ids);

        int deleted = service.deleteSensorDataByDateTimeRange(new GetSensorDataRequest("2025-04-19T00:00:00","2025-04-20T00:00:00", List.of()), companyId);

        verify(detailRepo).deleteBySensorDataHeaderIdIn(ids);
        verify(headerRepo).deleteByIdIn(ids);
        assertEquals(2, deleted);
    }

    @Test
    void updateSensorDataThrowsWhenNotFound() {
        when(headerRepo.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.updateSensorData(1L, new CreateSensorDataRequest(UUID.randomUUID(), List.of()), 1L));
        assertEquals(SENSORDATA_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void deleteSensorDataByIdDeletesDetailsAndHeader() {
        Long companyId = 6L;
        SensorDataHeader header = new SensorDataHeader();
        header.setSensorId(10L);
        when(headerRepo.findById(3L)).thenReturn(Optional.of(header));
        when(sensorService.getSensorById(10L, companyId)).thenReturn(new Sensor());

        SensorDataHeader result = service.deleteSensorDataById(3L, companyId);

        verify(detailRepo).deleteBySensorDataHeaderId(3L);
        verify(headerRepo).deleteBySensorDataHeaderId(3L);
        assertEquals(header, result);
    }
}
