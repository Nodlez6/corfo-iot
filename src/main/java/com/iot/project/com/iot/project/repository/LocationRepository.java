package com.iot.project.com.iot.project.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Location;



@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByCompanyId(Long id);
    Optional<Location> findByLocationIdAndCompanyId(Long locationId, Long companyId);
	//@Query( value = "select l from Location l join Company c on l.companyId = c.id  where c.apiKey = ?1 ")
	//List<Location> findByCompanyApiKey(String apiKey);
}


