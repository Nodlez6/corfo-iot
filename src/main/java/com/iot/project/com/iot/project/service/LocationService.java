package com.iot.project.com.iot.project.service;

import java.util.List;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.exception.NotFoundException;

public class LocationService {
    //private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        //return locationRepository.getLocations();
        return null;
    }

    public Location getLocationById(Long id) {
        //Location location = locationRepository.findById(id);
//        if(!location){
//            throw NotFoundException("Location not found");
//        }
        return null;
    }

    public Location createLocation(Location location, Company company) {
        //location.setCompany(company);
        //return locationRepository.save(location);
        return null;
    }

    public Location updateLocation(Long id, Location locationDetails) {
//        Location location = getLocationById(id);
//        location.setName(locationDetails.getName());
//        location.setAddress(locationDetails.getAddress());
//        return locationRepository.save(location);
        return null;
    }

    public boolean deleteLocation(Long id) {
        //Location location = locationRepository.getLocationById(id);
        //locationRepository.delete(location);
        return true;
    }
}
