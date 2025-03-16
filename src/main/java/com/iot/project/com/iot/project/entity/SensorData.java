package com.iot.project.com.iot.project.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //Esto podria cambiar a tipo JSON y generar su tabla correspondiente
    String data;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sensor_id", nullable=false)
    Sensor sensor;
}
