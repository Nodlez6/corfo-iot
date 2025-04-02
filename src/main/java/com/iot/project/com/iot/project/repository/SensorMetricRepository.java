package com.iot.project.com.iot.project.repository;

import java.util.Optional;

import com.iot.project.com.iot.project.entity.SensorMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMetricRepository extends JpaRepository<SensorMetric, Long> {
    Optional<SensorMetric> findByMetricName(String name);
}
