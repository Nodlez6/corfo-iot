package com.iot.project.com.iot.project.controller;

import java.util.UUID;

import com.iot.project.com.iot.project.dto.sensorDataHeader.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorDataHeader.GetSensorDataRequest;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensorData")
@RequiredArgsConstructor
@Validated
public class SensorDataController {
    private final SensorDataService sensorDataService;
//    @GetMapping
//    public ResponseEntity<List<SensorData>> getSensorData(GetSensorDataRequest request, HttpServletRequest httpRequest) {
//        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
//        List<SensorData> sensorDataList = sensorDataHeaderService.getAllSensorData(request, companyId);
//        return ResponseEntity.ok(sensorDataList);
//    }

    @PostMapping
    public ResponseEntity<SensorDataHeader> createSensorData(@RequestBody CreateSensorDataRequest request) {
        SensorDataHeader created = sensorDataService.createSensorData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
