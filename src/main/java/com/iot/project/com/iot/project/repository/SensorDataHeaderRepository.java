package com.iot.project.com.iot.project.repository;



import java.time.Instant;
import java.util.List;

import com.iot.project.com.iot.project.entity.SensorDataHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SensorDataHeaderRepository extends JpaRepository<SensorDataHeader, Long> {

}


