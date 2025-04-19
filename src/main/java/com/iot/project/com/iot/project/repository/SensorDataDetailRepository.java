package com.iot.project.com.iot.project.repository;

import com.iot.project.com.iot.project.entity.SensorDataDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorDataDetailRepository  extends JpaRepository<SensorDataDetail, Long> {

    void deleteBySensorDataHeaderIdIn(List<Long> headerIds);

    @Modifying
    @Query("DELETE FROM SensorDataDetail d WHERE d.sensorDataHeaderId = :headerId")
    void deleteBySensorDataHeaderId(@Param("headerId") Long headerId);
}
