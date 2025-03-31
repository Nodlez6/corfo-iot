package com.iot.project.com.iot.project.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensor", schema = "public", indexes = @Index(name = "idx_api_key_sensor", columnList = "api_key", unique = true))
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String category;

    String locationMeta;

    @Column(name = "api_key")
    String apiKey;
    
    
//    @Column(name = "location_id")
//    Long locationId;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="location_id")
//    Location location;

}
