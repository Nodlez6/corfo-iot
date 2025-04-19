package com.iot.project.com.iot.project.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @Column(name = "sensor_data_header_id", nullable = false, updatable = false)
    private Long sensorDataHeaderId;

    // @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_metric_id", nullable = false)
    private SensorMetric metric;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
