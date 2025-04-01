package com.iot.project.com.iot.project.controller;

import java.util.List;

import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorData;
import com.iot.project.com.iot.project.service.SensorDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensorData")
@RequiredArgsConstructor
@Validated
public class SensorDataController {
//    private final SensorDataService sensorDataService;
//    @GetMapping
//    public ResponseEntity<List<SensorData>> getSensorData(GetSensorDataRequest request, HttpServletRequest httpRequest) {
//        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
//        List<Sensor> sensors = sensorDataService.getAllSensorData(companyId);
//        return ResponseEntity.ok(SensorData);
//    }




    // return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
