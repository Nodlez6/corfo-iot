package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
import com.iot.project.com.iot.project.dto.sensor.SensorResponseDto;
import com.iot.project.com.iot.project.dto.sensor.UpdateSensorRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.service.SensorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
@Validated
@Tag(name = "Sensors", description = "Operations related to sensor management")
public class SensorController {

    private final SensorService sensorService;
    private final AppProperties appProperties;

    private String APP_NAME;

    @PostConstruct
    public void init() {
        this.APP_NAME = appProperties.getResponseKey();
    }

    @Operation(summary = "Get all sensors", description = "Returns a list of all sensors associated with the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensors retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<SensorResponseDto>>> getAllSensors(HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<Sensor> sensors = sensorService.getAllSensors(companyId);
        List<SensorResponseDto> responseDtos = sensors.stream()
                .map(sensor -> new SensorResponseDto(sensor, ActionType.LIST_ALL))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSORS, responseDtos));
    }

    @Operation(summary = "Get sensor by ID", description = "Retrieves a sensor by its ID, validating it belongs to the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<SensorResponseDto>> getSensorById(
            @Parameter(description = "Sensor ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor sensor = sensorService.getSensorById(id, companyId);
        SensorResponseDto responseDto = new SensorResponseDto(sensor, ActionType.GET_BY_ID);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR, responseDto));
    }

    @Operation(summary = "Get sensor by API key", description = "Retrieves a sensor using its API key, validating it belongs to the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")
    })
    @GetMapping("/apikey/{apiKey}")
    public ResponseEntity<ServiceResponse<SensorResponseDto>> getSensorByApiKey(
            @Parameter(description = "Sensor API key") @PathVariable("apiKey") UUID apiKey) {
        Sensor sensor = sensorService.getSensorBySensorApiKey(apiKey);
        SensorResponseDto responseDto = new SensorResponseDto(sensor, ActionType.GET_BY_APIKEY);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR, responseDto));
    }

    @Operation(summary = "Create a new sensor", description = "Creates a new sensor under the authenticated company using the provided data.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor created successfully")
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<SensorResponseDto>> createSensor(
            @Parameter(description = "Sensor creation data") @RequestBody @Validated CreateSensorRequest request,
            HttpServletRequest httpRequest) {
        log.info("Create Sensor -> {}", request);
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor created = sensorService.createSensor(request, companyId);
        SensorResponseDto responseDto = new SensorResponseDto(created, ActionType.CREATED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR, responseDto));
    }

    @Operation(summary = "Update an existing sensor", description = "Updates the information of an existing sensor under the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor updated successfully")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<SensorResponseDto>> updateSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id,
            @Parameter(description = "Sensor update data") @RequestBody UpdateSensorRequest request,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor updated = sensorService.updateSensor(id, request, companyId);
        SensorResponseDto responseDto = new SensorResponseDto(updated, ActionType.UPDATED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR, responseDto));
    }

    @Operation(summary = "Delete a sensor", description = "Deletes a sensor by its ID under the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<SensorResponseDto>> deleteSensor(
            @Parameter(description = "Sensor ID to delete") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor deleted = sensorService.deleteSensor(id, companyId);
        SensorResponseDto responseDto = new SensorResponseDto(deleted, ActionType.DELETED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR, responseDto));
    }
}
