package com.iot.project.com.iot.project.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensor_data_detail", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_data_detail_id")
    private Long id;

    @Column(name = "sensor_data_header_id", nullable = false, updatable = false)
    private Long sensorDataHeaderId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_metric_id", nullable = false)
    private SensorMetric metric;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
