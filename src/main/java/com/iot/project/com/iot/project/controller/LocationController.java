package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Validated
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id,
                                                    @RequestHeader(value = "X-API-KEY", required = true) String companyApiKey) {
        Location location = locationService.getLocationById(id, UUID.fromString(companyApiKey));
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Validated CreateLocationRequest request,
                                                   @RequestHeader(value = "X-API-KEY", required = true) String companyApiKey) {
        Location created = locationService.createLocation(request, UUID.fromString(companyApiKey));
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id,
                                                   @RequestBody @Validated UpdateLocationRequest request,
                                                   @RequestHeader(value = "X-API-KEY", required = true) String companyApiKey) {
        Location updated = locationService.updateLocation(id, request, UUID.fromString(companyApiKey));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id, @RequestHeader(value = "X-API-KEY", required = true) String companyApiKey) {
        locationService.deleteLocation(id, UUID.fromString(companyApiKey));
        return ResponseEntity.noContent().build();
    }
}
