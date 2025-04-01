package com.iot.project.com.iot.project.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.ApiKeyAlreadyExistsException;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.API_KEY_ALREADY_EXISTS;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;

@AllArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final AdminService adminService;

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public Company getCompanyById(long id) throws NotFoundException {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
    }

    public Company createCompany(String companyName, UUID apiKey, Admin admin) {
        adminService.authenticateAdmin(admin.getUsername(), admin.getPassword());
        if( this.existsThisApiKey(apiKey) ){
            throw new ApiKeyAlreadyExistsException(API_KEY_ALREADY_EXISTS);
        }

        Company newCompany = Company.builder()
                .name(companyName)
                .apiKey(apiKey)
                .build();

        return companyRepository.save(newCompany);
    }

    public Company getCompanyByApiKey(UUID apiKey) {
        return companyRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));
    }

    public boolean existsThisApiKey(UUID apiKey) {
        return companyRepository.findByApiKey(apiKey).isPresent();
    }


}
