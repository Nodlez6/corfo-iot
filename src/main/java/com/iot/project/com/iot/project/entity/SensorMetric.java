package com.iot.project.com.iot.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensor_metric", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_metric_id")
    private Long id;

    @Column(name = "metric_name", nullable = false, unique = true)
    private String metricName;

}
