package com.iot.project.com.iot.project.entity;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensor_data_header", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_data_header_id")
    @JsonIgnore
    private Long id;

    @Column(name = "sensor_id", nullable = false)
    private Long sensorId;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sensor_data_header_id", referencedColumnName = "sensor_data_header_id", nullable = false, insertable = false, updatable = false)
    private Set<SensorDataDetail> details;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
