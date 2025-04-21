package com.iot.project.com.iot.project.service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.ApiKeyAlreadyExistsException;
import com.iot.project.com.iot.project.exception.ConstantsExceptions;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@AllArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public Company getCompanyById(long id) throws NotFoundException {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantsExceptions.COMPANY_ID_NOT_FOUND));
    }

    public Company getCompanyByApiKey(UUID apiKey) {
        return companyRepository.findByCompanyApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException(ConstantsExceptions.COMPANY_APIKEY_NOT_FOUND));
    }

    public Company createCompany(String companyName, UUID apiKey) {
        if( this.existsCompanyApiKey(apiKey) ){
            throw new ApiKeyAlreadyExistsException(ConstantsExceptions.API_KEY_ALREADY_EXISTS);
        }
        Company newCompany = Company.builder()
                .companyName(companyName)
                .companyApiKey(apiKey)
                .locations(new HashSet<>()) 
                .build();
        return companyRepository.save(newCompany);
    }

    public Company deleteCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantsExceptions.COMPANY_ID_NOT_FOUND));
        companyRepository.delete(company);
        return company;
    }

    public Company updateCompany(Long id, String companyName, UUID apiKey) {
        Company existing = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantsExceptions.COMPANY_ID_NOT_FOUND));

        UUID oldApiKey = existing.getCompanyApiKey();
        if(!oldApiKey.equals(apiKey)){
            if( this.existsCompanyApiKey(apiKey) ){
                throw new ApiKeyAlreadyExistsException(ConstantsExceptions.API_KEY_ALREADY_EXISTS);
            }
        }
        existing.setCompanyName(companyName);
        existing.setCompanyApiKey(apiKey);
        return companyRepository.save(existing);
    }

    
    public boolean existsCompanyApiKey(UUID apiKey) {
        return companyRepository.findByCompanyApiKey(apiKey).isPresent();
    }

}
