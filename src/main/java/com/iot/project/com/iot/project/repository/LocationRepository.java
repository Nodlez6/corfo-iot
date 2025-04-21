package com.iot.project.com.iot.project.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.iot.project.com.iot.project.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;



@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByCompanyId(Long id);
    List<Location> findAllByCompanyIdOrderByLocationIdAsc(Long companyId);
    Optional<Location> findByLocationIdAndCompanyId(Long locationId, Long companyId);
	//@Query( value = "select l from Location l join Company c on l.companyId = c.id  where c.apiKey = ?1 ")
	//List<Location> findByCompanyApiKey(String apiKey);
}


