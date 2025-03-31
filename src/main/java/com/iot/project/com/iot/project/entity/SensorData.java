package com.iot.project.com.iot.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "sensor_data", schema = "public")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //Esto podria cambiar a tipo JSON y generar su tabla correspondiente
    String data;
    
    @Column(name = "sensor_id")
    Long sensorId;
}
