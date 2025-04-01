package com.iot.project.com.iot.project.repository;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Sensor;



@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
	Optional<Sensor> findBySensorApiKey(UUID apiKey);

	List<Sensor> findAllByLocationIdIn(List<Long> locationIds);

	@Query("""
	   SELECT s
	   FROM Sensor s, Location l
	   WHERE s.locationId = l.locationId
		 AND s.sensorId = :sensorId
		 AND l.companyId = :companyId
	""")
	Optional<Sensor> findBySensorIdAndCompanyId(@Param("sensorId") Long sensorId, @Param("companyId") Long companyId);

	@Query("SELECT s FROM Sensor s, Location l WHERE s.locationId = l.locationId AND l.companyId = :companyId")
	List<Sensor> findAllByCompanyId(@Param("companyId") Long companyId);

	@Query("""
			SELECT s FROM Sensor s, Location l
			WHERE s.locationId = l.locationId
			AND s.sensorId = :sensorId
			AND s.locationId = :locationId
			AND l.companyId = :companyId
			""")
	Optional<Sensor> findBySensorIdAndLocationIdAndCompanyId(@Param("sensorId") Long sensorId,
															 @Param("locationId") Long locationId,
															 @Param("companyId") Long companyId);
}


