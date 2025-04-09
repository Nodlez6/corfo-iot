package com.iot.project.com.iot.project.controller;

import java.util.List;

import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Validated
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations(HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<Location> locations = locationService.getAllLocations(companyId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id,
                                                    HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location location = locationService.getLocationById(id, companyId);
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Validated CreateLocationRequest request,
                                                   HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location created = locationService.createLocation(request, companyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id,
                                                   @RequestBody @Validated UpdateLocationRequest request,
                                                   HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location updated = locationService.updateLocation(id, request, companyId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long companyId =  (Long) httpRequest.getAttribute("authenticatedCompanyId");
        locationService.deleteLocation(id, companyId);
        return ResponseEntity.noContent().build();
    }
}
