package com.iot.project.com.iot.project.repository;



import java.time.Instant;
import java.util.List;

import com.iot.project.com.iot.project.entity.SensorDataDetail;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SensorDataHeaderRepository extends JpaRepository<SensorDataHeader, Long> {
    @Query("""
       SELECT h
       FROM SensorDataHeader h
       JOIN SensorDataDetail d ON d.sensorDataHeaderId = h.id
       JOIN Sensor s ON s.sensorId = h.sensorId
       JOIN Location l ON l.locationId = s.locationId
       WHERE l.companyId = :companyId
       AND s.sensorId IN :sensorIds
       AND d.timestamp BETWEEN :from AND :to
        """)
    List<SensorDataHeader> findBySensorIdsAndDateRangeAndCompanyApiKey(
            @Param("companyId") Long companyId,
            @Param("sensorIds") List<Long> sensorIds,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

    @Query("""
       SELECT DISTINCT h
       FROM SensorDataHeader h
       JOIN Sensor s ON s.sensorId = h.sensorId
       JOIN Location l ON l.locationId = s.locationId
       LEFT JOIN FETCH h.details d
       LEFT JOIN FETCH d.metric m
       WHERE l.companyId = :companyId
        """)
    List<SensorDataHeader> findAllByCompanyId(@Param("companyId") Long companyId);
}


