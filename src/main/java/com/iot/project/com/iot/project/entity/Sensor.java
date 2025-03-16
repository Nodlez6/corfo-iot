package com.iot.project.com.iot.project.entity;

import java.util.Date;

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
@Table(name = "sensor", schema = "public")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String category;

    String locationMeta;

    @Column(name = "api_key")
    String apiKey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="location_id", nullable=false)
    Location location;

}
