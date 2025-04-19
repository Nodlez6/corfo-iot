package com.iot.project.com.iot.project.service;

import java.util.HashSet;
import java.util.List;

import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.exception.BadRequestException;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static com.iot.project.com.iot.project.exception.ConstantsExceptions.LOCATION_ID_NOT_FOUND;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.BAD_REQUEST_LOCATION_SERVICE;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.LOCATION_NOT_FOUND_FOR_COMPANY;

@Service
@RequiredArgsConstructor
public class LocationService {

        private final LocationRepository locationRepository;




        public List<Location> getAllLocations(Long companyId) {
            return locationRepository.findAllByCompanyIdOrderByLocationIdAsc(companyId);
        }


        public Location getLocationById(Long id, Long companyId) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(LOCATION_ID_NOT_FOUND));

            if( !location.getCompanyId().equals(companyId) ){
                throw new BadRequestException(BAD_REQUEST_LOCATION_SERVICE);
            }
            return location;
        }

        public Location createLocation(CreateLocationRequest request, Long companyId) {
            Location location = Location.builder()
                    .locationName(request.getLocationName())
                    .locationAddress(request.getLocationAddress())
                    .locationCountry(request.getLocationCountry())
                    .locationCity(request.getLocationCity())
                    .locationMeta(request.getLocationMeta())
                    .companyId(companyId)
                    .sensors(new HashSet<>())
                    .build();
            return locationRepository.save(location);
        }


        public Location updateLocation(Long id, UpdateLocationRequest request, Long companyId) {
            Location existingLocation = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(LOCATION_ID_NOT_FOUND));

            if (!existingLocation.getCompanyId().equals(companyId)){
                throw new NotFoundException(LOCATION_NOT_FOUND_FOR_COMPANY);
            }
            existingLocation.setLocationName(request.getLocationName());
            existingLocation.setLocationAddress(request.getLocationAddress());
            existingLocation.setLocationCountry(request.getLocationCountry());
            existingLocation.setLocationCity(request.getLocationCity());
            existingLocation.setLocationMeta(request.getLocationMeta());
            existingLocation.setCompanyId(companyId);

            return locationRepository.save(existingLocation);
        }



        public Location deleteLocation(Long id, Long companyId) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(LOCATION_ID_NOT_FOUND));

            if( !location.getCompanyId().equals(companyId) ){
                throw new BadRequestException(LOCATION_NOT_FOUND_FOR_COMPANY);
            }
            locationRepository.delete(location);
            return location;
        }
}
