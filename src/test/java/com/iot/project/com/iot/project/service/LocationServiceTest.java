package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.exception.BadRequestException;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getAllLocationsReturnsListFromRepository() {
        Long companyId = 10L;
        List<Location> expected = List.of(
                Location.builder().locationId(1L).companyId(companyId).build(),
                Location.builder().locationId(2L).companyId(companyId).build()
        );
        when(locationRepository.findAllByCompanyIdOrderByLocationIdAsc(companyId))
                .thenReturn(expected);
        assertEquals(expected, locationService.getAllLocations(companyId));
    }

    @Test
    void getLocationByIdReturnsWhenCompanyMatches() {
        Long companyId = 5L;
        Location loc = Location.builder().locationId(1L).companyId(companyId).build();
        when(locationRepository.findById(1L)).thenReturn(Optional.of(loc));
        assertEquals(loc, locationService.getLocationById(1L, companyId));
    }

    @Test
    void getLocationByIdThrowsNotFoundWhenIdMissing() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> locationService.getLocationById(1L, 5L));
        assertEquals(LOCATION_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getLocationByIdThrowsBadRequestWhenCompanyMismatch() {
        Location loc = Location.builder().locationId(1L).companyId(2L).build();
        when(locationRepository.findById(1L)).thenReturn(Optional.of(loc));
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> locationService.getLocationById(1L, 3L));
        assertEquals(BAD_REQUEST_LOCATION_SERVICE, ex.getMessage());
    }

    @Test
    void createLocationSavesAndReturnsEntity() {
        Long companyId = 7L;
        CreateLocationRequest req = CreateLocationRequest.builder()
                .locationName("LocA")
                .locationAddress("Addr")
                .locationCountry("CL")
                .locationCity("Santiago")
                .locationMeta("m")
                .build();
        Location toSave = Location.builder()
                .locationName(req.getLocationName())
                .locationAddress(req.getLocationAddress())
                .locationCountry(req.getLocationCountry())
                .locationCity(req.getLocationCity())
                .locationMeta(req.getLocationMeta())
                .companyId(companyId)
                .build();
        when(locationRepository.save(any(Location.class))).thenReturn(toSave);
        Location result = locationService.createLocation(req, companyId);
        assertEquals(toSave, result);
    }

    @Test
    void updateLocationReturnsUpdatedWhenCompanyMatches() {
        Long companyId = 8L;
        UpdateLocationRequest req = UpdateLocationRequest.builder()
                .locationName("New")
                .locationAddress("NewAddr")
                .locationCountry("PE")
                .locationCity("Lima")
                .locationMeta("x")
                .build();
        Location existing = Location.builder()
                .locationId(1L)
                .companyId(companyId)
                .locationName("Old")
                .build();
        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(existing)).thenReturn(existing);
        Location result = locationService.updateLocation(1L, req, companyId);
        assertEquals(req.getLocationName(), result.getLocationName());
        assertEquals(req.getLocationAddress(), result.getLocationAddress());
        assertEquals(req.getLocationCountry(), result.getLocationCountry());
        assertEquals(req.getLocationCity(), result.getLocationCity());
        assertEquals(req.getLocationMeta(), result.getLocationMeta());
    }

    @Test
    void updateLocationThrowsNotFoundWhenIdMissing() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> locationService.updateLocation(1L,
                        UpdateLocationRequest.builder().build(), 1L));
        assertEquals(LOCATION_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void updateLocationThrowsNotFoundWhenCompanyMismatch() {
        Long companyId = 9L;
        Location existing = Location.builder()
                .locationId(1L)
                .companyId(5L)
                .build();
        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> locationService.updateLocation(1L,
                        UpdateLocationRequest.builder().build(), companyId));
        assertEquals(LOCATION_NOT_FOUND_FOR_COMPANY, ex.getMessage());
    }

    @Test
    void deleteLocationReturnsWhenCompanyMatches() {
        Long companyId = 4L;
        Location loc = Location.builder().locationId(2L).companyId(companyId).build();
        when(locationRepository.findById(2L)).thenReturn(Optional.of(loc));
        Location result = locationService.deleteLocation(2L, companyId);
        assertEquals(loc, result);
        verify(locationRepository).delete(loc);
    }

    @Test
    void deleteLocationThrowsNotFoundWhenIdMissing() {
        when(locationRepository.findById(3L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> locationService.deleteLocation(3L, 1L));
        assertEquals(LOCATION_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void deleteLocationThrowsBadRequestWhenCompanyMismatch() {
        Location loc = Location.builder().locationId(5L).companyId(2L).build();
        when(locationRepository.findById(5L)).thenReturn(Optional.of(loc));
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> locationService.deleteLocation(5L, 3L));
        assertEquals(LOCATION_NOT_FOUND_FOR_COMPANY, ex.getMessage());
    }
}
