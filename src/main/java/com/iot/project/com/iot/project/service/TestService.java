//package com.iot.project.com.iot.project.service;
//
//
//import java.util.List;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.iot.project.com.iot.project.entity.Company;
//import com.iot.project.com.iot.project.repository.CompanyRepository;
//import com.iot.project.com.iot.project.repository.LocationRepository;
//import com.iot.project.com.iot.project.repository.SensorDataRepository;
//import com.iot.project.com.iot.project.repository.SensorRepository;
//
//import lombok.RequiredArgsConstructor;
//
//
//@Service
//@RequiredArgsConstructor
//public class TestService {
//	private final CompanyRepository companyRepository;
//	private final SensorRepository sensorRepository;
//	private final SensorDataRepository sensorDataRepository;
//	private final LocationRepository locationRepository;	
//	private final Log logger = LogFactory.getLog(this.getClass());
//
//	@Transactional
//	public void test()  {
//
////		ObjectMapper _myMapper = new ObjectMapper();
//////		Company _company = new Company(null, "company1", "myApiKey", null);
//////		this.companyRepository.save(_company);
////		Company _company = this.companyRepository.findById(15L).get();
////			System.out.println( _company.getLocations());
//		this.testLocation();
//	}
//	@Transactional
//	public void testLocation()  {
//		List<Company> _companies = this.companyRepository.findByName("CompanyForTesting");
//		if(_companies != null) {
//			_companies.forEach(_entity -> this.companyRepository.deleteById(_entity.getId()));
//		}
////		ObjectMapper _myMapper = new ObjectMapper();
//////		Company _company = new Company(null, "company1", "myApiKey", null);
//////		this.companyRepository.save(_company);
////		Company _company = this.companyRepository.findById(15L).get();
////			System.out.println( _company.getLocations());
//	}
//	
//	
//}