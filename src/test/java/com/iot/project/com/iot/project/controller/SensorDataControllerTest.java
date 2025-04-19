package com.iot.project.com.iot.project.controller;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataDetailDTO;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataHeaderResponse;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataReadingDTO;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataResponseDto;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.SensorMetric;
import com.iot.project.com.iot.project.entity.SensorDataDetail;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.service.SensorDataService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Metric;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorDataControllerTest {

    @Mock
    private SensorDataService sensorDataService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private SensorDataController controller;

    private static final String APP_NAME = "iotService";
    private static final Long COMPANY_ID = 42L;

    @BeforeEach
    void setUp() {
        when(appProperties.getResponseKey()).thenReturn(APP_NAME);
        controller.init();
    }

    @Test
    void getSensorDataShouldReturnListWrapped() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        SensorDataHeader header = SensorDataHeader.builder()
                .id(1L)
                .sensorId(100L)
                .timestamp(Instant.parse("2025-04-19T10:00:00Z"))
                .details(Set.of(SensorDataDetail.builder().metric(SensorMetric.builder().metricName("test").build()).build()))
                .build();
        when(sensorDataService.getAllSensorData(COMPANY_ID)).thenReturn(List.of(header));

        ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> response = controller.getSensorData(httpRequest);
        ServiceResponse<List<SensorDataHeaderResponse>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<SensorDataHeaderResponse>>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, List<SensorDataHeaderResponse>> inner = respMap.get(APP_NAME);
        assertTrue(inner.containsKey(ActionMethod.SENSOR_DATA.name()));
        List<SensorDataHeaderResponse> list = inner.get(ActionMethod.SENSOR_DATA.name());
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getSensorDataHeaderId());
    }

    @Test
    void getSensorDataByEpochShouldReturnListWrapped() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        GetSensorDataRequest req = GetSensorDataRequest.builder().build();
        SensorDataHeader header = SensorDataHeader.builder()
                .id(2L)
                .sensorId(101L)
                .timestamp(Instant.now())
                .details(Set.of(SensorDataDetail.builder().metric(SensorMetric.builder().metricName("test").build()).build()))
                .build();
        when(sensorDataService.getAllSensorData(eq(req), eq(COMPANY_ID))).thenReturn(List.of(header));

        ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> response = controller.getSensorDataByDate(req, httpRequest);
        ServiceResponse<List<SensorDataHeaderResponse>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<SensorDataHeaderResponse>>> respMap = body.getResponse();
        assertTrue(respMap.get(APP_NAME).containsKey(ActionMethod.SENSOR_DATA.name()));
    }

    @Test
    void getSensorDataByDateTimeRangeShouldReturnListWrapped() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        GetSensorDataRequest req = GetSensorDataRequest.builder().build();
        SensorDataHeader header = SensorDataHeader.builder()
                .id(3L)
                .sensorId(102L)
                .timestamp(Instant.now())
                .details(Set.of(SensorDataDetail.builder().metric(SensorMetric.builder().metricName("test").build()).build()))
                .build();
        when(sensorDataService.getAllSensorDataByDateTimeRange(eq(req), eq(COMPANY_ID))).thenReturn(List.of(header));

        ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> response = controller.getSensorDataByDateTimeRange(req, httpRequest);
        ServiceResponse<List<SensorDataHeaderResponse>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<SensorDataHeaderResponse>>> respMap = body.getResponse();
        assertTrue(respMap.get(APP_NAME).containsKey(ActionMethod.SENSOR_DATA.name()));
    }

    @Test
    void createSensorDataShouldReturnCreatedStatus() {
        UUID apiKey = UUID.randomUUID();
        SensorDataReadingDTO reading = SensorDataReadingDTO.builder()
                .datetime("2025-04-19T10:00:00Z")
                .metrics(Map.of("temp", 1.23))
                .build();

        CreateSensorDataRequest req = CreateSensorDataRequest.builder()
                .apiKey(apiKey)
                .jsonData(List.of(reading))
                .build();

        SensorDataDetail detail = SensorDataDetail.builder()
                .metric(SensorMetric.builder().id(null).metricName("temp").build())
                .value("1.23")
                .timestamp(Instant.parse("2025-04-19T10:00:00Z"))
                .build();

        SensorDataHeader created = SensorDataHeader.builder()
                .id(4L)
                .sensorId(200L)
                .timestamp(Instant.parse("2025-04-19T10:00:00Z"))
                .details(Set.of(detail))
                .build();

        when(sensorDataService.createSensorData(eq(req))).thenReturn(created);

        ResponseEntity<ServiceResponse<SensorDataHeaderResponse>> response = controller.createSensorData(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ServiceResponse<SensorDataHeaderResponse> body = response.getBody();
        assertNotNull(body);

        Map<String, Map<String, SensorDataHeaderResponse>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, SensorDataHeaderResponse> inner = respMap.get(APP_NAME);
        assertTrue(inner.containsKey(ActionMethod.SENSOR_DATA.name()));

        SensorDataHeaderResponse dto = inner.get(ActionMethod.SENSOR_DATA.name());
        assertEquals(4L, dto.getSensorDataHeaderId());
        assertEquals(1, dto.getDetails().size());
    }

    @Test
    void deleteSensorDataByDateTimeRangeShouldReturnCountMessage() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        GetSensorDataRequest req = GetSensorDataRequest.builder().build();
        when(sensorDataService.deleteSensorDataByDateTimeRange(eq(req), eq(COMPANY_ID))).thenReturn(3);

        ResponseEntity<ServiceResponse<String>> response = controller.deleteSensorDataByDateTimeRange(req, httpRequest);
        ServiceResponse<String> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String message = body.getResponse().get(APP_NAME).get(ActionMethod.SENSOR_DATA.name());
        assertEquals("3 registros eliminados.", message);
    }


    @Test
    void updateSensorDataShouldReturnUpdatedDto() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        long headerId = 5L;
        UUID apiKey = UUID.randomUUID();
        CreateSensorDataRequest req = CreateSensorDataRequest.builder()
                .apiKey(apiKey)
                .jsonData(Collections.emptyList())
                .build();

        SensorDataHeader updated = SensorDataHeader.builder()
                .id(headerId)
                .sensorId(201L)
                .timestamp(Instant.now())
                .details(Set.of(SensorDataDetail.builder().metric(SensorMetric.builder().metricName("test").build()).build()))
                .build();
        when(sensorDataService.updateSensorData(headerId, req, COMPANY_ID)).thenReturn(updated);

        ResponseEntity<ServiceResponse<SensorDataResponseDto>> response = controller.updateSensorData(headerId, req, httpRequest);
        ServiceResponse<SensorDataResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensorDataResponseDto dto = body.getResponse().get(APP_NAME).get(ActionMethod.SENSOR_DATA.name());
    }

    @Test
    void cancelSensorDataShouldReturnDeletedDto() {
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
        long headerId = 6L;
        SensorDataHeader deleted = SensorDataHeader.builder()
                .id(headerId)
                .sensorId(202L)
                .timestamp(Instant.now())
                .details(Set.of(SensorDataDetail.builder().metric(SensorMetric.builder().metricName("test").build()).build()))
                .build();
        when(sensorDataService.deleteSensorDataById(headerId, COMPANY_ID)).thenReturn(deleted);

        ResponseEntity<ServiceResponse<SensorDataResponseDto>> response = controller.cancelSensorData(headerId, httpRequest);
        ServiceResponse<SensorDataResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SensorDataResponseDto dto = body.getResponse().get(APP_NAME).get(ActionMethod.SENSOR_DATA.name());
    }
}
