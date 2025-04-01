package com.iot.project.com.iot.project.controller;


import java.util.List;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.CreateCompanyRequest;
import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/apikey/{apiKey}")
    public ResponseEntity<Company> getCompanyByApiKey(@PathVariable("apiKey") UUID apiKey) {
        Company company = companyService.getCompanyByApiKey(apiKey);
        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody @Validated CreateCompanyRequest request) {
        Admin admin = Admin.builder()
                .username(request.getAdminUsername())
                .password(request.getAdminPassword())
                .build();
        Company created = companyService.createCompany(
                request.getCompanyName(),
                request.getApiKey(),
                admin
        );
        return ResponseEntity.ok(created);
    }
}
