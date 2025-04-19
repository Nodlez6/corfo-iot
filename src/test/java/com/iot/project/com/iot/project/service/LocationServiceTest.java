//package com.iot.project.com.iot.project.service;
//
//import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
//import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
//import com.iot.project.com.iot.project.entity.Location;
//import com.iot.project.com.iot.project.exception.BadRequestException;
//import com.iot.project.com.iot.project.exception.NotFoundException;
//import com.iot.project.com.iot.project.repository.LocationRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static com.iot.project.com.iot.project.exception.ConstantsExceptions.BAD_REQUEST_LOCATION_SERVICE;
//import static com.iot.project.com.iot.project.exception.ConstantsExceptions.ENTITY_NOT_FOUND_BY_COMPANY;
//import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class LocationServiceTest {
//
//    @Mock
//    private LocationRepository locationRepository;
//
//    @InjectMocks
//    private LocationService locationService;
//
//    @Test
//    public void testGetAllLocations() {
//        Long companyId = 1L;
//        List<Location> locations = Arrays.asList(
//                Location.builder().locationName("locationName1").companyId(companyId).build(),
//                Location.builder().locationName("locationName2").companyId(companyId).build()
//        );
//        when(locationRepository.findAllByCompanyId(companyId)).thenReturn(locations);
//
//        List<Location> result = locationService.getAllLocations(companyId);
//
//        assertEquals(2, result.size());
//        verify(locationRepository).findAllByCompanyId(companyId);
//    }
//
//    @Test
//    public void testGetLocationByIdFound() {
//        Long companyId = 1L;
//        Long locationId = 100L;
//        Location location = Location.builder()
//                .locationName("locationName")
//                .locationAddress("locationAddress")
//                .locationCountry("locationCountry")
//                .locationCity("locationCity")
//                .locationMeta("locationMeta")
//                .companyId(companyId)
//                .build();
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
//
//        Location result = locationService.getLocationById(locationId, companyId);
//
//        assertNotNull(result);
//        assertEquals("locationName", result.getLocationName());
//        assertEquals("locationAddress", result.getLocationAddress());
//        verify(locationRepository).findById(locationId);
//    }
//
//    @Test
//    public void testGetLocationByIdNotFound() {
//        Long companyId = 1L;
//        Long locationId = 100L;
//        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () ->
//                locationService.getLocationById(locationId, companyId)
//        );
//        assertEquals(RESOURCE_NOT_FOUND, exception.getMessage());
//        verify(locationRepository).findById(locationId);
//    }
//
//    @Test
//    public void testGetLocationByIdCompanyMismatch() {
//        Long companyId = 1L;
//        Long wrongCompanyId = 2L;
//        Long locationId = 100L;
//        Location location = Location.builder()
//                .locationName("locationName")
//                .companyId(wrongCompanyId)
//                .build();
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
//
//        BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                locationService.getLocationById(locationId, companyId)
//        );
//        assertEquals(BAD_REQUEST_LOCATION_SERVICE, exception.getMessage());
//        verify(locationRepository).findById(locationId);
//    }
//
//    @Test
//    public void testCreateLocation() {
//        Long companyId = 1L;
//        CreateLocationRequest request = CreateLocationRequest.builder()
//                .locationName("locationName")
//                .locationAddress("locationAddress")
//                .locationCountry("locationCountry")
//                .locationCity("locationCity")
//                .locationMeta("locationMeta")
//                .build();
//
//        Location savedLocation = Location.builder()
//                .locationName(request.getLocationName())
//                .locationAddress(request.getLocationAddress())
//                .locationCountry(request.getLocationCountry())
//                .locationCity(request.getLocationCity())
//                .locationMeta(request.getLocationMeta())
//                .companyId(companyId)
//                .build();
//        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);
//
//        Location result = locationService.createLocation(request, companyId);
//
//        assertNotNull(result);
//        assertEquals("locationName", result.getLocationName());
//        assertEquals("locationAddress", result.getLocationAddress());
//        verify(locationRepository).save(any(Location.class));
//    }
//
//    @Test
//    public void testUpdateLocationValid() {
//        Long companyId = 1L;
//        Long locationId = 100L;
//        UpdateLocationRequest request = UpdateLocationRequest.builder()
//                .locationName("updatedLocationName")
//                .locationAddress("updatedLocationAddress")
//                .locationCountry("updatedLocationCountry")
//                .locationCity("updatedLocationCity")
//                .locationMeta("updatedLocationMeta")
//                .build();
//
//        Location existingLocation = Location.builder()
//                .locationName("oldLocationName")
//                .locationAddress("oldLocationAddress")
//                .locationCountry("oldLocationCountry")
//                .locationCity("oldLocationCity")
//                .locationMeta("oldLocationMeta")
//                .companyId(companyId)
//                .build();
//
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(existingLocation));
//        when(locationRepository.save(existingLocation)).thenReturn(existingLocation);
//
//        Location result = locationService.updateLocation(locationId, request, companyId);
//
//        assertNotNull(result);
//        assertEquals("updatedLocationName", result.getLocationName());
//        assertEquals("updatedLocationAddress", result.getLocationAddress());
//        assertEquals("updatedLocationCountry", result.getLocationCountry());
//        assertEquals("updatedLocationCity", result.getLocationCity());
//        assertEquals("updatedLocationMeta", result.getLocationMeta());
//        verify(locationRepository).findById(locationId);
//        verify(locationRepository).save(existingLocation);
//    }
//
//    @Test
//    public void testUpdateLocationCompanyMismatch() {
//        Long companyId = 1L;
//        Long wrongCompanyId = 2L;
//        Long locationId = 100L;
//        UpdateLocationRequest request = UpdateLocationRequest.builder()
//                .locationName("updatedLocationName")
//                .locationAddress("updatedLocationAddress")
//                .locationCountry("updatedLocationCountry")
//                .locationCity("updatedLocationCity")
//                .locationMeta("updatedLocationMeta")
//                .build();
//
//        Location existingLocation = Location.builder()
//                .locationName("oldLocationName")
//                .companyId(wrongCompanyId)
//                .build();
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(existingLocation));
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () ->
//                locationService.updateLocation(locationId, request, companyId)
//        );
//        assertEquals("Location " + ENTITY_NOT_FOUND_BY_COMPANY, exception.getMessage());
//        verify(locationRepository).findById(locationId);
//    }
//
//    @Test
//    public void testDeleteLocationValid() {
//        Long companyId = 1L;
//        Long locationId = 100L;
//        Location location = Location.builder()
//                .locationName("locationName")
//                .companyId(companyId)
//                .build();
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
//
//        assertDoesNotThrow(() -> locationService.deleteLocation(locationId, companyId));
//        verify(locationRepository).findById(locationId);
//        verify(locationRepository).delete(location);
//    }
//
//    @Test
//    public void testDeleteLocationCompanyMismatch() {
//        Long companyId = 1L;
//        Long wrongCompanyId = 2L;
//        Long locationId = 100L;
//        Location location = Location.builder()
//                .locationName("locationName")
//                .companyId(wrongCompanyId)
//                .build();
//        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
//
//        BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                locationService.deleteLocation(locationId, companyId)
//        );
//        assertEquals(BAD_REQUEST_LOCATION_SERVICE, exception.getMessage());
//        verify(locationRepository).findById(locationId);
//    }
//}
