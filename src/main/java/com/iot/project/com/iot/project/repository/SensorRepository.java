package com.iot.project.com.iot.project.repository;



import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Sensor;



@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
	Sensor findByApiKey(UUID apiKey);
}


