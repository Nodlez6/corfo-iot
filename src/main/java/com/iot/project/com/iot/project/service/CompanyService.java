package com.iot.project.com.iot.project.service;

import java.util.Map;

import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.entity.Company;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CompanyService {
    //private final CompanyRepository companyRepository;
    private final AdminService adminService;

    public Company getCompanies(){
        //return companyRepository.getCompanies()
        return null;
    }

    public Company getCompanyById(long id){
        //return companyRepository.getCompanyById(id);
        return null;
    }

    public Company createCompany(Company company, Admin admin){
        adminService.authenticateAdmin(admin.getUsername(), admin.getPassword());
        //companyRepository.save(company)
        return null;
    }

    //una idea, se puede cmabiar por PUT en vez de patch
    public Company updateCompany(long id, Map<String, Object> updates,  Admin admin) {
        adminService.authenticateAdmin(admin.getUsername(), admin.getPassword());
        //Company existingCompany = companyRepository.findById(id);
//        if (existingCompany == null) {
//            throw new NotFoundException("Company not found");
//        }
//
//        if (updates.containsKey("name")) {
//            existingCompany.setName((String) updates.get("name"));
//        }
//        if (updates.containsKey("address")) {
//            existingCompany.setAddress((String) updates.get("address"));
//        }

       // return companyRepository.save(existingCompany);
        return null;
    }

    public Company getCompanyByApiKey(String apiKey){
        //companyRepository.getCompanyByApiKey(apiKey)
        return null;
    }
}
