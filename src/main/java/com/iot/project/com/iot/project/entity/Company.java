package com.iot.project.com.iot.project.entity;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "company", schema = "public")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(name = "api_key")
    UUID apiKey;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="company")
    Set<Location> locations;
}
