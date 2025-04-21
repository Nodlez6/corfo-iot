package com.iot.project.com.iot.project.dto.company;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Location;

import lombok.Getter;


@Getter
public class CompanyResponseDto {
    
    private ActionType action;
    private Long companyId;
    private String companyName;
    private UUID companyApiKey;
    private Set<Location> locations;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public CompanyResponseDto(Company company, ActionType action) {
        this.action = action;
        this.companyId = company.getCompanyId();
        this.companyName = company.getCompanyName();
        this.companyApiKey = company.getCompanyApiKey();
        this.locations = company.getLocations();
        this.timestamp = LocalDateTime.now();
    }
}
