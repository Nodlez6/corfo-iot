package com.iot.project.com.iot.project.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorData;



@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
	SensorData findBySensorId(Long sensorId);
}


