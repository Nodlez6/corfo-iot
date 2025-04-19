package com.iot.project.com.iot.project.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.company.CompanyResponseDto;
import com.iot.project.com.iot.project.dto.company.CreateCompanyRequest;
import com.iot.project.com.iot.project.dto.company.UpdateCompanyRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ActionType;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SecurityRequirement(name = "ApiKeyAuth")
@RestController
@Validated
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Operations related to company management")
public class CompanyController {

    private final CompanyService companyService;
    private final AppProperties appProperties;

    private String APP_NAME;

    @PostConstruct
    public void init() {
        this.APP_NAME = appProperties.getResponseKey();
    }

    @Operation(summary = "Get all companies", description = "Returns a list of all companies in the system, wrapped in a ServiceResponse.", responses = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<CompanyResponseDto>>> getAllCompanies() {
        List<Company> companies = companyService.getCompanies();
        List<CompanyResponseDto> responseDtos = companies.stream()
                .map(company -> new CompanyResponseDto(company, ActionType.LIST_ALL))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANIES, responseDtos));
    }

    @Operation(summary = "Get company by ID", description = "Returns a specific company's data by its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<CompanyResponseDto>> getCompanyById(
            @Parameter(description = "Company ID") @PathVariable("id") Long id) {
        Company company = companyService.getCompanyById(id);
        CompanyResponseDto responseDto = new CompanyResponseDto(company, ActionType.GET_BY_ID);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANY, responseDto));
    }

    @Operation(summary = "Get company by API key (path)", description = "Retrieves a company using the provided API key in the path.", responses = {
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/apikey/{apiKey}")
    public ResponseEntity<ServiceResponse<CompanyResponseDto>> getCompanyByApiKey(
            @Parameter(description = "Company API key") @PathVariable("apiKey") UUID apiKey) {
        Company company = companyService.getCompanyByApiKey(apiKey);
        CompanyResponseDto responseDto = new CompanyResponseDto(company, ActionType.GET_BY_APIKEY);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANY, responseDto));
    }

    @Operation(summary = "Create a new company", description = "Creates a new company using the provided data and returns the created entity.", responses = {
            @ApiResponse(responseCode = "201", description = "Company created successfully")
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<CompanyResponseDto>> createCompany(
            @Parameter(description = "Company creation data") @RequestBody CreateCompanyRequest request) {
        log.info("Create Company -> {}", request);
        Company created = companyService.createCompany(request.getCompanyName(), request.getApiKey());
        CompanyResponseDto responseDto = new CompanyResponseDto(created, ActionType.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANY, responseDto));
    }

    @Operation(summary = "Update an existing company", description = "Updates an existing company's data by its ID.", responses = {
            @ApiResponse(responseCode = "201", description = "Company updated successfully")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<CompanyResponseDto>> updateCompany(
            @Parameter(description = "Company ID") @PathVariable("id") Long id,
            @Parameter(description = "New company data") @RequestBody UpdateCompanyRequest request) {
        Company updated = companyService.updateCompany(id, request.getCompanyName(), request.getApiKey());
        CompanyResponseDto responseDto = new CompanyResponseDto(updated, ActionType.UPDATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANY, responseDto));
    }

    @Operation(summary = "Delete a company", description = "Deletes a company from the system using its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Company deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<CompanyResponseDto>> deleteCompany(
            @Parameter(description = "ID of the company to delete") @PathVariable("id") Long id) {
        Company companyDeleted = companyService.deleteCompanyById(id);
        CompanyResponseDto responseDto = new CompanyResponseDto(companyDeleted, ActionType.DELETED);
        return ResponseEntity.ok(new ServiceResponse<>(APP_NAME, ActionMethod.COMPANY, responseDto));
    }
}