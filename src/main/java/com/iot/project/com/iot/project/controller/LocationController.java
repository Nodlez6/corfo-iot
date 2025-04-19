package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.LocationResponseDto;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.service.LocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@SecurityRequirement(name = "ApiKeyAuth")
@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Validated
@Tag(name = "Locations", description = "Operations related to location management")
public class LocationController {
    // Required dependency injections
    private final LocationService locationService;
    private final AppProperties appProperties;

    // Variable to store the application name (retrieved from AppProperties)
    private String APP_NAME;

    // Method executed after bean construction to initialize APP_NAME
    @PostConstruct
    public void init() {
        this.APP_NAME = appProperties.getResponseKey();
    }

    @Operation(summary = "Get all locations", description = "Returns a list of all locations associated with the authenticated company", responses = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<LocationResponseDto>>> getAllLocations(HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        List<Location> locations = locationService.getAllLocations(companyId);
        List<LocationResponseDto> responseDtos = locations.stream()
                .map(location -> new LocationResponseDto(location, ActionType.LIST_ALL))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.LOCATIONS, responseDtos));
    }

    @Operation(summary = "Get location by ID", description = "Returns the data of a specific location by its ID for the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Location retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<LocationResponseDto>> getLocationById(
            @Parameter(description = "Location ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location location = locationService.getLocationById(id, companyId);
        LocationResponseDto responseDto = new LocationResponseDto(location, ActionType.GET_BY_ID);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.LOCATION, responseDto));
    }

    @Operation(summary = "Create a new location", description = "Creates a new location for the authenticated company using the provided data.", responses = {
            @ApiResponse(responseCode = "201", description = "Location created successfully")
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<LocationResponseDto>> createLocation(
            @Parameter(description = "Location creation data") @RequestBody @Validated CreateLocationRequest request,
            HttpServletRequest httpRequest) {
        log.info("Create Location -> {}", request);
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location created = locationService.createLocation(request, companyId);
        LocationResponseDto responseDto = new LocationResponseDto(created, ActionType.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ServiceResponse<>(APP_NAME, ActionMethod.LOCATION, responseDto));
    }

    @Operation(summary = "Update an existing location", description = "Updates an existing location's data by its ID for the authenticated company.", responses = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<LocationResponseDto>> updateLocation(
            @Parameter(description = "Location ID") @PathVariable Long id,
            @Parameter(description = "New location data") @RequestBody @Validated UpdateLocationRequest request,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location updated = locationService.updateLocation(id, request, companyId);
        LocationResponseDto responseDto = new LocationResponseDto(updated, ActionType.UPDATED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.LOCATION, responseDto));
    }

    @Operation(summary = "Delete a location", description = "Deletes a location associated with the authenticated company using its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Location deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<LocationResponseDto>> deleteLocation(
            @Parameter(description = "ID of the location to delete") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long companyId = (Long) httpRequest.getAttribute("authenticatedCompanyId");
        Location locationDeleted = locationService.deleteLocation(id, companyId);
        LocationResponseDto responseDto = new LocationResponseDto(locationDeleted, ActionType.DELETED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.LOCATION, responseDto));
    }
}
