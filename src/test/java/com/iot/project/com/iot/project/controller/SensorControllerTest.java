package com.iot.project.com.iot.project.controller;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
import com.iot.project.com.iot.project.dto.sensor.SensorResponseDto;
import com.iot.project.com.iot.project.dto.sensor.UpdateSensorRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.service.SensorService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private SensorController controller;

    private static final String APP_NAME = "iotService";
    private static final Long COMPANY_ID = 42L;

    @BeforeEach
    void setUp() {
        when(appProperties.getResponseKey()).thenReturn(APP_NAME);
        controller.init();
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
    }

    @Test
    void getAllSensorsShouldReturnListWrapped() {
        Sensor s1 = Sensor.builder().sensorId(1L).sensorName("S1").sensorApiKey(UUID.randomUUID()).build();
        Sensor s2 = Sensor.builder().sensorId(2L).sensorName("S2").sensorApiKey(UUID.randomUUID()).build();
        when(sensorService.getAllSensors(COMPANY_ID)).thenReturn(List.of(s1, s2));

        ResponseEntity<ServiceResponse<List<SensorResponseDto>>> response = controller.getAllSensors(httpRequest);
        ServiceResponse<List<SensorResponseDto>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<SensorResponseDto>>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, List<SensorResponseDto>> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.SENSORS.name()));
        List<SensorResponseDto> dataList = innerMap.get(ActionMethod.SENSORS.name());
        assertEquals(2, dataList.size());
        assertEquals(s1.getSensorId(), dataList.get(0).getSensorId());
    }

    @Test
    void getSensorByIdShouldReturnWrappedSensor() {
        Sensor s = Sensor.builder().sensorId(5L).sensorName("Sensor5").sensorApiKey(UUID.randomUUID()).build();
        when(sensorService.getSensorById(5L, COMPANY_ID)).thenReturn(s);

        ResponseEntity<ServiceResponse<SensorResponseDto>> response = controller.getSensorById(5L, httpRequest);
        ServiceResponse<SensorResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, SensorResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, SensorResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.SENSOR.name()));
        SensorResponseDto dto = innerMap.get(ActionMethod.SENSOR.name());
        assertEquals(s.getSensorId(), dto.getSensorId());
    }

    @Test
    void createSensorShouldReturnOkStatus() {
        UUID key = UUID.randomUUID();
        CreateSensorRequest req = CreateSensorRequest.builder().sensorName("NewSensor").build();
        Sensor created = Sensor.builder().sensorId(7L).sensorName("NewSensor").sensorApiKey(key).build();
        when(sensorService.createSensor(req, COMPANY_ID)).thenReturn(created);

        ResponseEntity<ServiceResponse<SensorResponseDto>> response = controller.createSensor(req, httpRequest);
        ServiceResponse<SensorResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, SensorResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, SensorResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.SENSOR.name()));
        SensorResponseDto dto = innerMap.get(ActionMethod.SENSOR.name());
        assertEquals(created.getSensorId(), dto.getSensorId());
    }

    @Test
    void updateSensorShouldReturnOkStatus() {
        UUID key = UUID.randomUUID();
        UpdateSensorRequest req = UpdateSensorRequest.builder().sensorName("UpdSensor").sensorApiKey(key).build();
        Sensor updated = Sensor.builder().sensorId(8L).sensorName("UpdSensor").sensorApiKey(key).build();
        when(sensorService.updateSensor(8L, req, COMPANY_ID)).thenReturn(updated);

        ResponseEntity<ServiceResponse<SensorResponseDto>> response = controller.updateSensor(8L, req, httpRequest);
        ServiceResponse<SensorResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, SensorResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, SensorResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.SENSOR.name()));
        SensorResponseDto dto = innerMap.get(ActionMethod.SENSOR.name());
        assertEquals(updated.getSensorName(), dto.getName());
    }

    @Test
    void deleteSensorShouldReturnOkStatus() {
        Sensor deleted = Sensor.builder().sensorId(9L).sensorName("DelSensor").sensorApiKey(UUID.randomUUID()).build();
        when(sensorService.deleteSensor(9L, COMPANY_ID)).thenReturn(deleted);

        ResponseEntity<ServiceResponse<SensorResponseDto>> response = controller.deleteSensor(9L, httpRequest);
        ServiceResponse<SensorResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, SensorResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, SensorResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.SENSOR.name()));
        SensorResponseDto dto = innerMap.get(ActionMethod.SENSOR.name());
        assertEquals(deleted.getSensorId(), dto.getSensorId());
    }
}