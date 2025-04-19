package com.iot.project.com.iot.project.controller;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.LocationResponseDto;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.service.LocationService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private LocationController controller;

    private static final String APP_NAME = "iotService";
    private static final Long COMPANY_ID = 42L;

    @BeforeEach
    void setUp() {
        when(appProperties.getResponseKey()).thenReturn(APP_NAME);
        controller.init();
        when(httpRequest.getAttribute("authenticatedCompanyId")).thenReturn(COMPANY_ID);
    }

    @Test
    void getAllLocationsShouldReturnListWrapped() {
        Location loc1 = Location.builder().locationId(1L).locationName("Loc A").build();
        Location loc2 = Location.builder().locationId(2L).locationName("Loc B").build();
        when(locationService.getAllLocations(COMPANY_ID)).thenReturn(List.of(loc1, loc2));

        ResponseEntity<ServiceResponse<List<LocationResponseDto>>> response = controller.getAllLocations(httpRequest);
        ServiceResponse<List<LocationResponseDto>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<LocationResponseDto>>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, List<LocationResponseDto>> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.LOCATIONS.name()));
        List<LocationResponseDto> dataList = innerMap.get(ActionMethod.LOCATIONS.name());
        assertEquals(2, dataList.size());
        assertEquals(loc1.getLocationId(), dataList.get(0).getLocationId());
    }

    @Test
    void getLocationByIdShouldReturnWrappedLocation() {
        Location loc = Location.builder().locationId(5L).locationName("TestLoc").build();
        when(locationService.getLocationById(5L, COMPANY_ID)).thenReturn(loc);

        ResponseEntity<ServiceResponse<LocationResponseDto>> response = controller.getLocationById(5L, httpRequest);
        ServiceResponse<LocationResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, LocationResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, LocationResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.LOCATION.name()));
        LocationResponseDto dto = innerMap.get(ActionMethod.LOCATION.name());
        assertEquals(loc.getLocationId(), dto.getLocationId());
    }

    @Test
    void createLocationShouldReturnCreatedStatus() {
        CreateLocationRequest req = CreateLocationRequest.builder().locationName("NewLoc").build();
        Location created = Location.builder().locationId(10L).locationName("NewLoc").build();
        when(locationService.createLocation(req, COMPANY_ID)).thenReturn(created);

        ResponseEntity<ServiceResponse<LocationResponseDto>> response = controller.createLocation(req, httpRequest);
        ServiceResponse<LocationResponseDto> body = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, LocationResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, LocationResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.LOCATION.name()));
        LocationResponseDto dto = innerMap.get(ActionMethod.LOCATION.name());
        assertEquals(created.getLocationId(), dto.getLocationId());
    }

    @Test
    void updateLocationShouldReturnOkStatus() {
        UpdateLocationRequest req = UpdateLocationRequest.builder().locationName("UpdLoc").build();
        Location updated = Location.builder().locationId(7L).locationName("UpdLoc").build();
        when(locationService.updateLocation(7L, req, COMPANY_ID)).thenReturn(updated);

        ResponseEntity<ServiceResponse<LocationResponseDto>> response = controller.updateLocation(7L, req, httpRequest);
        ServiceResponse<LocationResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, LocationResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, LocationResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.LOCATION.name()));
        LocationResponseDto dto = innerMap.get(ActionMethod.LOCATION.name());
        assertEquals(updated.getLocationName(), dto.getName());
    }

    @Test
    void deleteLocationShouldReturnOkStatus() {
        Location deleted = Location.builder().locationId(8L).locationName("ToDelete").build();
        when(locationService.deleteLocation(8L, COMPANY_ID)).thenReturn(deleted);

        ResponseEntity<ServiceResponse<LocationResponseDto>> response = controller.deleteLocation(8L, httpRequest);
        ServiceResponse<LocationResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, LocationResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, LocationResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.LOCATION.name()));
        LocationResponseDto dto = innerMap.get(ActionMethod.LOCATION.name());
        assertEquals(deleted.getLocationId(), dto.getLocationId());
    }
}