package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.sensor.CreateSensorRequest;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.service.SensorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Validated
public class SensorController {
    private final SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensors(HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<Sensor> sensors = sensorService.getAllSensors(companyId);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id,
                                                HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor sensor = sensorService.getSensorById(id, companyId);
        return ResponseEntity.ok(sensor);
    }

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody @Validated CreateSensorRequest request,
                                               HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Sensor created = sensorService.createSensor(request, companyId);
        return ResponseEntity.ok(created);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id,
//                                               @RequestBody @Validated UpdateSensorRequest request,
//                                               @RequestHeader("X-API-KEY") String apiKeyStr) {
//        UUID apiKey = UUID.fromString(apiKeyStr);
//        Sensor updated = sensorService.updateSensor(id, request, apiKey);
//        return ResponseEntity.ok(updated);
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSensor(@PathVariable Long id,
//                                             @RequestHeader("X-API-KEY") String apiKeyStr) {
//        UUID apiKey = UUID.fromString(apiKeyStr);
//        sensorService.deleteSensor(id, apiKey);
//        return ResponseEntity.noContent().build();
//    }
}
