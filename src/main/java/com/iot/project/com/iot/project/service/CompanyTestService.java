//package com.iot.project.com.iot.project.service;
//
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.iot.project.com.iot.project.entity.Admin;
//import com.iot.project.com.iot.project.entity.Company;
//import com.iot.project.com.iot.project.repository.AdminRepository;
//import com.iot.project.com.iot.project.repository.CompanyRepository;
//
//import lombok.RequiredArgsConstructor;
//
//
//@Service
//@RequiredArgsConstructor
//public class CompanyTestService {
//	private final CompanyRepository companyRepository;
//	private final AdminRepository adminRepository;
//	@Transactional
//	public Company create(String username, String passw, Company company) throws CustomException {
//		if(username == null || passw == null || company == null ) {
//			throw new CustomException(ExceptionDescription.REQUIRED_FIELDS_EXCEPTION, ExceptionDescription.REQUIRED_FIELDS_EXCEPTION_MSG);
//		}
//		Admin _admin = this.adminRepository.findByUsernameAndPassword(username, username);
//		if(_admin == null ) {
//			throw new CustomException(ExceptionDescription.INCORRECT_CREDENTIALS, ExceptionDescription.INCORRECT_CREDENTIALS_MSG);
//		}
//		return this.companyRepository.save(company);
//	}
//	@Transactional
//	public Company findCompany(Long id, String apiKey) throws CustomException {
//		Company _entity = null;
//		try {
//			if(id != null ) {
//				_entity = this.companyRepository.findById(id).orElse(null);
//			}
//			else if(apiKey != null ) {
//				_entity = this.companyRepository.findByApiKey(apiKey);
//			}
//			if(_entity == null) {
//				throw new CustomException(ExceptionDescription.NOT_FOUND, ExceptionDescription.NOT_FOUND_MSG);
//			}
//			return _entity;
//		} catch (CustomException e) {
//			throw e;
//		}
//	}
//	@Transactional
//	public Company findById(Long id) throws CustomException {
//		return this.findCompany(id, null);
//	}
//	@Transactional
//	public Company findByApiKeyId(String apiKey) throws CustomException {
//		return this.findCompany(null, apiKey);
//
//	}
//}