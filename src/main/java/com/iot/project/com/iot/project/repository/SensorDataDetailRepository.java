package com.iot.project.com.iot.project.repository;

import com.iot.project.com.iot.project.entity.SensorDataDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataDetailRepository  extends JpaRepository<SensorDataDetail, Long> {

    void deleteBySensorDataHeaderIdIn(List<Long> headerIds);

}
