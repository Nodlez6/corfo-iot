package com.iot.project.com.iot.project.service;

import java.util.List;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.location.CreateLocationRequest;
import com.iot.project.com.iot.project.dto.location.UpdateLocationRequest;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.exception.BadRequestException;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.CompanyRepository;
import com.iot.project.com.iot.project.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.BAD_REQUEST_LOCATION_SERVICE;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LocationService {

        private final LocationRepository locationRepository;
        private final CompanyRepository companyRepository;

        public List<Location> getAllLocations() {
            return locationRepository.findAll();
        }

        public Location getLocationById(Long id, UUID company_api_key) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
            Company company = companyRepository.findByCompanyApiKey(company_api_key)
                    .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

            if( !location.getCompanyId().equals(company.getCompanyId()) ){
                throw new BadRequestException(BAD_REQUEST_LOCATION_SERVICE);
            }
            return location;
        }

        public Location createLocation(CreateLocationRequest request, UUID company_api_key) {
            Company company = companyRepository.findByCompanyApiKey(company_api_key)
                    .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
            Location location = Location.builder()
                    .locationName(request.getLocationName())
                    .locationAddress(request.getLocationAddress())
                    .locationCountry(request.getLocationCountry())
                    .locationCity(request.getLocationCity())
                    .locationMeta(request.getLocationMeta())
                    .companyId(company.getCompanyId())
                    .build();
            return locationRepository.save(location);
        }

    public Location updateLocation(Long id, UpdateLocationRequest request, UUID company_api_key ) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        Company company = companyRepository.findByCompanyApiKey(company_api_key)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        existingLocation.setLocationName(request.getLocationName());
        existingLocation.setLocationAddress(request.getLocationAddress());
        existingLocation.setLocationCountry(request.getLocationCountry());
        existingLocation.setLocationCity(request.getLocationCity());
        existingLocation.setLocationMeta(request.getLocationMeta());
        existingLocation.setCompanyId(company.getCompanyId());

        return locationRepository.save(existingLocation);
    }


        public void deleteLocation(Long id, UUID company_api_key) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

            Company company = companyRepository.findByCompanyApiKey(company_api_key)
                    .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

            if( !location.getCompanyId().equals(company.getCompanyId()) ){
                throw new BadRequestException(BAD_REQUEST_LOCATION_SERVICE);
            }
            locationRepository.delete(location);
        }
}
