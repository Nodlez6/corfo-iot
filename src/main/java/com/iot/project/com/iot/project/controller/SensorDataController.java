package com.iot.project.com.iot.project.controller;

import java.util.List;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.entity.SensorDataDetail;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.service.SensorDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensorData")
@RequiredArgsConstructor
@Validated
public class SensorDataController {
    private final SensorDataService sensorDataService;
    @GetMapping
    public ResponseEntity<List<SensorDataHeader>> getSensorData(@RequestBody @Validated GetSensorDataRequest request, HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<SensorDataHeader> sensorDataList = sensorDataService.getAllSensorData(request, companyId);
        return ResponseEntity.ok(sensorDataList);
    }

    @PostMapping
    public ResponseEntity<SensorDataHeader> createSensorData(@RequestBody @Validated CreateSensorDataRequest request) {
        SensorDataHeader created = sensorDataService.createSensorData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
