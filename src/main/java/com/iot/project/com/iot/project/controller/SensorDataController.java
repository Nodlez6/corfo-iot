package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataDetailDTO;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataHeaderResponse;
import com.iot.project.com.iot.project.dto.sensorData.SensorDataResponseDto;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.service.SensorDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/sensorData")
@RequiredArgsConstructor
@Validated
@Tag(name = "SensorData", description = "Operations related to sensor data management")
public class SensorDataController {

    private final SensorDataService sensorDataService;
    private final AppProperties appProperties;

    private String APP_NAME;

    @PostConstruct
    public void init() {
        this.APP_NAME = appProperties.getResponseKey();
    }

    @Operation(summary = "Get all sensor data", description = "Retrieves all sensor data records associated with the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor data retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> getSensorData(
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<SensorDataHeader> sensorDataList = sensorDataService.getAllSensorData(companyId);

        List<SensorDataHeaderResponse> responseDtos = sensorDataList.stream()
                .map(header -> {
                    List<SensorDataDetailDTO> details = header.getDetails().stream()
                            .map(detail -> SensorDataDetailDTO.builder()
                                    .metricName(detail.getMetric().getMetricName())
                                    .value(parseToOriginalType(detail.getValue()))
                                    .timestamp(detail.getTimestamp())
                                    .build())
                            .collect(Collectors.toList());

                    return SensorDataHeaderResponse.builder()
                            .sensorDataHeaderId(header.getId())
                            .sensorId(header.getSensorId())
                            .timestamp(header.getTimestamp())
                            .details(details)
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDtos));
    }

    @Operation(summary = "Get sensor data by epoch time", description = "Retrieves sensor data filtered by epoch time range and sensor IDs.", responses = {
            @ApiResponse(responseCode = "200", description = "Filtered sensor data retrieved successfully")
    })
    @GetMapping("/byEpoch")
    public ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> getSensorDataByDate(
            @RequestBody @Validated GetSensorDataRequest request,
            HttpServletRequest httpRequest) {

        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<SensorDataHeader> sensorDataList = sensorDataService.getAllSensorData(request, companyId);

        List<SensorDataHeaderResponse> responseDtos = sensorDataList.stream()
                .map(header -> {
                    List<SensorDataDetailDTO> details = header.getDetails().stream()
                            .map(detail -> SensorDataDetailDTO.builder()
                                    .metricName(detail.getMetric().getMetricName())
                                    .value(parseToOriginalType(detail.getValue()))
                                    .timestamp(detail.getTimestamp())
                                    .build())
                            .collect(Collectors.toList());

                    return SensorDataHeaderResponse.builder()
                            .sensorDataHeaderId(header.getId())
                            .sensorId(header.getSensorId())
                            .timestamp(header.getTimestamp())
                            .details(details)
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDtos));
    }

    @Operation(summary = "Get sensor data by date range", description = "Retrieves sensor data within a specific ISO date-time range and by sensor IDs.", responses = {
            @ApiResponse(responseCode = "200", description = "Filtered sensor data retrieved successfully")
    })
    @GetMapping("/byDate")
    public ResponseEntity<ServiceResponse<List<SensorDataHeaderResponse>>> getSensorDataByDateTimeRange(
            @RequestBody @Validated GetSensorDataRequest request,
            HttpServletRequest httpRequest) {

        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<SensorDataHeader> sensorDataList = sensorDataService.getAllSensorDataByDateTimeRange(request, companyId);

        List<SensorDataHeaderResponse> responseDtos = sensorDataList.stream()
                .map(header -> {
                    List<SensorDataDetailDTO> details = header.getDetails().stream()
                            .map(detail -> SensorDataDetailDTO.builder()
                                    .metricName(detail.getMetric().getMetricName())
                                    .value(parseToOriginalType(detail.getValue()))
                                    .timestamp(detail.getTimestamp())
                                    .build())
                            .collect(Collectors.toList());

                    return SensorDataHeaderResponse.builder()
                            .sensorDataHeaderId(header.getId())
                            .sensorId(header.getSensorId())
                            .timestamp(header.getTimestamp())
                            .details(details)
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDtos));
    }

    @Operation(summary = "Create sensor data", description = "Creates new sensor data records based on the provided request payload.", responses = {
            @ApiResponse(responseCode = "201", description = "Sensor data created successfully")
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<SensorDataHeaderResponse>> createSensorData(
            @Parameter(description = "Payload containing sensor data") @RequestBody @Validated CreateSensorDataRequest request) {

        log.info("Create SensorData -> {}", request);
        SensorDataHeader created = sensorDataService.createSensorData(request);

        List<SensorDataDetailDTO> detailDTOs = created.getDetails().stream()
                .map(detail -> SensorDataDetailDTO.builder()
                        .metricName(detail.getMetric().getMetricName())
                        .value(parseToOriginalType(detail.getValue()))
                        .timestamp(detail.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        SensorDataHeaderResponse responseDTO = SensorDataHeaderResponse.builder()
                .sensorDataHeaderId(created.getId())
                .sensorId(created.getSensorId())
                .timestamp(created.getTimestamp())
                .details(detailDTOs)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDTO));
    }

    @Operation(summary = "Delete sensor data by date range", description = "Deletes all sensor data entries for specified sensors within a given ISO date-time range.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor data deleted successfully")
    })
    @DeleteMapping("/deleteByDate")
    public ResponseEntity<ServiceResponse<String>> deleteSensorDataByDateTimeRange(
            @Parameter(description = "Request with date range and sensor IDs") @RequestBody @Validated GetSensorDataRequest request,
            HttpServletRequest httpRequest) {

        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        int deletedCount = sensorDataService.deleteSensorDataByDateTimeRange(request, companyId);

        return ResponseEntity.ok(
                new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, deletedCount + " registros eliminados."));
    }

    @Operation(summary = "Update sensor data", description = "Updates existing sensor data by its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor data updated successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor data not found")
    })
    @PutMapping("/{sensorDataHeaderId}")
    public ResponseEntity<ServiceResponse<SensorDataResponseDto>>
    updateSensorData(
            @PathVariable Long sensorDataHeaderId,
            @RequestBody @Validated CreateSensorDataRequest request,
            HttpServletRequest httpRequest) {

        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        SensorDataHeader updated = sensorDataService.updateSensorData(sensorDataHeaderId,
                request, companyId);

        List<SensorDataDetailDTO> detailDTOs = updated.getDetails().stream()
                .map(detail -> SensorDataDetailDTO.builder()
                        .metricName(detail.getMetric().getMetricName())
                        .value(parseToOriginalType(detail.getValue()))
                        .timestamp(detail.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        SensorDataHeaderResponse responseDTO = SensorDataHeaderResponse.builder()
                .sensorDataHeaderId(updated.getId())
                .sensorId(updated.getSensorId())
                .timestamp(updated.getTimestamp())
                .details(detailDTOs)
                .build();

        SensorDataResponseDto responseDto = new SensorDataResponseDto(responseDTO, ActionType.UPDATED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDto));
    }

    @Operation(summary = "Delete sensor data by ID", description = "Cancels a specific sensor data entry by marking it for deletion or removing it directly.", responses = {
            @ApiResponse(responseCode = "200", description = "Sensor data cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor data not found")
    })
    @DeleteMapping("/{sensorDataHeaderId}")
    public ResponseEntity<ServiceResponse<SensorDataResponseDto>> cancelSensorData(
            @PathVariable Long sensorDataHeaderId,
            HttpServletRequest httpRequest) {

        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        SensorDataHeader deleted = sensorDataService.deleteSensorDataById(sensorDataHeaderId, companyId);

        List<SensorDataDetailDTO> detailDTOs = deleted.getDetails().stream()
                .map(detail -> SensorDataDetailDTO.builder()
                        .metricName(detail.getMetric().getMetricName())
                        .value(parseToOriginalType(detail.getValue()))
                        .timestamp(detail.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        SensorDataHeaderResponse responseDTO = SensorDataHeaderResponse.builder()
                .sensorDataHeaderId(deleted.getId())
                .sensorId(deleted.getSensorId())
                .timestamp(deleted.getTimestamp())
                .details(detailDTOs)
                .build();

        SensorDataResponseDto responseDto = new SensorDataResponseDto(responseDTO, ActionType.DELETED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.SENSOR_DATA, responseDto));
    }

    private Object parseToOriginalType(String value) {
        if (value == null)
            return null;
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            // Not a number
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.valueOf(value);
        }
        return value;
    }
}

